package com.example.mizu.data.remote.dto

import com.example.mizu.domain.WeightReading
import com.google.gson.annotations.SerializedName

data class WeightReadingDto(
    @SerializedName("weight_kg") val weightKg: Double,
    @SerializedName("timestamp") val timestamp: Long
)

fun WeightReadingDto.toDomain() = WeightReading(weightKg = weightKg, timestamp = timestamp)
