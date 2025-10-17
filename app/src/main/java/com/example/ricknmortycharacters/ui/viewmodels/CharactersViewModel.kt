package com.example.ricknmortycharacters.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ricknmortycharacters.data.db.CartoonCharacter
import com.example.ricknmortycharacters.data.db.CharactersRepository
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

    fun fetch(page: Int? = null, filter: String? = null, name: String? = null) {
        val pageToFetch = page ?: _currentPage.value

        viewModelScope.launch {
            _isLoading.value = true
            try {
                val result = repository.fetchPage(pageToFetch, filter, name)
                _characters.value = result.characters
                _currentPage.value = pageToFetch
                result.info?.pages?.let { _totalPages.value = it }
            } finally {
                _isLoading.value = false
            }
        }
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
