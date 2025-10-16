package com.example.ricknmortycharacters

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.activity.viewModels
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.LaunchedEffect
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
import com.example.ricknmortycharacters.ui.components.FilterSheet
import com.example.ricknmortycharacters.ui.screens.TopBar
import com.example.ricknmortycharacters.ui.screens.CharacterDetailScreen
import com.example.ricknmortycharacters.ui.screens.CharactersScreen
import com.example.ricknmortycharacters.ui.viewmodels.CharactersViewModel

class MainActivity : ComponentActivity() {
    private val viewModel: CharactersViewModel by viewModels()
    private val CHARACTERROUTE = "characters"

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()

            // Theme state
            var isDarkTheme by remember { mutableStateOf(false) }
            val colors = if (isDarkTheme) darkColorScheme() else lightColorScheme()
            var showFilters by remember { mutableStateOf(false) }
            var activeFilter by remember { mutableStateOf<String?>(null) }

            // Fetch characters once when the screen is first composed
            LaunchedEffect(Unit) {
                viewModel.fetch(page = 1)
            }

            MaterialTheme(
                colorScheme = colors,
            ) {
                Scaffold(
                    containerColor = colorScheme.background,
                    topBar = {
                        val navBackStackEntry by navController.currentBackStackEntryAsState()
                        val canNavigateBack = navBackStackEntry?.destination?.route != CHARACTERROUTE

                        var isSearchActive by remember { mutableStateOf(false) }
                        var searchQuery by remember { mutableStateOf("") }

                        TopBar(
                            title = "Characters",
                            canNavigateBack = canNavigateBack,
                            onBackClick = { navController.popBackStack() },
                            onThemeSwitchClick = { isDarkTheme = !isDarkTheme },
                            isSearchActive = isSearchActive,
                            searchQuery = searchQuery,
                            onSearchToggle = { isSearchActive = !isSearchActive },
                            onSearchChange = { searchQuery = it; viewModel.fetch(name = it.ifBlank { null }) },
                            onFilterClick = { showFilters = true },
                        )
                        if (showFilters) {
                            FilterSheet(
                                onSelect = { selected ->
                                    activeFilter = selected
                                    viewModel.fetch(page = 1, filter= selected, name = if (searchQuery.isBlank()) null else searchQuery,)
                                },
                                onDismiss = { showFilters = false }
                            )
                        }
                    }
                ) { padding ->
                    NavHost(
                        navController = navController,
                        startDestination = CHARACTERROUTE,
                        modifier = Modifier.padding(padding)
                    ) {
                        composable(CHARACTERROUTE) {
                            CharactersScreen(viewModel = viewModel, onCharacterClick = {characterId ->
                                navController.navigate("details/$characterId")
                            })
                        }
                        composable(
                            route = "details/{charId}",
                            arguments = listOf(navArgument("charId") { type = NavType.IntType })
                        ) { backStackEntry ->
                            val id = backStackEntry.arguments?.getInt("charId") ?: 0
                            CharacterDetailScreen(characterId = id)
                        }
                    }
                }
            }
        }
    }
}
