package com.example.nasaplanets.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.nasaplanets.ui.viewmodels.PlanetsViewModel

@Composable
fun PlanetsScreen(viewModel: PlanetsViewModel, onPlanetClick: (String) -> Unit, modifier: Modifier = Modifier) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier,
        contentPadding = PaddingValues(8.dp)
    ) {
        items(viewModel.planets) { planet ->
            PlanetCard(
                planetName = planet,
                modifier = Modifier.clickable { onPlanetClick(planet) }
            )
        }
    }
}

