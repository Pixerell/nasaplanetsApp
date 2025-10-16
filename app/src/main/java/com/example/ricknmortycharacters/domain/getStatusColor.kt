package com.example.ricknmortycharacters.domain

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun getStatusColor(status: String): Color {
    return when (status.lowercase()) {
        "alive" -> Color(0xFF4CAF50)
        "dead" -> Color(0xFFF44336)
        else -> MaterialTheme.colorScheme.onSurfaceVariant
    }
}
