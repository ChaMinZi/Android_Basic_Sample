package com.udacity.asteroidradar.repositories

import android.util.Log
import com.udacity.asteroidradar.api.Network
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.database.NasaDatabase
import com.udacity.asteroidradar.models.Asteroid
import com.udacity.asteroidradar.models.PictureOfDay
import com.udacity.asteroidradar.models.asDomainModel
import com.udacity.asteroidradar.utils.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import org.json.JSONObject

class MainRepository(private val database: NasaDatabase) {

    val asteroidList: Flow<List<Asteroid>>
        get() = database.asteroidDao.getAllAsteroid(Constants.getToday())

    suspend fun updateAsteroid(startDate: String, endDate: String) {
        withContext(Dispatchers.IO) {
            val asteroidResponse = Network.nasa.getAsteroid(startDate, endDate)
            if (asteroidResponse.isSuccessful) {
                val asteroidList =
                    parseAsteroidsJsonResult(JSONObject(asteroidResponse.body()!!.string()))
                database.asteroidDao.insertAsteroidList(*asteroidList.asDomainModel())
            }
        }
    }
}