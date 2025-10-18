package com.example.ricknmortycharacters.domain

import android.util.Log
import com.example.ricknmortycharacters.data.api.RickAndMortyApi
import com.example.ricknmortycharacters.data.db.CartoonCharacter
import com.example.ricknmortycharacters.data.db.CharacterDao
import com.example.ricknmortycharacters.data.db.CharacterFilter
import com.example.ricknmortycharacters.data.db.Info
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

data class PageResult(
    val characters: List<CartoonCharacter>,
    val info: Info? = null
)

class CharactersRepository(
    private val api: RickAndMortyApi,
    private val dao: CharacterDao
) {
    suspend fun fetchPage(
        page: Int,
        filter: CharacterFilter = CharacterFilter()
    ): PageResult = withContext(Dispatchers.IO) {
        try {
            val response = api.getCharacters(
                page = page,
                name = filter.name,
                status = filter.status,
                gender = filter.gender,
                species = filter.species,
            )

            val entitiesWithPage = response.results.map { it.copy(page = page) }
            dao.insertAll(entitiesWithPage)
            PageResult(characters = entitiesWithPage, info = response.info)
        } catch (e: Exception) {
            Log.w("Repo", "fetchPage failed for page=$page, name=${filter.name}, status=${filter.status}, gender=${filter.gender}, species=${filter.species}б ${e.message}")
            val allFilteredCharacters = dao.filter(
                name = filter.name,
                status = filter.status,
                species = filter.species,
                gender = filter.gender
            )

            // Calculate pagination
            val pageSize = 20
            val totalCount = allFilteredCharacters.size
            val totalPages = if (totalCount == 0) 0 else (totalCount + pageSize - 1) / pageSize

            // Get characters for current page
            val startIndex = (page - 1) * pageSize
            val endIndex = minOf(startIndex + pageSize, totalCount)
            val cached = if (startIndex < totalCount) {
                allFilteredCharacters.slice(startIndex until endIndex)
            } else {
                emptyList()
            }

            val fallbackInfo = Info(
                count = totalCount,
                pages = totalPages,
                next = if (page < totalPages) (page + 1).toString() else null,
                prev = if (page > 1) (page - 1).toString() else null
            )

            PageResult(characters = cached, info = fallbackInfo)
        }
    }
}