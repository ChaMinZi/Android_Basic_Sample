package com.example.android.trackmysleepquality

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import com.example.android.trackmysleepquality.database.SleepDatabase
import com.example.android.trackmysleepquality.database.SleepDatabaseDao
import com.example.android.trackmysleepquality.database.SleepNight
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

/**
 * This is not meant to be a full set of tests. For simplicity, most of your samples do not
 * include tests. However, when building the Room, it is helpful to make sure it works before
 * adding the UI.
 */

@RunWith(AndroidJUnit4::class)
class SleepDatabaseTest {

    private lateinit var sleepDao: SleepDatabaseDao
    private lateinit var db: SleepDatabase

    @Before // Test 전에 불립니다.
    fun createDb() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        // Using an in-memory database because the information stored here disappears when the
        // process is killed.
        /**
         * inMemoryDatabaseBuilder(in-memory) :
         * 이 database는 file system에 실제로 저장되지 않습니다. 또한 태스트가 실핻되면 삭제됩니다.
         * .allowMainThreadQueries() :
         * 원래는 ( default 상태에서는 ) main thread로 query를 실행할 경우 Error가 발생합니다.
         * 하지만 위의 메서드는 main thread에서 쿼리를 실행하는 것을 허용해줍니다.
         * 이 메서드는 오직 test 할 때만 사용해야 합니다.
        **/
        db = Room.inMemoryDatabaseBuilder(context, SleepDatabase::class.java)
                // Allowing main thread queries, just for testing.
                .allowMainThreadQueries()
                .build()
        sleepDao = db.sleepDataBaseDao
    }

    @After // Test가 끝나면 불립니다.
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test // 여러 개를 선언해도 됩니다.
    @Throws(Exception::class) // 무언가 잘못되면 예외를 발생시킵니다.
    fun insertAndGetNight() = runBlocking {
        val night = SleepNight()
        sleepDao.insert(night)
        val tonight = sleepDao.getTonight()
        assertEquals(tonight?.sleepQuality, -1)
    }
}

