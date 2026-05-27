package com.example.mizu.data.remote.dto

import org.junit.Assert.assertEquals
import org.junit.Test

class WeightReadingDtoTest {

    @Test
    fun `toDomain copies weight and timestamp`() {
        val dto = WeightReadingDto(weightKg = 73.4, timestamp = 5000L)

        val domain = dto.toDomain()

        assertEquals(73.4, domain.weightKg, 0.001)
        assertEquals(5000L, domain.timestamp)
    }
}
