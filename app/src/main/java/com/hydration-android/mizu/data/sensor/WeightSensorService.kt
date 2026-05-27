package com.example.mizu.data.sensor

import kotlinx.coroutines.flow.Flow

interface WeightSensorService {
    fun weightReadings() : Flow<ByteArray>
}