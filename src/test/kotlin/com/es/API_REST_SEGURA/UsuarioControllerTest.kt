package com.es.API_REST_SEGURA

import com.es.API_REST_SEGURA.controller.UsuarioController
import com.es.API_REST_SEGURA.dto.UsuarioDTO
import com.es.API_REST_SEGURA.dto.UsuarioRegisterDTO
import com.es.API_REST_SEGURA.security.SecurityConfig
import com.es.API_REST_SEGURA.service.TokenService
import com.es.API_REST_SEGURA.service.UsuarioService
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@ExtendWith(MockKExtension::class)
@WebMvcTest(UsuarioController::class)
@Import(SecurityConfig::class, LoginControllerTestConfig::class)
class UsuarioControllerTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @MockkBean
    lateinit var authenticationManager: AuthenticationManager

    @MockkBean
    lateinit var tokenService: TokenService

    @MockkBean
    lateinit var usuarioService: UsuarioService

    @Test
    @WithMockUser // Simula un usuario autenticado para evitar 403
    fun `login exitoso retorna token y rol`() {
        val username = "usuario"
        val password = "secreto"
        val token = "fake-jwt-token"

        val auth = UsernamePasswordAuthenticationToken(
            username,
            password,
            listOf(SimpleGrantedAuthority("ROLE_USER"))
        )

        every { authenticationManager.authenticate(match {
            it.principal == username && it.credentials == password
        }) } returns auth

        every { tokenService.generarToken(auth) } returns token

        val jsonBody = """
            {
                "username": "$username",
                "password": "$password"
            }
        """.trimIndent()

        mockMvc.perform(
            post("/usuarios/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBody)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.token").value(token))
            .andExpect(jsonPath("$.role").value("USER"))
    }

    @Test
    fun `insert usuario retorna CREATED y usuario insertado`() {
        val usuarioRequest = UsuarioRegisterDTO(
            username = "nuevoUsuario",
            email = "nuevo@usuario.com",
            telefono = "123456789",
            password = "pass123",
            passwordRepeat = "pass123"
        )

        val usuarioResponse = UsuarioDTO(
            username = "nuevoUsuario",
            email = "nuevo@usuario.com",
            telefono = "123456789",
            bono = 10
        )

        every { usuarioService.insertUser(usuarioRequest) } returns usuarioResponse

        val jsonBody = """
        {
            "username": "${usuarioRequest.username}",
            "email": "${usuarioRequest.email}",
            "telefono": "${usuarioRequest.telefono}",
            "password": "${usuarioRequest.password}",
            "passwordRepeat": "${usuarioRequest.passwordRepeat}"
        }
    """.trimIndent()

        mockMvc.perform(
            post("/usuarios/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBody)
        )
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$.username").value(usuarioResponse.username))
            .andExpect(jsonPath("$.email").value(usuarioResponse.email))
            .andExpect(jsonPath("$.telefono").value(usuarioResponse.telefono))
            .andExpect(jsonPath("$.bono").value(usuarioResponse.bono))
    }
}



