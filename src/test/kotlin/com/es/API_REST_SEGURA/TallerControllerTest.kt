package com.es.API_REST_SEGURA

import com.es.API_REST_SEGURA.controller.TallerController
import com.es.API_REST_SEGURA.model.EstadoTaller
import com.es.API_REST_SEGURA.model.Taller
import com.es.API_REST_SEGURA.security.SecurityConfig
import com.es.API_REST_SEGURA.service.TallerService
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.junit5.MockKExtension
import org.bson.types.ObjectId
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.*

@ExtendWith(MockKExtension::class)
@WebMvcTest(TallerController::class)
@Import(SecurityConfig::class, TallerControllerTestConfig::class)
class TallerControllerTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @MockkBean
    lateinit var tallerService: TallerService

    @MockkBean
    lateinit var authenticationManager: AuthenticationManager

    @Test
    fun `debe devolver un taller por ID`() {
        val id = ObjectId()
        val taller = Taller(
            id = id,
            titulo = "diseño textil",
            descripcion = "Crea tus propios patrones y estampados.",
            fecha = Date(),
            plazas = 6,
            estado = EstadoTaller.DISPONIBLE,
            reservas = emptyList()
        )

        every { tallerService.getTallerById(id) } returns taller

        mockMvc.perform(
            get("/talleres/${id.toHexString()}")
                .with(jwt())
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$._id").value(id.toHexString()))
            .andExpect(jsonPath("$.titulo").value("diseño textil"))
            .andExpect(jsonPath("$.descripcion").value("Crea tus propios patrones y estampados."))
            .andExpect(jsonPath("$.plazas").value(6))
            .andExpect(jsonPath("$.estado").value("DISPONIBLE"))
            .andExpect(jsonPath("$.reservas").isEmpty())

    }
}
