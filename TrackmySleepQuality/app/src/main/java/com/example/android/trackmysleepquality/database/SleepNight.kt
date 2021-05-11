package com.example.android.trackmysleepquality.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "daily_sleep_quality_table")
data class SleepNight(
        @PrimaryKey(autoGenerate = true)
        var nightId: Long = 0L,

        @ColumnInfo(name = "start_time_milli")
        val startTimeMilli: Long = System.currentTimeMillis(),

        // 시작 시간과 항상 다른 종료 시간을 기록하기 위해 정지 버튼을 눌렀는지 여부를 쉽게 알 수 있습니다.
        @ColumnInfo(name = "end_time_milli")
        var endTimeMilli: Long = startTimeMilli,

        // -1로 설정하여 아직 기록되지 않음을 기록
        @ColumnInfo(name = "quality_rating")
        var sleepQuality: Int = -1
)
