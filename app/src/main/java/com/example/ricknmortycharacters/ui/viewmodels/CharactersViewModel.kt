package com.example.ricknmortycharacters.ui.viewmodels


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ricknmortycharacters.data.db.CartoonCharacter
import com.example.ricknmortycharacters.data.db.CharacterFilter
import com.example.ricknmortycharacters.domain.CharactersRepository
import com.example.ricknmortycharacters.domain.NetworkUtils
import com.example.ricknmortycharacters.domain.PageResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CharactersViewModel @Inject constructor(
    private val repository: CharactersRepository,
    private val networkUtils: NetworkUtils
) : ViewModel() {

    private val _characters = MutableStateFlow<List<CartoonCharacter>>(emptyList())
    val characters: StateFlow<List<CartoonCharacter>> = _characters

    private val _currentPage = MutableStateFlow(1)
    val currentPage: StateFlow<Int> = _currentPage

    private val _totalPages = MutableStateFlow(1)
    val totalPages: StateFlow<Int> = _totalPages

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _activeStatusFilter = MutableStateFlow<String?>(null)
    val activeStatusFilter: StateFlow<String?> = _activeStatusFilter

    private val _activeGenderFilter = MutableStateFlow<String?>(null)
    val activeGenderFilter: StateFlow<String?> = _activeGenderFilter

    private val _activeSpeciesFilter = MutableStateFlow<String?>(null)
    val activeSpeciesFilter: StateFlow<String?> = _activeSpeciesFilter

    fun applyFilter(
        status: String?,
        gender: String?,
        species: String? = null
    ) {
        _activeStatusFilter.value = status
        _activeGenderFilter.value = gender
        _activeSpeciesFilter.value = species
        fetch(page = 1)
    }

    fun clearSearchAndFilters() {
        _searchQuery.value = ""
        _activeStatusFilter.value = null
        _activeGenderFilter.value = null
        _activeSpeciesFilter.value = null
        fetch(page = 1)
    }

    fun searchCharacters(query: String) {
        _searchQuery.value = query
        viewModelScope.launch {
            delay(300) // Debounce
            fetch(page = 1)
        }
    }

    fun fetch(page: Int? = null) {
        val pageToFetch = page ?: _currentPage.value

        viewModelScope.launch {
            _isLoading.value = true
            try {
                val filter = createFilter()
                val result = repository.fetchPage(pageToFetch, filter)
                updateState(result, pageToFetch)
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

    private fun createFilter(): CharacterFilter {
        return CharacterFilter(
            name = _searchQuery.value.takeIf { it.isNotBlank() },
            status = _activeStatusFilter.value,
            gender = _activeGenderFilter.value,
            species = _activeSpeciesFilter.value
        )
    }

    private fun updateState(result: PageResult, page: Int) {
        _characters.value = result.characters
        _currentPage.value = page
        result.info?.pages?.let { _totalPages.value = it }
    }

    fun isOnline(): Boolean {
        return networkUtils.isOnline()
    }
}