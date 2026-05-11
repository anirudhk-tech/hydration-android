package com.example.mizu.features.dashboard

data class DashboardState(
    val name: String = "Anirudh",
    val date: String = "Monday, May 11",
    val weightKg: Double = 72.2,
    val weightChangeKg: Double = -0.3,
    val intakeLiters: Double = 1.8,
    val goalLiters: Double = 3.5,
    val lastReadingTime: String = "2 min ago",
    val streakDays: Int = 5
)
