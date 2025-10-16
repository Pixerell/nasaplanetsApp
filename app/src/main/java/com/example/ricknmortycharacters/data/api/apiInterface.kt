package com.example.ricknmortycharacters.data.api

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RickAndMortyApi {

    @GET("character")
    suspend fun getCharacters(
        @Query("id") id: Int? = null,
        @Query("page") page: Int? = null,
        @Query("name") name: String? = null,
        @Query("status") status: String? = null,
        @Query("species") species: String? = null,
        @Query("type") type: String? = null,
        @Query("gender") gender: String? = null
    ): CharacterResponse

    @GET("character/{id}")
    suspend fun getCharacter(
        @Path("id") id: Int
    ): CartoonCharacter

}
