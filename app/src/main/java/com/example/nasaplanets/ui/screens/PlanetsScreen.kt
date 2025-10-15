package com.example.nasaplanets.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.nasaplanets.ui.viewmodels.PlanetsViewModel

@Composable
fun PlanetsScreen(viewModel: PlanetsViewModel, modifier: Modifier = Modifier) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier,
        contentPadding = androidx.compose.foundation.layout.PaddingValues(8.dp)
    ) {
        items(viewModel.planets) { planet ->
            Card(
                modifier = Modifier.padding(8.dp),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Text(
                    text = planet,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}
