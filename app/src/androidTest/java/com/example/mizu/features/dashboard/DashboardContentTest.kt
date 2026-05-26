package com.example.mizu.features.dashboard

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.mizu.ui.theme.MizuTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DashboardContentTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun showsUserName() {
        composeTestRule.setContent {
            MizuTheme { DashboardContent(state = DashboardState(name = "Anirudh")) }
        }

        composeTestRule.onNodeWithText("Hey, Anirudh").assertIsDisplayed()
    }

    @Test
    fun showsCurrentWeight() {
        composeTestRule.setContent {
            MizuTheme { DashboardContent(state = DashboardState(weightKg = 75.0)) }
        }

        composeTestRule.onNodeWithText("75.0 kg").assertIsDisplayed()
    }

    @Test
    fun showsHydrationProgress() {
        composeTestRule.setContent {
            MizuTheme { DashboardContent(state = DashboardState(intakeLiters = 1.8, goalLiters = 3.5)) }
        }

        composeTestRule.onNodeWithText("1.8L").assertIsDisplayed()
        composeTestRule.onNodeWithText("of 3.5L").assertIsDisplayed()
    }

    @Test
    fun showsStreakDays() {
        composeTestRule.setContent {
            MizuTheme { DashboardContent(state = DashboardState(streakDays = 7)) }
        }

        composeTestRule.onNodeWithText("7").assertIsDisplayed()
    }
}
