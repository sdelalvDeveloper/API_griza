package com.es.API_REST_SEGURA

import com.es.API_REST_SEGURA.controller.ReservaController
import com.es.API_REST_SEGURA.dto.ReservaFullDTO
import com.es.API_REST_SEGURA.dto.ReservaRegisterDTO
import com.es.API_REST_SEGURA.model.EstadoReserva
import com.es.API_REST_SEGURA.security.SecurityConfig
import com.es.API_REST_SEGURA.service.ReservaService
import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.test.web.servlet.MockMvc
import java.util.*
import kotlin.test.Test
import io.mockk.every
import org.springframework.http.MediaType
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(ReservaController::class)
@Import(SecurityConfig::class, ReservaControllerTestConfig::class)  // importa tu configuración de seguridad
class ReservaControllerTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @MockkBean
    lateinit var authenticationManager: AuthenticationManager

    @MockkBean
    lateinit var reservaService: ReservaService

    @Autowired
    lateinit var objectMapper: ObjectMapper


    @Test
    fun `insertReserva devuelve reserva creada con estado 201`() {
        val reservaRegisterDTO = ReservaRegisterDTO(
            username = "usuario1",
            tallerID = "6842a062fca6cc216bef5277"
        )

        val reservaFullDTO = ReservaFullDTO(
            id = "123abc",
            username = "usuario1",
            tituloTaller = "Taller de Kotlin",
            tallerID = "6842a062fca6cc216bef5277",
            estado = EstadoReserva.ACTIVA,  // Asumiendo que EstadoReserva es un enum
            fechaTaller = Date()
        )

        every { reservaService.insertReserva(any(), any()) } returns reservaFullDTO

        mockMvc.perform(
            post("/reservas/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reservaRegisterDTO))
                .with(jwt())
        )
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$.id").value(reservaFullDTO.id))
            .andExpect(jsonPath("$.username").value("usuario1"))
            .andExpect(jsonPath("$.tituloTaller").value("Taller de Kotlin"))
            .andExpect(jsonPath("$.tallerID").value("6842a062fca6cc216bef5277"))
            .andExpect(jsonPath("$.estado").value("ACTIVA"))
            .andExpect(jsonPath("$.fechaTaller").exists())
    }
}
