package com.example.ricknmortycharacters.data.db

import android.util.Log
import com.example.ricknmortycharacters.data.api.RickAndMortyApi
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
                gender = filter.gender
            )

            val entitiesWithPage = response.results.map { it.copy(page = page) }

            Log.d("Repo", "Fetched page=$page, name=${filter.name}, status=${filter.status}, gender=${filter.gender}, ids=${entitiesWithPage.map { it.id }}")

            dao.insertAll(entitiesWithPage)

            PageResult(characters = entitiesWithPage, info = response.info)
        } catch (e: Exception) {
            Log.w("Repo", "fetchPage failed for page=$page, name=${filter.name}, status=${filter.status}, gender=${filter.gender}: ${e.message}")

            // Get ALL characters matching the filter
            val allFilteredCharacters = dao.filter(
                name = filter.name,
                status = filter.status,
                species = null,
                gender = filter.gender,
                type = null
            )

            // Calculate pagination manually
            val pageSize = 20
            val totalCount = allFilteredCharacters.size
            val totalPages = if (totalCount == 0) 0 else (totalCount + pageSize - 1) / pageSize

            // Get characters for current page
            val startIndex = (page - 1) * pageSize
            val endIndex = minOf(startIndex + pageSize, totalCount)
            val cached = if (startIndex < totalCount) {
                allFilteredCharacters
                    .sortedBy { it.id } // Ensure consistent ordering
                    .slice(startIndex until endIndex)
            } else {
                emptyList()
            }

            Log.d("Repo", "Returned cached: page=$page, name=${filter.name}, status=${filter.status}, gender=${filter.gender}, count=${cached.size}, total=$totalCount, totalPages=$totalPages")

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