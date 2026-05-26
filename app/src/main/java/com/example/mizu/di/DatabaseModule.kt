package com.example.mizu.di

import android.content.Context
import androidx.room.Room
import com.example.mizu.data.local.MizuDatabase
import com.example.mizu.data.local.dao.WeightReadingDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideMizuDatabase(@ApplicationContext context: Context): MizuDatabase {
        return Room.databaseBuilder(
            context,
            MizuDatabase::class.java,
            "mizu_database"
        ).build()
    }
    @Provides
    @Singleton
    fun provideWeightReadingDao(database: MizuDatabase): WeightReadingDao {
        return database.weightReadingDao()
    }
}