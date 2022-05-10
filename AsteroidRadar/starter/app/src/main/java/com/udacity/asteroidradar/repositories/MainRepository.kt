package com.udacity.asteroidradar.repositories

import android.util.Log
import com.udacity.asteroidradar.api.Network
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.models.Asteroid
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

class MainRepository() {
    suspend fun getAsteriod(startDate: String, endDate: String) {
        withContext(Dispatchers.IO) {
            val asteriodResponse = Network.nasa.getAsteriod(startDate, endDate)
            if (asteriodResponse.isSuccessful) {
                val asteriodList = parseAsteroidsJsonResult(JSONObject(asteriodResponse.body()!!.string()))

            }
        }
    }
}