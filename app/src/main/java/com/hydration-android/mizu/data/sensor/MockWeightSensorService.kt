package com.example.mizu.data.sensor

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import kotlin.random.Random

class MockWeightSensorService @Inject constructor() : WeightSensorService {
    override fun weightReadings(): Flow<ByteArray> = flow {
        var currentWeight = 72.2
        while (true) {
            emit(currentWeight.toWeightBytes())
            currentWeight += Random.nextDouble(-0.3, 0.3)
            delay(3000L)
        }
    }.flowOn(Dispatchers.IO)
}

private fun Double.toWeightBytes(): ByteArray {
    val raw = (this / 0.005).toInt()

    return byteArrayOf(
        0x00,
        (raw and 0xFF).toByte(),
        ((raw shr 8) and 0xFF).toByte()
    )
}