package com.udacity.asteroidradar.repositories

import android.util.Log
import com.udacity.asteroidradar.api.Network
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.database.NasaDatabase
import com.udacity.asteroidradar.models.Asteroid
import com.udacity.asteroidradar.models.asDomainModel
import com.udacity.asteroidradar.utils.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import org.json.JSONObject

class MainRepository(private val database: NasaDatabase) {

    val asteroidList: Flow<List<Asteroid>>
        get() = database.asteroidDao.getAsteroidList(
            Constants.getToday(),
            Constants.getAfter7Days()
        ).map { list ->
            list.sortedBy { it.closeApproachDate }
        }

    suspend fun updateAsteroid(startDate: String, endDate: String) {
        withContext(Dispatchers.IO) {
            try {
                val asteroidResponse = Network.nasa.getAsteroid(startDate, endDate)
                if (asteroidResponse.isSuccessful) {
                    val asteroidList =
                        parseAsteroidsJsonResult(JSONObject(asteroidResponse.body()!!.string()))
                    database.asteroidDao.insertAsteroidList(*asteroidList.asDomainModel())
                } else {
                    Log.i("MainRepository : ", asteroidResponse.message())
                }
            } catch (e: Exception) {
                Log.i("MainRepository : ", e.message ?: "")
            }
        }
    }

    fun getWeekAsteroid(): Flow<List<Asteroid>> {
        return database.asteroidDao.getAsteroidList(Constants.getToday(), Constants.getAfter7Days())
    }

    fun getTodayAsteroid(): Flow<List<Asteroid>> {
        return database.asteroidDao.getAsteroidList(Constants.getToday(), Constants.getToday())
    }

    fun getSavedAsteroid(): Flow<List<Asteroid>> {
        return database.asteroidDao.getSavedAsteroid()
    }

    suspend fun deletePrevDayAsteroid() {
        withContext(Dispatchers.IO) {
            database.asteroidDao.deletePrevDayAsteroid(Constants.getToday())
        }
    }
}