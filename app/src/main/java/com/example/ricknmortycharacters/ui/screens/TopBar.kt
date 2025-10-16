package com.example.ricknmortycharacters.ui.screens

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Brightness4
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    title: String = "Planets",
    canNavigateBack: Boolean = false,
    isSearchActive: Boolean,
    searchQuery: String,
    onSearchChange: (String) -> Unit,
    onSearchToggle: () -> Unit,
    onFilterClick: () -> Unit = {},
    onBackClick: () -> Unit = {},
    onThemeSwitchClick: () -> Unit = {}
) {

    val focusRequester = remember { FocusRequester() }

    TopAppBar(
        title = {
            if (isSearchActive) {
                focusRequester.requestFocus()
                TextField(
                    value = searchQuery,
                    onValueChange = { new -> onSearchChange(new) },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequester),
                    placeholder = { Text("Search characters") },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { onSearchChange("") }) {
                                Icon(Icons.Default.Clear, contentDescription = "Clear")
                            }
                        }
                    }
                )
            } else {
                Text(title)
            }
        },
        navigationIcon = {
            IconButton(
                onClick = {
                    if (canNavigateBack) onBackClick()
                    else onThemeSwitchClick()
                }
            ) {
                Icon(
                    imageVector = if (canNavigateBack) Icons.AutoMirrored.Filled.ArrowBack else Icons.Filled.Brightness4,
                    contentDescription = if (canNavigateBack) "Back" else "Switch"
                )
            }
        },
        actions = {
            IconButton(onClick = onSearchToggle) { Icon(Icons.Filled.Search, contentDescription = "Search") }
            IconButton(onClick = onFilterClick) { Icon(Icons.Filled.FilterList, contentDescription = "Filter") }
        },
        modifier = Modifier.fillMaxWidth()
    )
}
