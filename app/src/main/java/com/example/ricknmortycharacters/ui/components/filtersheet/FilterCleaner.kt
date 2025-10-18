package com.example.ricknmortycharacters.ui.components.filtersheet

import androidx.compose.runtime.Composable

@Composable
fun FilterCleaner(
    hasActiveFilters: Boolean,
    onClearAll: () -> Unit
) {
    FilterItem(
        displayName = "Clear All Filters",
        isSelected = !hasActiveFilters,
        onClick = onClearAll
    )
}