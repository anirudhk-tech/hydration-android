package com.example.mizu.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.mizu.data.local.entity.WeightReadingEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WeightReadingDao {
    @Insert
    suspend fun insert(reading: WeightReadingEntity)

    @Query("SELECT * FROM weight_readings ORDER BY timestamp DESC")
    fun getAllReadings(): Flow<List<WeightReadingEntity>>

    @Query("SELECT * FROM weight_readings ORDER BY timestamp DESC LIMIT 1")
    fun getLatestReading(): Flow<WeightReadingEntity?>
}