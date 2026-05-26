package com.example.mizu.features.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mizu.data.repository.HydrationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val repository: HydrationRepository
) : ViewModel() {

    val uiState: StateFlow<HistoryState> = repository.readingHistory()
        .map { readings -> HistoryState(readings = readings) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = HistoryState()
        )
}
