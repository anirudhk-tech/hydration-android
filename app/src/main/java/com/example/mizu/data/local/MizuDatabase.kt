package com.example.mizu.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.mizu.data.local.dao.WeightReadingDao
import com.example.mizu.data.local.entity.WeightReadingEntity

@Database(entities = [WeightReadingEntity::class], version = 1, exportSchema = false)
abstract class MizuDatabase : RoomDatabase() {
    abstract fun weightReadingDao() : WeightReadingDao
}