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
}

data class IdPageRow(
    val id: Int,
    val page: Int,
    val name: String
)

