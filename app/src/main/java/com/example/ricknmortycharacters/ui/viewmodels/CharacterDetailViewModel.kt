package com.example.ricknmortycharacters.ui.viewmodels

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import androidx.lifecycle.viewModelScope
import com.example.ricknmortycharacters.data.db.CartoonCharacter
import com.example.ricknmortycharacters.data.api.RickAndMortyApi
import com.example.ricknmortycharacters.data.db.CharacterDao
import com.example.ricknmortycharacters.domain.NetworkUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CharacterDetailViewModel @Inject constructor(
    private val dao: CharacterDao,
    private val networkUtils: NetworkUtils,
    private val api: RickAndMortyApi
) : ViewModel() {

    private val _character = MutableStateFlow<CartoonCharacter?>(null)
    val character: StateFlow<CartoonCharacter?> = _character

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun fetchById(id: Int) = viewModelScope.launch {
        _isLoading.value = true
        _error.value = null

        try {
            val character = if (networkUtils.isOnline()) {
                fetchFromApi(id)
            } else {
                fetchFromDatabase(id)
            }
            _character.value = character
        } catch (e: Exception) {
            handleError(e, id)
        } finally {
            _isLoading.value = false
        }
    }

    private suspend fun fetchFromApi(id: Int): CartoonCharacter {
        return api.getCharacter(id).also { dao.insertCharacter(it) }
    }

    private suspend fun fetchFromDatabase(id: Int): CartoonCharacter? {
        return dao.getCharacterById(id)
    }

    private suspend fun handleError(e: Exception, id: Int) {
        _error.value = e.localizedMessage ?: "Unknown error occurred"
        // Fallback to database
        fetchFromDatabase(id)?.let { _character.value = it }
    }
}