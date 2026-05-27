package com.example.mizu.features.history

import app.cash.turbine.test
import com.example.mizu.data.repository.HydrationRepository
import com.example.mizu.domain.WeightReading
import com.example.mizu.util.MainDispatcherRule
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class HistoryViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val repository = mockk<HydrationRepository>()

    @Test
    fun `initial state has no readings`() = runTest {
        every { repository.readingHistory() } returns flowOf()

        val viewModel = HistoryViewModel(repository)

        assertTrue(viewModel.uiState.value.readings.isEmpty())
    }

    @Test
    fun `uiState exposes the readings emitted by the repository`() = runTest {
        val readings = listOf(
            WeightReading(weightKg = 72.5, timestamp = 3000L),
            WeightReading(weightKg = 71.0, timestamp = 2000L),
        )
        every { repository.readingHistory() } returns flowOf(readings)

        val viewModel = HistoryViewModel(repository)

        viewModel.uiState.test {
            skipItems(1) // initial empty HistoryState()
            assertEquals(readings, awaitItem().readings)
            cancelAndIgnoreRemainingEvents()
        }
    }
}
