package com.example.mizu.features.dashboard

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mizu.data.repository.HydrationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor (
    private val repository: HydrationRepository
) : ViewModel() {

    private val _goalLiters = MutableStateFlow(3.5)
    val uiState: StateFlow<DashboardState> = combine(
        repository.weightReadings().catch { e -> Log.e("DashboardViewModel", "Flow error", e) },
        _goalLiters
    ) { reading, goal ->
        DashboardState(weightKg = reading.weightKg, goalLiters = goal)
    }
    .distinctUntilChanged()
    .stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = DashboardState()
    )
}
