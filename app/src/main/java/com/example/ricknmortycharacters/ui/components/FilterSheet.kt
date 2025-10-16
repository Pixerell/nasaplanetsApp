package com.example.ricknmortycharacters.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterSheet(
    onSelect: (String) -> Unit,
    onDismiss: () -> Unit
) {
    // options in desired order
    val options = listOf("name", "status", "location", "gender")

    ModalBottomSheet(
        onDismissRequest = onDismiss
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Filter by", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(bottom = 8.dp))
            options.forEach { opt ->
                ListItem(
                    headlineContent = { Text(opt.replaceFirstChar { it.uppercase() }) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onSelect(opt)
                            onDismiss()
                        }
                )
                HorizontalDivider()
            }
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = onDismiss,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Close")
            }
        }
    }
}
