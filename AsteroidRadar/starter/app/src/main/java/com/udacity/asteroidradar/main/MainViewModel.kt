package com.udacity.asteroidradar.main

import android.app.Application
import androidx.lifecycle.*
import com.udacity.asteroidradar.database.AsteroidDao_Impl
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.models.Asteroid
import com.udacity.asteroidradar.models.AsteroidEntity
import com.udacity.asteroidradar.models.PictureOfDay
import com.udacity.asteroidradar.repositories.MainRepository
import kotlinx.coroutines.launch
import java.time.LocalDate

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val dataBase = getDatabase(application)
    private val repository = MainRepository(dataBase)

    private var _asteroidList = repository.asteroidList.asLiveData()
    val asteroidList get() = _asteroidList

    private var _pictureOfDay = repository.pictureOfDay.asLiveData()
    val pictureOfDay: LiveData<PictureOfDay?>
        get() = _pictureOfDay

    init {
        viewModelScope.launch {
            repository.updateAsteroid(
                LocalDate.now().toString(),
                LocalDate.now().plusDays(7).toString()
            )
        }
    }

}