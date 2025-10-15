package com.example.ricknmortycharacters.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class CharactersViewModel : ViewModel() {

    // Список планет для LazyGrid
    val characters = mutableStateListOf<String>()

    init {
        loadCharacters()
    }

    private fun loadCharacters() {
        // Имитируем загрузку
        viewModelScope.launch {
            val dummyList = List(20) { "Planet $it" }
            characters.addAll(dummyList)
        }
    }

    fun fetch(page: Int = 1, filter: String? = null, name: String? = null) {
        viewModelScope.launch {
            try {
                Log.d("CharactersViewModel", "Fetching characters with name: $name and a page $page with $filter")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}