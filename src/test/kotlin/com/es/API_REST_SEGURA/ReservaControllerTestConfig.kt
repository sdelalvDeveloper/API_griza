package com.es.API_REST_SEGURA


import com.es.API_REST_SEGURA.service.ReservaService
import io.mockk.mockk
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean

@TestConfiguration
class ReservaControllerTestConfig {
    @Bean
    fun reservaService(): ReservaService = mockk(relaxed = true)
}