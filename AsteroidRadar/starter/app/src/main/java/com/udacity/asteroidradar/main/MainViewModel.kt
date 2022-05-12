package com.udacity.asteroidradar.main

import android.app.Application
import androidx.lifecycle.*
import com.udacity.asteroidradar.api.Network
import com.udacity.asteroidradar.database.AsteroidDao_Impl
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.models.Asteroid
import com.udacity.asteroidradar.models.AsteroidEntity
import com.udacity.asteroidradar.models.PictureOfDay
import com.udacity.asteroidradar.repositories.MainRepository
import com.udacity.asteroidradar.utils.Constants
import kotlinx.coroutines.launch
import java.time.LocalDate

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val dataBase = getDatabase(application)
    private val repository = MainRepository(dataBase)

    private var _asteroidList = repository.asteroidList.asLiveData()
    val asteroidList get() = _asteroidList

    private var _pictureOfDay = MutableLiveData<PictureOfDay?>()
    val pictureOfDay: LiveData<PictureOfDay?>
        get() = _pictureOfDay

    init {
        viewModelScope.launch {
//            repository.updateAsteroid(
//                Constants.getToday(),
//                Constants.getAfter7Days()
//            )
            getPictureOfDay()
        }
    }

    private suspend fun getPictureOfDay() {
        try {
            val response = Network.nasa.getPictureOfDay()
            _pictureOfDay.postValue(response)
        } catch (e: Exception) {
            _pictureOfDay.postValue(null)
        }
    }
}