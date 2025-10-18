package com.example.ricknmortycharacters

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.ricknmortycharacters.ui.components.filtersheet.FilterSheet
import com.example.ricknmortycharacters.ui.screens.CharacterDetailScreen
import com.example.ricknmortycharacters.ui.screens.CharactersScreen
import com.example.ricknmortycharacters.ui.screens.TopBar
import com.example.ricknmortycharacters.ui.viewmodels.CharacterDetailViewModel
import com.example.ricknmortycharacters.ui.viewmodels.CharactersViewModel
import dagger.hilt.android.AndroidEntryPoint

private const val CHARACTERROUTE = "characters"

@AndroidEntryPoint
class MainActivity : ComponentActivity() {


    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            val viewModel: CharactersViewModel = hiltViewModel()
            val navController = rememberNavController()

            var isDarkTheme by remember { mutableStateOf(false) }
            val colors = if (isDarkTheme) darkColorScheme() else lightColorScheme()
            var showFilters by remember { mutableStateOf(false) }

            val searchQuery by viewModel.searchQuery.collectAsState()
            val activeStatusFilter by viewModel.activeStatusFilter.collectAsState()
            val activeGenderFilter by viewModel.activeGenderFilter.collectAsState()
            val activeSpeciesFilter by viewModel.activeSpeciesFilter.collectAsState()

            LaunchedEffect(Unit) {
                try {
                    viewModel.fetch(page = 1)
                } catch (e: Exception) {
                    Log.e("MainActivity", "Fetch failed", e)
                }
            }

            MaterialTheme(colorScheme = colors) {
                Scaffold(
                    containerColor = colors.background,
                    topBar = {
                        val navBackStackEntry by navController.currentBackStackEntryAsState()
                        val canNavigateBack =
                            navBackStackEntry?.destination?.route != CHARACTERROUTE

                        var isSearchActive by remember { mutableStateOf(false) }

                        TopBar(
                            title = "Characters",
                            canNavigateBack = canNavigateBack,
                            onBackClick = { navController.popBackStack() },
                            onThemeSwitchClick = { isDarkTheme = !isDarkTheme },
                            isSearchActive = isSearchActive,
                            searchQuery = searchQuery,
                            onSearchToggle = {
                                isSearchActive = !isSearchActive
                                if (!isSearchActive) {
                                    viewModel.clearSearchAndFilters()
                                }
                            },
                            onSearchChange = { query ->
                                viewModel.searchCharacters(query)
                            },
                            onFilterClick = { showFilters = true }
                        )

                        if (showFilters) {
                            FilterSheet(
                                onSelect = { status, gender, species ->
                                    viewModel.applyFilter(status, gender, species)
                                    showFilters = false
                                },
                                currentStatusFilter = activeStatusFilter,
                                currentGenderFilter = activeGenderFilter,
                                currentSpeciesFilter = activeSpeciesFilter,
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
                            CharactersScreen(
                                viewModel = viewModel,
                                onCharacterClick = { characterId ->
                                    navController.navigate("details/$characterId")
                                }
                            )
                        }
                        composable(
                            route = "details/{charId}",
                            arguments = listOf(navArgument("charId") { type = NavType.IntType })
                        ) { backStackEntry ->
                            val charId = backStackEntry.arguments?.getInt("charId") ?: 0
                            val detailViewModel: CharacterDetailViewModel = hiltViewModel()
                            CharacterDetailScreen(
                                characterId = charId,
                                viewModel = detailViewModel
                            )
                        }

                    }
                }
            }
        }
    }
}
