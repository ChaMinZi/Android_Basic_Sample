package com.example.android.devbyteviewer.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.android.devbyteviewer.database.getDatabase
import com.example.android.devbyteviewer.repository.VideosRepository
import kotlinx.coroutines.launch

/**
 * DevByteViewModel designed to store and manage UI-related data in a lifecycle conscious way. This
 * allows data to survive configuration changes such as screen rotations. In addition, background
 * work such as fetching network results can continue through configuration changes and deliver
 * results after the new Fragment or Activity is available.
 * ( DevByteViewModel은 UI와 관련된 데이터를 라이프사이클을 의식하여 저장하고 관리하도록 설계되었습니다.
 *   이렇게하면 화면 회전과 같은 구성 변경(Configuration changes) 후에도 데이터를 유지할 수 있응 장점이 있습니다.
 *   또한, 네트워크 결과 가져오기와 같은 백그라운드 작업도 구성 변경이 일어나도 동작하며,
 *   새로운 Fragment, Activity를 사용할 수 있게되면 결과를 전달할 수 있습니다. )
 *
 * @param application The application that this viewmodel is attached to, it's safe to hold a
 * reference to applications across rotation since Application is never recreated during actiivty
 * or fragment lifecycle events.
 * ( application이 연결된 ViewModel은 Activity나 Fragment의 Lifecycle Event가 발생해도 절대 다시 생성되지 않기 때문에,
 *   applicatino에 대한 참조를 유지하는 것이 안전합니다.
 */
class DevByteViewModel(application: Application) : AndroidViewModel(application) {

    private val database = getDatabase(application)
    private val videosRepository = VideosRepository(database)

    init {
        // refreshVideos()가 suspend function 이라서 코루틴을 이용해야 합니다.
        viewModelScope.launch {
            videosRepository.refreshVideos()
        }
    }

    // getVideos in repository
    val playlist = videosRepository.videos

    /**
     * Factory for constructing DevByteViewModel with parameter
     */
    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(DevByteViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return DevByteViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}
