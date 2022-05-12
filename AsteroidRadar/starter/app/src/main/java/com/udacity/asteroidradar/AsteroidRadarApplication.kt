package com.udacity.asteroidradar

import android.app.Application
import android.os.Build
import androidx.work.*
import com.udacity.asteroidradar.worker.DeleteDataWorker
import com.udacity.asteroidradar.worker.RefreshDataWorker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class AsteroidRadarApplication : Application() {
    private val applicationScope = CoroutineScope(Dispatchers.Default)

    override fun onCreate() {
        super.onCreate()
        delayInit()
    }

    private fun delayInit() = applicationScope.launch {
        setupRecurringWork()
    }

    private fun setupRecurringWork() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.UNMETERED)
            .setRequiresCharging(true)
            .apply {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    setRequiresDeviceIdle(true)
                }
            }
            .build()

        refreshWork(constraints)
        deleteWork(constraints)
    }

    private fun refreshWork(constraints: Constraints) {
        val repeatingRefreshRequest = PeriodicWorkRequestBuilder<RefreshDataWorker>(
            1,
            TimeUnit.DAYS
        ).setConstraints(constraints).build()

        WorkManager.getInstance().enqueueUniquePeriodicWork(
            RefreshDataWorker.WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            repeatingRefreshRequest
        )
    }

    private fun deleteWork(constraints: Constraints) {
        val repeatingDeleteRequest = PeriodicWorkRequestBuilder<RefreshDataWorker>(
            1,
            TimeUnit.DAYS
        ).setConstraints(constraints).build()

        WorkManager.getInstance().enqueueUniquePeriodicWork(
            DeleteDataWorker.WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            repeatingDeleteRequest
        )
    }
}