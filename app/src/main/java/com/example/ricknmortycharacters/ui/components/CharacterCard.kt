package com.example.ricknmortycharacters.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.ricknmortycharacters.data.api.CartoonCharacter
import com.example.ricknmortycharacters.domain.getStatusColor

@Composable
fun CharacterCard(cartoonCharacter: CartoonCharacter, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.padding(8.dp),
        elevation = CardDefaults.cardElevation(4.dp),
    ) {
        Column {
            AsyncImage(
                model = cartoonCharacter.image,
                contentDescription = "${cartoonCharacter.name} Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f),
                contentScale = ContentScale.Crop
            )

            Column(modifier = Modifier.padding(8.dp)) {
                ExpandableText(
                    text = cartoonCharacter.name,
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.titleMedium
                )

                ExpandableText(
                    text = "${cartoonCharacter.gender} | ${cartoonCharacter.species}",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodyMedium
                )

                val statusColor = getStatusColor(cartoonCharacter.status)
                ExpandableText(
                    text = "Status: ${cartoonCharacter.status}",
                    color = statusColor,
                    style = MaterialTheme.typography.bodyMedium
                )

                ExpandableText(
                    text = if (cartoonCharacter.type.isNotEmpty()) cartoonCharacter.type else "\u00A0",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodyMedium
                )


            }
        }
    }
}