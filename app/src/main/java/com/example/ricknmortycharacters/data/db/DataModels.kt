package com.example.ricknmortycharacters.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

data class CharacterResponse(
    val info: Info,
    val results: List<CartoonCharacter>
)

data class Info(
    val count: Int,
    val pages: Int,
    val next: String?,
    val prev: String?
)

@Entity(tableName = "characters")
data class CartoonCharacter(
    @PrimaryKey val id: Int,
    val name: String,
    val status: String,
    val species: String,
    val type: String,
    val gender: String,
    val origin: LocationRef,
    val location: LocationRef,
    val image: String,
    val episode: List<String>,
    val url: String,
    val created: String,
    val page: Int = 0
)

data class LocationRef(
    val name: String,
    val url: String
)

data class CharacterFilter(
    val status: String? = null,
    val gender: String? = null,
    val name: String? = null
)