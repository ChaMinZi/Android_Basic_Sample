package com.udacity.asteroidradar.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.repositories.MainRepository
import com.udacity.asteroidradar.utils.Constants
import retrofit2.HttpException

class RefreshDataWorker(appContext: Context, params: WorkerParameters) :
    CoroutineWorker(appContext, params) {

    companion object {
        const val WORK_NAME = "RefreshDataWorker"
    }

    override suspend fun doWork(): Result {
        val database = getDatabase(applicationContext)
        val repository = MainRepository(database)

        return try {
            repository.updateAsteroid(
                Constants.getToday(),
                Constants.getAfter7Days()
            )
            Result.success()
        } catch (e: HttpException) {
            Result.retry()
        }
    }
}