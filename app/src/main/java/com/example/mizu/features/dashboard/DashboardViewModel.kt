package com.example.mizu.features.dashboard

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class DashboardViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(DashboardState(weightKg = 72.2, intakeLiters = 4.0, goalLiters = 15.0, name = "Anirudh"))
    val uiState: StateFlow<DashboardState> = _uiState.asStateFlow()
}