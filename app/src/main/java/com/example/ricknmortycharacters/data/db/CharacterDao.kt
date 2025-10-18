package com.example.ricknmortycharacters.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CharacterDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(characters: List<CartoonCharacter>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCharacter(character: CartoonCharacter)

    @Query("SELECT * FROM characters WHERE id = :id LIMIT 1")
    suspend fun getCharacterById(id: Int): CartoonCharacter?

    @Query("SELECT * FROM characters WHERE page = :page ORDER BY id")
    suspend fun getCharactersByPage(page: Int): List<CartoonCharacter>

    @Query(
        """
    SELECT * FROM characters
    WHERE (:name IS NULL OR LOWER(name) LIKE '%' || LOWER(:name) || '%')
      AND (:status IS NULL OR LOWER(status) = LOWER(:status))
      AND (:species IS NULL OR LOWER(species) = LOWER(:species))
      AND (:gender IS NULL OR LOWER(gender) = LOWER(:gender))
    ORDER BY id
"""
    )
    suspend fun filter(
        name: String?,
        status: String?,
        species: String?,
        gender: String?,
    ): List<CartoonCharacter>
}

