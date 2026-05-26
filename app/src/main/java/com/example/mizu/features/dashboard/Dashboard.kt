package com.example.mizu.features.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mizu.ui.theme.MizuTheme

@Composable
fun Dashboard(
    modifier: Modifier = Modifier,
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    DashboardContent(state = state, modifier = modifier)
}

@Composable
fun DashboardContent(
    state: DashboardState,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp)
            .padding(bottom = 32.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Column(modifier = Modifier.padding(top = 8.dp)) {
            Text(
                text = "Hey, ${state.name}",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = state.date,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        HydrationCard(state = state)

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            WeightCard(state = state, modifier = Modifier.weight(1f))
            StreakCard(state = state, modifier = Modifier.weight(1f))
        }

        LastReadingCard(state = state)
    }
}

@Composable
private fun HydrationCard(state: DashboardState, modifier: Modifier = Modifier) {
    val progress = (state.intakeLiters / state.goalLiters).coerceIn(0.0, 1.0).toFloat()
    val percentage = (progress * 100).toInt()

    ElevatedCard(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "TODAY'S HYDRATION",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    text = "${state.intakeLiters}L",
                    style = MaterialTheme.typography.displaySmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Text(
                    text = "of ${state.goalLiters}L",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
            )
            Text(
                text = "$percentage% of daily goal",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun WeightCard(state: DashboardState, modifier: Modifier = Modifier) {
    val changePrefix = if (state.weightChangeKg >= 0) "+" else ""

    ElevatedCard(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = "WEIGHT",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "${state.weightKg} kg",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "$changePrefix${state.weightChangeKg} kg today",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun StreakCard(state: DashboardState, modifier: Modifier = Modifier) {
    ElevatedCard(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = "STREAK",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "${state.streakDays}",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "days in a row",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun LastReadingCard(state: DashboardState, modifier: Modifier = Modifier) {
    ElevatedCard(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(
                    text = "LAST READING",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "${state.weightKg} kg",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            Text(
                text = state.lastReadingTime,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DashboardPreview() {
    MizuTheme {
        DashboardContent(
            state = DashboardState(),
            modifier = Modifier.padding(16.dp)
        )
    }
}
