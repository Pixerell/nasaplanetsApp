package com.example.ricknmortycharacters.ui.viewmodels

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import androidx.lifecycle.viewModelScope
import com.example.ricknmortycharacters.data.api.CartoonCharacter
import com.example.ricknmortycharacters.data.api.RetrofitClient
import kotlinx.coroutines.launch

class CharacterDetailViewModel : ViewModel() {
    private val api = RetrofitClient.api

    private val _character = MutableStateFlow<CartoonCharacter?>(null)
    val character: StateFlow<CartoonCharacter?> = _character

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun fetchById(id: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val result = api.getCharacter(id)
                _character.value = result
            } catch (e: Exception) {
                _error.value = e.message ?: "Unknown error"
                _character.value = null
            } finally {
                _isLoading.value = false
            }
        }
    }
}
