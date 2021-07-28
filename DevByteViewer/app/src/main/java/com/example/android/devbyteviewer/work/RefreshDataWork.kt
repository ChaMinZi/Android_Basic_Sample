package com.example.android.devbyteviewer.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.android.devbyteviewer.database.getDatabase
import com.example.android.devbyteviewer.repository.VideosRepository
import retrofit2.HttpException

/**
 * WorkManager는 CoroutineWorker을 상속받을 경우, worker에 코루틴을 별다른 조치 없이 바로 사용하게 해줍니다.
 * Coroutine 을 사용하는 경우 doWork()는 suspend function을 사용합니다.
 *
 * 아래의 Worker는 결과 값(result)을 반환할 때까지 실행됩니다.
 *
 * doWork()는 백그라운드 스레드에서 동작하기 때문에 UI thread가 blocking 될 걱정을 하지 않아도 됩니다.
 */
class RefreshDataWork(appContext: Context, params: WorkerParameters) :
    CoroutineWorker(appContext, params) {

    companion object {
        const val WORK_NAME = "RefreshDataWorker"
    }

    override suspend fun doWork(): Result {
        val database = getDatabase(applicationContext)
        val repository = VideosRepository(database)

        return try {
            repository.refreshVideos()
            Result.success()
        } catch (e: HttpException) {
            // 폰이 네트워크에 연결할 수 없는 상황이거나 서버가 꺼진 경우
            Result.retry() // Failure과 달리 Retry는 work manager에게 나중에 이 작업을 다시 시도하도록 요청합니다.
        }
    }
}