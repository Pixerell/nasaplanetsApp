package com.example.nasaplanets.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Brightness4
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    title: String = "Planets",
    canNavigateBack: Boolean = false,
    onMenuClick: () -> Unit = {},
    onBackClick: () -> Unit = {},
    onThemeSwitchClick: () -> Unit = {}
) {
    TopAppBar(
        title = { Text(title) },
        navigationIcon = {
            IconButton(
                onClick = {
                    if (canNavigateBack) onBackClick()
                    else onMenuClick()
                }
            ) {
                Icon(
                    imageVector = if (canNavigateBack) Icons.AutoMirrored.Filled.ArrowBack else Icons.Filled.Menu,
                    contentDescription = if (canNavigateBack) "Back" else "Menu"
                )
            }
        },
        actions = {
            IconButton(onClick = { onThemeSwitchClick()}) {
                Icon(Icons.Filled.Brightness4, contentDescription = "Theme switcher")
            }
        },
        modifier = Modifier.fillMaxWidth()
    )
}
