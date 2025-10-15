package com.example.ricknmortycharacters.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.ricknmortycharacters.ui.viewmodels.CharactersViewModel

@Composable
fun CharactersScreen(viewModel: CharactersViewModel, onCharacterClick: (String) -> Unit, modifier: Modifier = Modifier) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier,
        contentPadding = PaddingValues(8.dp)
    ) {
        items(viewModel.characters) { character ->
            CharacterCard(
                characterName = character,
                modifier = Modifier.clickable { onCharacterClick(character) }
            )
        }
    }
}

