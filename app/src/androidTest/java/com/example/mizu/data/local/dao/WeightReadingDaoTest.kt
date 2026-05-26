package com.example.mizu.data.local.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.mizu.data.local.MizuDatabase
import com.example.mizu.data.local.entity.WeightReadingEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class WeightReadingDaoTest {

    private lateinit var database: MizuDatabase
    private lateinit var dao: WeightReadingDao

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, MizuDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        dao = database.weightReadingDao()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun insertAndRetrieve() = runTest {
        dao.insert(WeightReadingEntity(weightKg = 72.5, timestamp = 1000L))

        val readings = dao.getAllReadings().first()

        assertEquals(1, readings.size)
        assertEquals(72.5, readings[0].weightKg, 0.001)
    }

    @Test
    fun getAllReadings_orderedByTimestampDesc() = runTest {
        dao.insert(WeightReadingEntity(weightKg = 70.0, timestamp = 1000L))
        dao.insert(WeightReadingEntity(weightKg = 72.5, timestamp = 3000L))
        dao.insert(WeightReadingEntity(weightKg = 71.0, timestamp = 2000L))

        val readings = dao.getAllReadings().first()

        assertEquals(72.5, readings[0].weightKg, 0.001)
        assertEquals(71.0, readings[1].weightKg, 0.001)
        assertEquals(70.0, readings[2].weightKg, 0.001)
    }

    @Test
    fun getLatestReading_returnsNewestByTimestamp() = runTest {
        dao.insert(WeightReadingEntity(weightKg = 70.0, timestamp = 1000L))
        dao.insert(WeightReadingEntity(weightKg = 72.5, timestamp = 2000L))

        val latest = dao.getLatestReading().first()

        assertEquals(72.5, latest?.weightKg, 0.001)
    }

    @Test
    fun getAllReadings_emptyWhenNothingInserted() = runTest {
        val readings = dao.getAllReadings().first()

        assertEquals(0, readings.size)
    }
}
