package com.example.ricknmortycharacters.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ricknmortycharacters.ui.viewmodels.CharacterDetailViewModel
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import com.example.ricknmortycharacters.ui.components.RenderCharacterDetail


@Composable
fun CharacterDetailScreen(characterId: Int, viewModel: CharacterDetailViewModel = viewModel()) {

    val character by viewModel.character.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    Log.d("Charactersdetails", "${character}")
    LaunchedEffect(characterId) {
        viewModel.fetchById(characterId)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        when {
            isLoading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }

            error != null -> {
                Text(
                    text = "Error: $error",
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.Center)
                )
            }

            character != null -> {
                RenderCharacterDetail(character!!)
            }

            else -> {
                Text(
                    text = "No data",
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}
