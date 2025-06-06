package com.es.API_REST_SEGURA

import com.es.API_REST_SEGURA.service.TallerService
import io.mockk.mockk
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean

@TestConfiguration
class TallerControllerTestConfig {
    @Bean
    fun tallerService(): TallerService = mockk(relaxed = true)
}