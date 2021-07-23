package com.example.android.devbyteviewer.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface VideoDao {
    @Query("select * from databasevideo")
    fun getVideos(): LiveData<List<DatabaseVideo>>

    /**
     * @[vararg] = variable arguments
     * it's how a function can take an unknown number of arguments in Kotlin.
     * (Kotlin에서 함수가 알 수 없는 수의 인수를 취할 수 있는 방법)
     * 여기서 실제로는 array를 전달할 것입니다.

     * @[onConflict]
     * 동일한 PrimaryKey가 Database에 있는 경우, exception이 발생하기 때문에
     * OnConflictStrategy 를 설정해줌
    **/
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg videos: DatabaseVideo)
}

@Database(entities = [DatabaseVideo::class], version = 1)
abstract class VideosDatabase: RoomDatabase() {
    abstract val videoDao: VideoDao
}

private lateinit var INSTANCE: VideosDatabase
fun getDatabase(context: Context): VideosDatabase {
    synchronized(VideosDatabase::class.java) {
        // lateinit 변수가 이미 할당되었는지 여부를 확인하는 if문
        if (!::INSTANCE.isInitialized) {
            // INSTANCE가 아직 초기화되지 않음
            INSTANCE = Room.databaseBuilder(
                context.applicationContext,
                VideosDatabase::class.java,
                "videos"
            ).build()
        }
    }
    return INSTANCE
}