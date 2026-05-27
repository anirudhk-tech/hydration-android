package com.example.mizu.data.repository

import android.util.Log
import com.example.mizu.data.local.dao.WeightReadingDao
import com.example.mizu.data.local.entity.WeightReadingEntity
import com.example.mizu.data.sensor.WeightSensorService
import com.example.mizu.domain.WeightReading
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HydrationRepository @Inject constructor (
    private val sensorService: WeightSensorService,
    private val dao: WeightReadingDao
) {

    fun weightReadings(): Flow<WeightReading> {
        return sensorService.weightReadings()
            .map { bytes -> bytes.toWeightReading() }
            .onEach { reading -> dao.insert(reading.toEntity())}
    }

    fun readingHistory(): Flow<List<WeightReading>> {
        return dao.getAllReadings()
            .map { entities -> entities.map { it.toDomain() }}
    }
}

private fun ByteArray.toWeightReading(): WeightReading {
    val raw = ((this[2].toInt() and 0xFF) shl 8) or (this[1].toInt() and 0xFF)
    return WeightReading(weightKg = raw * 0.005)
}

private fun WeightReading.toEntity(): WeightReadingEntity {
    return WeightReadingEntity(weightKg = this.weightKg, timestamp = this.timestamp)
}

private fun WeightReadingEntity.toDomain(): WeightReading {
    return WeightReading(weightKg = this.weightKg, timestamp = this.timestamp)
}