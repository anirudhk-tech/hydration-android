package com.example.mizu.data.remote

import com.example.mizu.data.remote.dto.WeightReadingDto
import retrofit2.http.GET

interface WeightApiService {

    // GET http://192.168.1.100/weight
    // Response: { "weight_kg": 72.5, "timestamp": 1234567890 }
    @GET("weight")
    suspend fun getLatestReading(): WeightReadingDto

    // GET http://192.168.1.100/readings
    // Response: [{ "weight_kg": 72.5, "timestamp": 1234567890 }, ...]
    @GET("readings")
    suspend fun getAllReadings(): List<WeightReadingDto>
}
