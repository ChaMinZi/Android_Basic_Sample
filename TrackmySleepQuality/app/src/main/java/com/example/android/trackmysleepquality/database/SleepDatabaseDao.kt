/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.trackmysleepquality.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface SleepDatabaseDao {
    @Insert
    suspend fun insert(night: SleepNight)
    // 컴파일시간 동안 룸은 코드를 발생시킵니다. Object를 데이터베이스에 row 값으로 삽입할 코드를

    @Update
    suspend fun update(night: SleepNight)

    @Query("SELECT * from daily_sleep_quality_table WHERE nightId = :key")
    suspend fun get(key: Long): SleepNight?

    @Query("DELETE FROM daily_sleep_quality_table")
    suspend fun clear()

    /**
     * 내림차순으로 정렬된 데이터 중에 첫 번째 1개의 값만 리턴해라
     * 리턴값은 nullable하다. 왜냐하면 처음이나 모든 content를 지우고 난 후에는
     * 해당 Table에 저장된 값이 없을 것이기 때문에 null도 반환 가능해야 합니다.
    **/
    @Query("SELECT * FROM daily_sleep_quality_table ORDER BY nightId DESC LIMIT 1")
    suspend fun getTonight(): SleepNight?
}
