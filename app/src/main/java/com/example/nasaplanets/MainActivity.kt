package com.example.nasaplanets

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.ui.Modifier
import androidx.activity.viewModels
import androidx.compose.material3.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.nasaplanets.ui.screens.PlanetsScreen
import com.example.nasaplanets.ui.viewmodels.PlanetsViewModel

class MainActivity : ComponentActivity() {
    private val viewModel: PlanetsViewModel by viewModels()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            MaterialTheme {
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = { Text("Planets") }
                        )
                    }
                ) { padding ->
                    NavHost(
                        navController = navController,
                        startDestination = "planets",
                        modifier = Modifier.padding(padding)
                    ) {
                        composable("planets") {
                            PlanetsScreen(viewModel)
                        }
                        // позже добавим detail screen
                    }
                }
            }
        }
    }
}