package com.example.mizu.data.repository

import app.cash.turbine.test
import com.example.mizu.data.local.dao.WeightReadingDao
import com.example.mizu.data.local.entity.WeightReadingEntity
import com.example.mizu.data.sensor.WeightSensorService
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class HydrationRepositoryTest {

    private val sensorService = mockk<WeightSensorService>()
    private val dao = mockk<WeightReadingDao>(relaxed = true)
    private val repository = HydrationRepository(sensorService, dao)

    // 72.2 kg encoded the way the sensor sends it: flags byte + uint16 little-endian
    // at 0.005 kg resolution -> raw = 72.2 / 0.005 = 14440 = 0x3868.
    private val seventyTwoPointTwo = byteArrayOf(0x00, 0x68, 0x38)

    @Test
    fun `weightReadings decodes sensor bytes into kilograms`() = runTest {
        every { sensorService.weightReadings() } returns flowOf(seventyTwoPointTwo)

        repository.weightReadings().test {
            assertEquals(72.2, awaitItem().weightKg, 0.001)
            awaitComplete()
        }
    }

    @Test
    fun `weightReadings persists every decoded reading to the dao`() = runTest {
        every { sensorService.weightReadings() } returns flowOf(seventyTwoPointTwo)

        repository.weightReadings().test {
            awaitItem()
            awaitComplete()
        }

        coVerify { dao.insert(match { it.weightKg in 72.19..72.21 }) }
    }

    @Test
    fun `readingHistory maps entities to domain models in order`() = runTest {
        val entities = listOf(
            WeightReadingEntity(id = 1, weightKg = 72.5, timestamp = 3000L),
            WeightReadingEntity(id = 2, weightKg = 71.0, timestamp = 2000L),
        )
        every { dao.getAllReadings() } returns flowOf(entities)

        repository.readingHistory().test {
            val readings = awaitItem()
            assertEquals(2, readings.size)
            assertEquals(72.5, readings[0].weightKg, 0.001)
            assertEquals(3000L, readings[0].timestamp)
            assertEquals(71.0, readings[1].weightKg, 0.001)
            awaitComplete()
        }
    }
}
