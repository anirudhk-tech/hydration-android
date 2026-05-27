package com.example.mizu.di

import com.example.mizu.data.sensor.MockWeightSensorService
import com.example.mizu.data.sensor.WeightSensorService
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class SensorModule {
    @Binds
    @Singleton
    abstract fun bindWeightSensorService(
        impl: MockWeightSensorService
    ): WeightSensorService
}