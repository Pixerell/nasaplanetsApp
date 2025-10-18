package com.example.ricknmortycharacters.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ricknmortycharacters.data.db.CartoonCharacter
import com.example.ricknmortycharacters.data.db.CharacterFilter
import com.example.ricknmortycharacters.data.db.CharactersRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CharactersViewModel(private val repository: CharactersRepository) : ViewModel() {

    private val _characters = MutableStateFlow<List<CartoonCharacter>>(emptyList())
    val characters: StateFlow<List<CartoonCharacter>> get() = _characters

    private val _currentPage = MutableStateFlow(1)
    val currentPage: StateFlow<Int> get() = _currentPage

    private val _totalPages = MutableStateFlow(1)
    val totalPages: StateFlow<Int> get() = _totalPages

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> get() = _searchQuery

    private val _activeStatusFilter = MutableStateFlow<String?>(null)
    val activeStatusFilter: StateFlow<String?> get() = _activeStatusFilter

    private val _activeGenderFilter = MutableStateFlow<String?>(null)
    val activeGenderFilter: StateFlow<String?> get() = _activeGenderFilter

    fun fetch(page: Int? = null) {
        val pageToFetch = page ?: _currentPage.value

        viewModelScope.launch {
            _isLoading.value = true
            try {
                val filter = CharacterFilter(
                    name = if (_searchQuery.value.isNotBlank()) _searchQuery.value else null,
                    status = _activeStatusFilter.value,
                    gender = _activeGenderFilter.value
                )

                val result = repository.fetchPage(pageToFetch, filter)
                _characters.value = result.characters
                _currentPage.value = pageToFetch
                result.info?.pages?.let { _totalPages.value = it }
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun searchCharacters(query: String) {
        _searchQuery.value = query
        viewModelScope.launch {
            delay(300) // Debounce
            if (query.length >= 2 || query.isEmpty()) {
                fetch(page = 1)
            }
        }
    }

    fun applyFilter(status: String?, gender: String?) {
        _activeStatusFilter.value = status
        _activeGenderFilter.value = gender
        fetch(page = 1)
    }

    fun clearSearchAndFilters() {
        _searchQuery.value = ""
        _activeStatusFilter.value = null
        _activeGenderFilter.value = null
        fetch(page = 1)
    }

    fun nextPage() {
        val next = _currentPage.value + 1
        if (next <= _totalPages.value) fetch(next)
    }

    fun previousPage() {
        val prev = _currentPage.value - 1
        if (prev >= 1) fetch(prev)
    }
}