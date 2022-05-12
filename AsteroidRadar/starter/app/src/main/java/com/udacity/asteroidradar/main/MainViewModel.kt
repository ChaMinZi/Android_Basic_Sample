package com.udacity.asteroidradar.main

import android.app.Application
import androidx.lifecycle.*
import com.udacity.asteroidradar.api.Network
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.models.Asteroid
import com.udacity.asteroidradar.models.PictureOfDay
import com.udacity.asteroidradar.repositories.MainRepository
import com.udacity.asteroidradar.utils.Constants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val database = getDatabase(application)
    private val repository = MainRepository(database)

    private var _asteroidList =
        repository.asteroidList.asLiveData() as MutableLiveData<List<Asteroid>>
    val asteroidList get() = _asteroidList

    private var _pictureOfDay = MutableLiveData<PictureOfDay?>()
    val pictureOfDay: LiveData<PictureOfDay?>
        get() = _pictureOfDay

    init {
        viewModelScope.launch {
            repository.updateAsteroid(
                Constants.getToday(),
                Constants.getAfter7Days()
            )
            getPictureOfDay()
        }
    }

    private suspend fun getPictureOfDay() {
        try {
            val response = Network.nasa.getPictureOfDay()
            if (response.isSuccessful)
                _pictureOfDay.postValue(response.body())
            else
                _pictureOfDay.postValue(null)
        } catch (e: Exception) {
            _pictureOfDay.postValue(null)
        }
    }

    fun getWeekAsteroid() {
        viewModelScope.launch {
            repository.getWeekAsteroid().collectLatest {
                _asteroidList.postValue(it)
            }
        }
    }

    fun getTodayAsteroid() {
        viewModelScope.launch {
            repository.getTodayAsteroid().collectLatest {
                _asteroidList.postValue(it)
            }
        }
    }

    fun getSavedAsteroid() {
        viewModelScope.launch {
            repository.getSavedAsteroid().collectLatest {
                _asteroidList.postValue(it)
            }
        }
    }
}