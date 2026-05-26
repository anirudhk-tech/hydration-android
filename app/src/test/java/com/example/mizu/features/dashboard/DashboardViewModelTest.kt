package com.example.mizu.features.dashboard

import app.cash.turbine.test
import com.example.mizu.data.repository.HydrationRepository
import com.example.mizu.domain.WeightReading
import com.example.mizu.util.MainDispatcherRule
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class DashboardViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val repository = mockk<HydrationRepository>()

    @Test
    fun `initial state has default values`() = runTest {
        every { repository.weightReadings() } returns flowOf()

        val viewModel = DashboardViewModel(repository)

        assertEquals(72.2, viewModel.uiState.value.weightKg, 0.001)
        assertEquals(3.5, viewModel.uiState.value.goalLiters, 0.001)
    }

    @Test
    fun `uiState updates weightKg when repository emits a reading`() = runTest {
        val reading = WeightReading(weightKg = 75.0)
        every { repository.weightReadings() } returns flowOf(reading)

        val viewModel = DashboardViewModel(repository)

        viewModel.uiState.test {
            skipItems(1) // skip initial DashboardState()
            val updated = awaitItem()
            assertEquals(75.0, updated.weightKg, 0.001)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `uiState preserves goalLiters when weight updates`() = runTest {
        val reading = WeightReading(weightKg = 68.0)
        every { repository.weightReadings() } returns flowOf(reading)

        val viewModel = DashboardViewModel(repository)

        viewModel.uiState.test {
            skipItems(1)
            val updated = awaitItem()
            assertEquals(3.5, updated.goalLiters, 0.001)
            cancelAndIgnoreRemainingEvents()
        }
    }
}
