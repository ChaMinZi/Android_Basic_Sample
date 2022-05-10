package com.udacity.asteroidradar.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.repositories.MainRepository
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val repository = MainRepository()

    init {
        viewModelScope.launch {
            repository.getAsteriod("2022-05-10", "2022-05-17")
        }
    }
}