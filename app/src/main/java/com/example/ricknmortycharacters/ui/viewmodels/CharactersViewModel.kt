package com.example.ricknmortycharacters.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ricknmortycharacters.data.api.CartoonCharacter
import com.example.ricknmortycharacters.data.api.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CharactersViewModel : ViewModel() {

    // Список планет для LazyGrid
    private val _characters = MutableStateFlow<List<CartoonCharacter>>(emptyList())
    val characters: StateFlow<List<CartoonCharacter>> get() = _characters

    private val _currentPage = MutableStateFlow(1)
    val currentPage: StateFlow<Int> get() = _currentPage

    private val _totalPages = MutableStateFlow(1)
    val totalPages: StateFlow<Int> get() = _totalPages

    private val _nextPageUrl = MutableStateFlow<String?>(null)
    val nextPageUrl: StateFlow<String?> get() = _nextPageUrl

    private val _prevPageUrl = MutableStateFlow<String?>(null)
    val prevPageUrl: StateFlow<String?> get() = _prevPageUrl

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading

    private val api = RetrofitClient.api

    fun fetch(
        page: Int? = null,
        name: String? = null,
        status: String? = null,
        species: String? = null,
        type: String? = null,
        gender: String? = null,
        filter: String? = null
    ) {
        val pageToFetch = page ?: _currentPage.value

        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = api.getCharacters(
                    page = pageToFetch,
                    name = name,
                    status = if (filter == "status") "Alive" else status,
                    species = if (filter == "species") "Human" else species,
                    gender = if (filter == "gender") "Male" else gender
                )
                _characters.value = response.results
                _currentPage.value = pageToFetch
            } catch (e: Exception) {
                e.printStackTrace()
                _characters.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun nextPage() {
        val next = _nextPageUrl.value
        if (next != null) {
            _currentPage.update { it + 1 }
            fetch(_currentPage.value)
        }
    }

    fun previousPage() {
        val prev = _prevPageUrl.value
        if (prev != null) {
            _currentPage.update { it - 1 }
            fetch(_currentPage.value)
        }
    }
}