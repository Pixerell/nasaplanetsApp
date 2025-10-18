package com.example.ricknmortycharacters.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterSheet(
    onSelect: (status: String?, gender: String?) -> Unit,
    currentStatusFilter: String?,
    currentGenderFilter: String?,
    onDismiss: () -> Unit
) {
    var selectedStatus by remember { mutableStateOf(currentStatusFilter) }
    var selectedGender by remember { mutableStateOf(currentGenderFilter) }

    LaunchedEffect(currentStatusFilter, currentGenderFilter) {
        selectedStatus = currentStatusFilter
        selectedGender = currentGenderFilter
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

    ModalBottomSheet(
        onDismissRequest = onDismiss
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Filter Characters",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            if (selectedStatus != null || selectedGender != null) {
                Column(modifier = Modifier.padding(bottom = 16.dp)) {
                    Text(
                        text = "Selected Filters:",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    if (selectedStatus != null) {
                        Text(
                            text = "Status: ${selectedStatus}",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                    if (selectedGender != null) {
                        Text(
                            text = "Gender: ${selectedGender}",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }

            // Status Filters
            Text(
                text = "Status",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            statusOptions.forEach { (value, displayName) ->
                FilterItem(
                    displayName = displayName,
                    isSelected = selectedStatus == value,
                    onClick = {
                        // Toggle status filter - if already selected, clear it
                        selectedStatus = if (selectedStatus == value) null else value
                    }
                )
                HorizontalDivider()
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Gender Filters
            Text(
                text = "Gender",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            genderOptions.forEach { (value, displayName) ->
                FilterItem(
                    displayName = displayName,
                    isSelected = selectedGender == value,
                    onClick = {
                        // Toggle gender filter - if already selected, clear it
                        selectedGender = if (selectedGender == value) null else value
                    }
                )
                HorizontalDivider()
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Clear all filters option
            FilterItem(
                displayName = "Clear All Filters",
                isSelected = selectedStatus == null && selectedGender == null,
                onClick = {
                    selectedStatus = null
                    selectedGender = null
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Apply and Cancel buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Cancel button
                TextButton(
                    onClick = onDismiss,
                    modifier = Modifier.padding(end = 8.dp)
                ) {
                    Text("Cancel")
                }

                // Apply button
                Button(
                    onClick = {
                        // Apply the selected filters and close the sheet
                        onSelect(selectedStatus, selectedGender)
                        onDismiss()
                    }
                ) {
                    Text("Apply Filters")
                }
            }
        }
    }
}

@Composable
private fun FilterItem(
    displayName: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .background(
                color = if (isSelected) MaterialTheme.colorScheme.primaryContainer
                else MaterialTheme.colorScheme.background,
                shape = MaterialTheme.shapes.small
            )
            .padding(vertical = 12.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = displayName,
            color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer
            else MaterialTheme.colorScheme.onSurface,
            style = if (isSelected) MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.Bold
            ) else MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f)
        )
    }
}