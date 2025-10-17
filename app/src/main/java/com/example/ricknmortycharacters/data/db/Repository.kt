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
        filter: String? = null,
        name: String? = null
    ): PageResult = withContext(Dispatchers.IO) {
        try {
            val response = api.getCharacters(
                page = page,
                name = name,
                status = filter
            )
            val entitiesWithPage = response.results.map { it.copy(page = page) }

            Log.d("Repo", "Fetched page=$page, ids=${entitiesWithPage.map { it.id }}")

            dao.insertAll(entitiesWithPage)

            // debug
            val count = dao.countByPage(page)
            Log.d("Repo", "After insert: DB count for page=$page => $count")
            val rows = dao.listIdAndPage()
            Log.d("Repo", "DB rows after insert (id,page,name): ${rows.joinToString { "${it.id},${it.page},${it.name}" }}")

            PageResult(characters = entitiesWithPage, info = response.info)
        } catch (e: Exception) {
            Log.w("Repo", "fetchPage failed for page=$page: ${e.message}")

            // return cached page characters
            val cached = dao.getCharactersByPage(page)
            Log.d("Repo", "Returned cached for page=$page, cachedIds=${cached.map { it.id }}")

            val maxPage = dao.maxPage() ?: 0
            val totalCount = dao.getAllCharactersFlow() // can't call Flow here for immediate value; use countByPage across pages
            val pagesList = dao.distinctPages()
            val pages = if (pagesList.isEmpty()) 0 else pagesList.maxOrNull() ?: pagesList.size

            val fallbackInfo = if (pages > 0) {
                Info(
                    count = pagesList.sumOf { dao.countByPage(it) }, // optional accurate count
                    pages = pages,
                    next = null,
                    prev = if (page > 1) (page - 1).toString() else null
                )
            } else null

            PageResult(characters = cached, info = fallbackInfo)
        }
    }

    // ... other methods unchanged
}
