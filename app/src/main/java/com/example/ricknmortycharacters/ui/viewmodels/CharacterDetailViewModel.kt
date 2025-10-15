package com.example.ricknmortycharacters.ui.viewmodels

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class CharacterDetailViewModel : ViewModel() {
    private val _characterName = MutableStateFlow("")
    val characterName: StateFlow<String> = _characterName

    fun setCharacter(name: String) {
        _characterName.value = name
    }
}
