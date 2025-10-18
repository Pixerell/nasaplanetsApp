package com.example.ricknmortycharacters.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
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

    Box(modifier = modifier.fillMaxSize()) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize(Alignment.Center)
            )
        } else if (characters.isEmpty() && currentPage <= 1) {
            Text(
                text = "No characters found",
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize(Alignment.Center)
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
    }
}