package com.example.nasaplanets.ui.viewmodels

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class PlanetDetailViewModel : ViewModel() {
    private val _planetName = MutableStateFlow("")
    val planetName: StateFlow<String> = _planetName

    fun setPlanet(name: String) {
        _planetName.value = name
    }
}
