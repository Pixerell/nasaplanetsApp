package com.example.ricknmortycharacters.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.ricknmortycharacters.data.api.CartoonCharacter
import com.example.ricknmortycharacters.domain.formatCreated
import com.example.ricknmortycharacters.domain.getStatusColor

@Composable
fun RenderCharacterDetail(character: CartoonCharacter) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column {
                AsyncImage(
                    model = character.image,
                    contentDescription = character.name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f),
                    contentScale = androidx.compose.ui.layout.ContentScale.Crop
                )

                Spacer(modifier = Modifier.height(12.dp))

                Column(
                    modifier = Modifier.padding(horizontal = 16.dp)
                ){
                    Text(text = character.name, style = MaterialTheme.typography.headlineSmall)
                    Spacer(modifier = Modifier.height(12.dp))
                    val statusColor = getStatusColor(character.status)
                    ExpandableText(
                        text = "Status: ${character.status}",
                        color = statusColor,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(text = "Species: ${character.species}")
                    Text(text = "Gender: ${character.gender}")
                    if (character.type.isNotEmpty()) Text(text = "Type: ${character.type}")
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(text = "Origin: ${character.origin.name}")
                    Text(text = "Location: ${character.location.name}")
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(text = "Appeared in ${character.episode.size} episodes")
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Created: ${formatCreated(character.created)}",
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                }

            }
        }
    }
}