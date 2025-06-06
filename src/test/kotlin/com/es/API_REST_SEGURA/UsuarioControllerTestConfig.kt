package com.es.API_REST_SEGURA

import com.es.API_REST_SEGURA.service.TokenService
import com.es.API_REST_SEGURA.service.UsuarioService
import io.mockk.mockk
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean

@TestConfiguration
class LoginControllerTestConfig {

    @Bean
    fun tokenService(): TokenService = mockk(relaxed = true)

    @Bean
    fun usuarioService(): UsuarioService = mockk(relaxed = true)
}