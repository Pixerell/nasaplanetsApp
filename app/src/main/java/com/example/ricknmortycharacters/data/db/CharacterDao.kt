package com.example.ricknmortycharacters.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

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

    @Query("SELECT * FROM characters ORDER BY id")
    fun getAllCharactersFlow(): Flow<List<CartoonCharacter>>

    @Query("SELECT COUNT(*) FROM characters WHERE page = :page")
    suspend fun countByPage(page: Int): Int

    @Query("SELECT MAX(page) FROM characters")
    suspend fun maxPage(): Int? // null when no rows

    @Query("SELECT DISTINCT page FROM characters ORDER BY page")
    suspend fun distinctPages(): List<Int>

    @Query("SELECT id, page, name FROM characters ORDER BY page, id")
    suspend fun listIdAndPage(): List<IdPageRow>


    @Query("SELECT * FROM characters WHERE LOWER(name) LIKE '%' || LOWER(:query) || '%' ORDER BY id")
    suspend fun searchByName(query: String): List<CartoonCharacter>

    // Search with pagination support
    @Query("SELECT * FROM characters WHERE LOWER(name) LIKE '%' || LOWER(:query) || '%' AND page = :page ORDER BY id")
    suspend fun searchByNameWithPage(query: String, page: Int): List<CartoonCharacter>

    // Count search results by page
    @Query("SELECT COUNT(*) FROM characters WHERE LOWER(name) LIKE '%' || LOWER(:query) || '%' AND page = :page")
    suspend fun countSearchByPage(query: String, page: Int): Int

    @Query("""
        SELECT * FROM characters
        WHERE (:name IS NULL OR LOWER(name) LIKE LOWER(:name))
          AND (:status IS NULL OR LOWER(status) = LOWER(:status))
          AND (:species IS NULL OR LOWER(species) = LOWER(:species))
          AND (:gender IS NULL OR LOWER(gender) = LOWER(:gender))
          AND (:type IS NULL OR LOWER(type) = LOWER(:type))
        ORDER BY id
    """)


    suspend fun filter(
        name: String?,
        status: String?,
        species: String?,
        gender: String?,
        type: String?
    ): List<CartoonCharacter>
}

data class IdPageRow(
    val id: Int,
    val page: Int,
    val name: String
)

