package com.example.ricknmortycharacters.ui.screens

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.ricknmortycharacters.ui.viewmodels.CharactersViewModel
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import com.example.ricknmortycharacters.ui.components.CharacterCard
import com.example.ricknmortycharacters.ui.components.PaginationRow
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.text.style.TextAlign


@Composable
fun CharactersScreen(
    viewModel: CharactersViewModel,
    onCharacterClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val characters by viewModel.characters.collectAsState()
    val currentPage by viewModel.currentPage.collectAsState()
    val totalPages by viewModel.totalPages.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    val isRefreshing by remember { derivedStateOf { isLoading && characters.isNotEmpty() } }
    val isInitialLoading by remember { derivedStateOf { isLoading && characters.isEmpty() } }

    val context = androidx.compose.ui.platform.LocalContext.current


    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = {
            if (!viewModel.isOnline()) {
                Toast.makeText(context, "No Internet Connection. Showing Cache", Toast.LENGTH_SHORT).show()
            }
            else {
                Toast.makeText(context, "Fetching Data...", Toast.LENGTH_SHORT).show()
            }
            viewModel.fetch(page = 1)
        },
        modifier = modifier.fillMaxSize()
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            if (isInitialLoading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .fillMaxSize()
                        .wrapContentSize(Alignment.Center)
                )
            } else if (characters.isEmpty() && currentPage <= 1) {
                Text(
                    text = "No characters found\nPull down to refresh",
                    modifier = Modifier
                        .fillMaxSize()
                        .wrapContentSize(Alignment.Center),
                    textAlign = TextAlign.Center
                )
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(8.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(characters) { cartoonCharacter ->
                        CharacterCard(
                            cartoonCharacter = cartoonCharacter,
                            modifier = Modifier.clickable { onCharacterClick(cartoonCharacter.id) }
                        )
                    }

                    item(span = { GridItemSpan(this.maxLineSpan) }) {
                        PaginationRow(
                            currentPage = currentPage,
                            totalPages = totalPages,
                            onPreviousPage = { viewModel.previousPage() },
                            onNextPage = { viewModel.nextPage() }
                        )
                    }
                }
            }

            // Show a small loading indicator at the bottom during pagination
            if (isLoading && characters.isNotEmpty() && !isRefreshing) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(16.dp)
                )
            }
        }
    }
}