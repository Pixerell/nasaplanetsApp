package com.example.ricknmortycharacters.data.api

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

data class CartoonCharacter(
    val id: Int,
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
    val created: String
)

data class LocationRef(
    val name: String,
    val url: String
)