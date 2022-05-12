package com.udacity.asteroidradar.database

import android.content.Context
import androidx.room.*
import com.udacity.asteroidradar.models.Asteroid
import com.udacity.asteroidradar.models.AsteroidEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AsteroidDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAsteroidList(vararg asteroids: AsteroidEntity)

    @Query("SELECT * FROM AsteroidEntity WHERE closeApproachDate >= :startDate and closeApproachDate <= :endDate ORDER BY closeApproachDate")
    fun getAsteroidList(startDate: String, endDate: String): Flow<List<Asteroid>>

    @Query("SELECT * FROM AsteroidEntity ORDER BY closeApproachDate")
    fun getSavedAsteroid(): Flow<List<Asteroid>>

    @Query("DELETE FROM AsteroidEntity WHERE closeApproachDate < :today")
    fun deletePrevDayAsteroid(today: String)
}

@Database(entities = [AsteroidEntity::class], version = 1)
abstract class NasaDatabase : RoomDatabase() {
    abstract val asteroidDao: AsteroidDao
}

private lateinit var INSTANCE: NasaDatabase
fun getDatabase(context: Context): NasaDatabase {
    synchronized(NasaDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(
                context.applicationContext,
                NasaDatabase::class.java,
                "nasa_table"
            ).build()
        }
    }
    return INSTANCE
}