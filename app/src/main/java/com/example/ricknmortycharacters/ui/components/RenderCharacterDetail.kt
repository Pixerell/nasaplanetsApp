package com.example.ricknmortycharacters.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.ricknmortycharacters.data.db.CartoonCharacter
import com.example.ricknmortycharacters.domain.formatCreated
import com.example.ricknmortycharacters.domain.getStatusColor

@Composable
fun RenderCharacterDetail(character: CartoonCharacter) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Card(elevation = CardDefaults.cardElevation(4.dp)) {
            Column {
                AsyncImage(
                    model = character.image,
                    contentDescription = character.name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f),
                    contentScale = ContentScale.Crop
                )
                CharacterDetailsContent(character)
            }
        }
    }
}

@Composable
private fun CharacterDetailsContent(character: CartoonCharacter) {
    Column(Modifier.padding(16.dp)) {
        Text(
            text = character.name,
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(12.dp))
        CharacterDetailItem("Status", character.status, getStatusColor(character.status))
        CharacterDetailItem("Species", character.species)
        CharacterDetailItem("Gender", character.gender)

        if (character.type.isNotEmpty()) {
            CharacterDetailItem("Type", character.type)
        }
        Spacer(modifier = Modifier.height(12.dp))
        CharacterDetailItem("Origin", character.origin.name)
        CharacterDetailItem("Location", character.location.name)
        Spacer(modifier = Modifier.height(12.dp))
        Text(text = "Appeared in ${character.episode.size} episodes")
        Spacer(modifier = Modifier.height(12.dp))
        Text(text = "Created: ${formatCreated(character.created)}")
    }
}

@Composable
private fun CharacterDetailItem(
    label: String,
    value: String,
    valueColor: Color = MaterialTheme.colorScheme.onSurface
) {
    Row(Modifier.padding(vertical = 2.dp)) {
        Text(
            text = "$label: ",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = valueColor
        )
    }
}