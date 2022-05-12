package com.udacity.asteroidradar.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.repositories.MainRepository
import retrofit2.HttpException

class DeleteDataWorker(appContext: Context, params: WorkerParameters) :
    CoroutineWorker(appContext, params) {

    companion object {
        const val WORK_NAME = "DeleteDataWorker"
    }

    override suspend fun doWork(): Result {
        val database = getDatabase(applicationContext)
        val repository = MainRepository(database)

        return try {
            repository.deletePrevDayAsteroid()
            Result.success()
        } catch (e: HttpException) {
            Result.retry()
        }
    }
}