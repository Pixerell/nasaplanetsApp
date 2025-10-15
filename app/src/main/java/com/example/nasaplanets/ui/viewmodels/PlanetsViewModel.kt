package com.example.nasaplanets.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class PlanetsViewModel : ViewModel() {

    // Список планет для LazyGrid
    val planets = mutableStateListOf<String>()

    init {
        loadPlanets()
    }

    private fun loadPlanets() {
        // Имитируем загрузку
        viewModelScope.launch {
            val dummyList = List(20) { "Planet $it" }
            planets.addAll(dummyList)
        }
    }
}