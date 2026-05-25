package com.example.mizu.domain

data class WeightReading(
    val weightKg: Double,
    val timestamp: Long = System.currentTimeMillis()
)