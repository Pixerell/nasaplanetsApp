package com.example.ricknmortycharacters.ui.components.filtersheet

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun FilterSection(
    title: String,
    options: List<Pair<String, String>>,
    selectedValue: String?,
    onSelectionChange: (String?) -> Unit
) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier.padding(vertical = 8.dp)
    )
    options.forEach { (value, displayName) ->
        FilterItem(
            displayName = displayName,
            isSelected = selectedValue == value,
            onClick = { onSelectionChange(if (selectedValue == value) null else value) }
        )
        HorizontalDivider()
    }
}