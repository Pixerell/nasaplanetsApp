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
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.ricknmortycharacters.data.api.RetrofitClient
import com.example.ricknmortycharacters.data.db.AppDatabase
import com.example.ricknmortycharacters.data.db.CharactersRepository
import com.example.ricknmortycharacters.ui.components.FilterSheet
import com.example.ricknmortycharacters.ui.screens.CharacterDetailScreen
import com.example.ricknmortycharacters.ui.screens.CharactersScreen
import com.example.ricknmortycharacters.ui.screens.TopBar
import com.example.ricknmortycharacters.ui.viewmodels.CharacterDetailViewModel
import com.example.ricknmortycharacters.ui.viewmodels.CharactersViewModel
import com.example.ricknmortycharacters.ui.viewmodels.CharactersViewModelFactory
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val CHARACTERROUTE = "characters"

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var viewModel: CharactersViewModel

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            val repository = withContext(Dispatchers.IO) {
                val db = AppDatabase.getInstance(applicationContext)
                val dao = db.characterDao()
                CharactersRepository(RetrofitClient.api, dao)
            }
            viewModel = CharactersViewModelFactory(repository)
                .create(CharactersViewModel::class.java)

            setContent {
                val navController = rememberNavController()

                var isDarkTheme by remember { mutableStateOf(false) }
                val colors = if (isDarkTheme) darkColorScheme() else lightColorScheme()
                var showFilters by remember { mutableStateOf(false) }

                val searchQuery by viewModel.searchQuery.collectAsState()
                val activeStatusFilter by viewModel.activeStatusFilter.collectAsState()
                val activeGenderFilter by viewModel.activeGenderFilter.collectAsState()

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
                                    onSelect = { status, gender ->
                                        viewModel.applyFilter(status, gender)
                                        showFilters = false
                                    },
                                    currentStatusFilter = activeStatusFilter,
                                    currentGenderFilter = activeGenderFilter,
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
                                CharacterDetailScreen(characterId = charId, viewModel = detailViewModel)
                            }

                        }
                    }
                }
            }
        }
    }
}
