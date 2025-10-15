package com.example.nasaplanets

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.activity.viewModels
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.nasaplanets.ui.TopBar
import com.example.nasaplanets.ui.screens.PlanetDetailScreen
import com.example.nasaplanets.ui.screens.PlanetsScreen
import com.example.nasaplanets.ui.viewmodels.PlanetsViewModel

class MainActivity : ComponentActivity() {
    private val viewModel: PlanetsViewModel by viewModels()
    private val PLANETROUTE = "planets"

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()

            // Theme state
            var isDarkTheme by remember { mutableStateOf(false) }
            val colors = if (isDarkTheme) darkColorScheme() else lightColorScheme()

            MaterialTheme(
                colorScheme = colors,
            ) {
                Scaffold(
                    containerColor = colorScheme.background,
                    topBar = {
                        val navBackStackEntry by navController.currentBackStackEntryAsState()
                        val canNavigateBack = navBackStackEntry?.destination?.route != PLANETROUTE
                        TopBar(
                            title = "Planets",
                            canNavigateBack = canNavigateBack,
                            onBackClick = { navController.popBackStack() },
                            onThemeSwitchClick = { isDarkTheme = !isDarkTheme }
                        )
                    }
                ) { padding ->
                    NavHost(
                        navController = navController,
                        startDestination = PLANETROUTE,
                        modifier = Modifier.padding(padding)
                    ) {
                        composable(PLANETROUTE) {
                            PlanetsScreen(viewModel = viewModel, onPlanetClick = { planet ->
                                navController.navigate("details/$planet")
                            })
                        }
                        composable(
                            route = "details/{planetName}",
                            arguments = listOf(navArgument("planetName") { type = NavType.StringType })
                        ) { backStackEntry ->
                            PlanetDetailScreen(backStackEntry.arguments?.getString("planetName") ?: "")
                        }
                    }
                }
            }
        }
    }
}
