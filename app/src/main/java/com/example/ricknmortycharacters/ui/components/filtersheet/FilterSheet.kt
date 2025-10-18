package com.example.ricknmortycharacters.ui.components.filtersheet


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterSheet(
    onSelect: (status: String?, gender: String?, species: String?) -> Unit,
    currentStatusFilter: String?,
    currentGenderFilter: String?,
    currentSpeciesFilter: String?,
    onDismiss: () -> Unit
) {
    var selectedStatus by remember { mutableStateOf(currentStatusFilter) }
    var selectedGender by remember { mutableStateOf(currentGenderFilter) }
    var selectedSpecies by remember { mutableStateOf(currentSpeciesFilter) }

    LaunchedEffect(currentStatusFilter, currentGenderFilter, currentSpeciesFilter) {
        selectedStatus = currentStatusFilter
        selectedGender = currentGenderFilter
        selectedSpecies = currentSpeciesFilter
    }

    val statusOptions = listOf(
        "alive" to "Alive",
        "dead" to "Dead",
        "unknown" to "Unknown"
    )

    val genderOptions = listOf(
        "female" to "Female",
        "male" to "Male",
        "unknown" to "Unknown"
    )

    val speciesOptions = listOf(
        "human" to "Human",
        "alien" to "Alien",
        "humanoid" to "Humanoid",
        "unknown" to "Unknown",
        "poopybutthole" to "Poopybutthole",
        "robot" to "Robot",
        "mythological creature" to "Mythological Creature",
    )

    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = "Filter Characters",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            SelectedFiltersDisplay(
                selectedStatus = selectedStatus,
                selectedGender = selectedGender,
                selectedSpecies = selectedSpecies
            )

            FilterSection(
                title = "Status",
                options = statusOptions,
                selectedValue = selectedStatus,
                onSelectionChange = { selectedStatus = it }
            )

            Spacer(modifier = Modifier.height(16.dp))

            FilterSection(
                title = "Gender",
                options = genderOptions,
                selectedValue = selectedGender,
                onSelectionChange = { selectedGender = it }
            )

            Spacer(modifier = Modifier.height(16.dp))

            FilterSection(
                title = "Species",
                options = speciesOptions,
                selectedValue = selectedSpecies,
                onSelectionChange = { selectedSpecies = it }
            )

            Spacer(modifier = Modifier.height(16.dp))

            FilterCleaner(
                hasActiveFilters = listOf(
                    selectedStatus,
                    selectedGender,
                    selectedSpecies
                ).any { it != null },
                onClearAll = {
                    selectedStatus = null
                    selectedGender = null
                    selectedSpecies = null
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            FilterActions(
                onCancel = onDismiss,
                onApply = {
                    onSelect(selectedStatus, selectedGender, selectedSpecies)
                    onDismiss()
                }
            )
        }
    }
}