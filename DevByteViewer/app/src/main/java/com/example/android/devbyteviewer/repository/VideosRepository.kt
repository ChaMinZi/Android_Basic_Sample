package com.example.android.devbyteviewer.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.android.devbyteviewer.database.VideosDatabase
import com.example.android.devbyteviewer.database.asDomainModel
import com.example.android.devbyteviewer.domain.Video
import com.example.android.devbyteviewer.network.Network
import com.example.android.devbyteviewer.network.asDatabaseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Repository for fetching devbyte videos from the network and storing them on disk.
 *
 * Database object를 생성자 파라미터로 받음으로서,
 * 이 repository에서 (잠재적으로 leaks을 유발할 수 있는) Android Context에 대한 참조를 유지할 필요가 없어졌습니다.
 * --> Dependency injection
 *
 * 이 앱의 경우 매우 간단한 새로 고침 전략을 이용합니다.
 * 실제 앱에서도 새로고침 전략(refresh strategy)은 이 앱과 같이 요청 시 모든 내용을 다시 로드하여 업데이트하는 것과 같이 간단할 수 있습니다.
 * 또는 자동으로 데이터를 새로 고쳐서 대역폭 사용을 최소화하거나, 사용자가 다음에 사용할 가능성이 높은 데이터를 캐시하는 등
 * 매우 복잡할 수도 있습니다.
 */
class VideosRepository(private val database: VideosDatabase) {

    // 1. 오프라인 캐시로부터 Video load
    /**
     * A playlist of videos that can be shown on the screen.
     *
     * 다른 object(Activity, Fragment 등)가 이 프로퍼티를 통해서 videos를 관찰함
     * Database는 databaseVideo를 반환하기 때문에 Transformations를 이용하여 Video로 변환
     * ( Transformations는 activity나 fragment가 값을 호출할 때만 동작하기 때문에 ) videos를 안전한 프로퍼티로 선언할 수 있습니다.
     **/
    val videos: LiveData<List<Video>> =
        Transformations.map(database.videoDao.getVideos()) {
            it.asDomainModel()
        }

    // 2. 오프라인 캐시 refresh
    /**
     * 코루틴에서 호출할 것이므로 suspend로 선언
     *
     * Android의 데이터베이스는 파일 시스템이나 디스크에 저장됩니다.
     * 데이터를 저장하기 위해서는 파일 권한을 수행해야 합니다?
     * 디스크에서 읽고 쓰는 것(Disk I/O)은 RAM에 읽고 쓰는 것보다 매우 느립니다.
     * 또한 데이터베이스가 사용하는 낮은 레벨의 API는 읽기 또는 쓰기 작업이 완료될 때까지 항상 스레드를 Blocking 합니다.
     * 이 때문에 코루틴을 사용할 때 디스크 I/O를 별도로 처리해야 합니다. 디스크 I/O는 I/O 디스패처에서 실행되는 것이 중요합니다.
     *
     * @withContext(Dispatchers.IO)
     * : 데이터베이스 작업과 같이 디스크에 읽고 쓰는 작업을 실행하기 위해 특별히 조정된 디스패처입니다.
     * WithContext는 Kotlin 코루틴이 지정된 디스패처로 전환되도록 합니다.
     * 아래와 같이 선언함으로서 refreshVideos()는 아무 Dispatcher에서나 (심지어는 main Thread에서도) 안전하게 호출할 수 있습니다.
     * withContext는 데이터베이스 작업을 안전하게 실행하기 위해 I/O 스레드를 찾는 일을 처리합니다. 우리는 모든 데이터베이스 호출을 withContext 블록 안에 넣어야 합니다.
     *
     * @await()
     * : 코루틴이 사용할 수 있을 때까지 일시 중단하도록 지시하는 await 함수입니다.
     * 따라서 이것이 실행되면 getPlaylis의 결과를 항상 사용할 수 있지만, 결과를 기다리는 동안 이 스레드가 차단되지는 않습니다.
     * 이는 await가 suspend function이기 때문입니다.
     *
     * [val playlist = Network.devbytes.getPlaylist().await()] 이 코드는 스레드나 Disk I/O를 차단하지 않기 때문에 @withContext 외부에 둘 수도 있습니다.
     * 그러나 아래와 같이 코드를 작성하느 것이 좀 더 좋게 보이고
     * I/O 디스패처(dispatcher)에서 네트워크 요청(requests)를 시작해도 아무런 문제가 없기 때문에 아래와 같이 작성합니다.
     *
     * [database.videoDao.insertAll(*playlist.asDatabaseModel())]
     * : SOC(관심사 분리)를 유지하기 위해서, asDatabaseModel을 사용하여 network results를 database objects로 매핑해야 합니다.
     **/
    suspend fun refreshVideos() {
        withContext(Dispatchers.IO) {
            val playlist = Network.devbytes.getPlaylist().await()
            database.videoDao.insertAll(*playlist.asDatabaseModel())
        }
    }
}