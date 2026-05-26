package com.example.mizu

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.mizu.features.dashboard.Dashboard
import com.example.mizu.navigation.AppNavigation
import com.example.mizu.navigation.BottomNavBar
import com.example.mizu.ui.theme.MizuTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MizuTheme {
                val navController = rememberNavController()

                Scaffold (
                    bottomBar = { BottomNavBar(navController) }
                ) { innerPadding ->
                    AppNavigation(navController = navController, modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}