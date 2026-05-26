package com.example.mizu.features.history

import com.example.mizu.domain.WeightReading

data class HistoryState(
    val readings: List<WeightReading> = emptyList()
)
