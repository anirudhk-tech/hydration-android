package com.example.mizu.data.sensor

import app.cash.turbine.test
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class MockWeightSensorServiceTest {

    @Test
    fun `emits a 3-byte frame that decodes to the starting weight`() = runTest {
        val service = MockWeightSensorService()

        service.weightReadings().test {
            val frame = awaitItem()

            // flags byte + uint16 little-endian at 0.005 kg resolution
            assertEquals(3, frame.size)
            val raw = ((frame[2].toInt() and 0xFF) shl 8) or (frame[1].toInt() and 0xFF)
            assertEquals(72.2, raw * 0.005, 0.01)

            cancelAndIgnoreRemainingEvents()
        }
    }
}
