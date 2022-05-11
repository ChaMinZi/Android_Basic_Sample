package com.udacity.asteroidradar.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.udacity.asteroidradar.api.Network
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.database.NasaDatabase
import com.udacity.asteroidradar.models.Asteroid
import com.udacity.asteroidradar.models.PictureOfDay
import com.udacity.asteroidradar.models.asDomainModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import org.json.JSONObject

class MainRepository(private val database: NasaDatabase) {

    val pictureOfDay = flow {
        val pictureOfDayResponse = Network.nasa.getPictureOfDay()
        if (pictureOfDayResponse.isSuccessful) {
            emit(pictureOfDayResponse.body())
        }
    }

    val asteroidList: Flow<List<Asteroid>>
        get() = database.asteroidDao.getAllAsteroid()

    suspend fun updateAsteroid(startDate: String, endDate: String) {
        withContext(Dispatchers.IO) {
            val asteriodResponse = Network.nasa.getAsteriod(startDate, endDate)
            if (asteriodResponse.isSuccessful) {
                val asteriodList =
                    parseAsteroidsJsonResult(JSONObject(asteriodResponse.body()!!.string()))
                database.asteroidDao.insertAsteroidList(*asteriodList.asDomainModel())
            }
        }
    }
}