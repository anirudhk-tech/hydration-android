package com.example.mizu.data.repository

import android.util.Log
import com.example.mizu.data.sensor.WeightSensorService
import com.example.mizu.domain.WeightReading
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HydrationRepository @Inject constructor (private val sensorService: WeightSensorService){

    fun weightReadings(): Flow<WeightReading> {
        return sensorService.weightReadings()
            .map { bytes -> bytes.toWeightReading() }
            .onEach { reading -> Log.d("REPO", "Reading: ${reading.weightKg}") }
    }
}

private fun ByteArray.toWeightReading(): WeightReading {
    val raw = ((this[2].toInt() and 0xFF) shl 8) or (this[1].toInt() and 0xFF)
    return WeightReading(weightKg = raw * 0.005)
}