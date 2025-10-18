package com.example.ricknmortycharacters.ui.components.filtersheet

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SelectedFiltersDisplay(
    selectedStatus: String?,
    selectedGender: String?,
    selectedSpecies: String?
) {
    val activeFilters = listOfNotNull(
        selectedStatus?.let { "Status: $it" },
        selectedGender?.let { "Gender: $it" },
        selectedSpecies?.let { "Species: $it" }
    )

    if (activeFilters.isNotEmpty()) {
        Column(modifier = Modifier.padding(bottom = 16.dp)) {
            Text(
                text = "Selected Filters:",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )
            activeFilters.forEach { filter ->
                Text(text = filter, style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}