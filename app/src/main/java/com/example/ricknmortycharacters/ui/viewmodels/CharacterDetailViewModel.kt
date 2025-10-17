package com.example.ricknmortycharacters.ui.viewmodels

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import androidx.lifecycle.viewModelScope
import com.example.ricknmortycharacters.data.db.CartoonCharacter
import com.example.ricknmortycharacters.data.api.RetrofitClient
import com.example.ricknmortycharacters.data.db.CharacterDao
import com.example.ricknmortycharacters.domain.NetworkUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CharacterDetailViewModel @Inject constructor(
    private val dao: CharacterDao,
    private val networkutil: NetworkUtils
) : ViewModel() {

    private val api = RetrofitClient.api

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

            val character = if (networkutil.isOnline()) {
                api.getCharacter(id).also { dao.insertCharacter(it) }
            } else {
                dao.getCharacterById(id)
            }
            _character.value = character
        } catch (e: Exception) {
            _error.value = e.localizedMessage
            // fallback to Room
            dao.getCharacterById(id)?.let { _character.value = it }
        } finally {
            _isLoading.value = false
        }
    }
}