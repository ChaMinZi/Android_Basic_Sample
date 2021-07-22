package com.example.android.devbyteviewer.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface VideoDao {
    @Query("select * from databasevideo")
    fun getVideos(): List<DatabaseVideo>

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