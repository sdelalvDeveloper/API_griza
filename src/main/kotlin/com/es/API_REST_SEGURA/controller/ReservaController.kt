package com.es.API_REST_SEGURA.controller

import com.es.API_REST_SEGURA.dto.ReservaDTO
import com.es.API_REST_SEGURA.dto.ReservaRegisterDTO
import com.es.API_REST_SEGURA.model.EstadoReserva
import com.es.API_REST_SEGURA.service.ReservaService
import jakarta.servlet.http.HttpServletRequest
import org.bson.types.ObjectId
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/reservas")
class ReservaController {

    @Autowired
    private lateinit var reservaService: ReservaService

    @GetMapping("/{username}")
    fun getReservaByUsername(httpRequest: HttpServletRequest,
                            @PathVariable username: String,
                            authentication: Authentication
    ): ResponseEntity<List<ReservaDTO>>? {
        val reserva = reservaService.getReservaByUsername(username, authentication)

        return ResponseEntity(reserva, HttpStatus.OK)
    }

    @GetMapping("/getAll")
    fun getAllReservas(httpRequest: HttpServletRequest,
                       authentication: Authentication
    ): ResponseEntity<List<ReservaDTO>>? {
        val reservas = reservaService.getAll()

        return ResponseEntity(reservas, HttpStatus.OK)
    }

    @PostMapping("/register")
    fun insertReserva(httpRequest: HttpServletRequest,
                      @RequestBody reserva: ReservaRegisterDTO,
                      authentication: Authentication
    ): ResponseEntity<ReservaDTO>? {
        val reservaRegistrada = reservaService.insertReserva(reserva, authentication)

        return ResponseEntity(reservaRegistrada, HttpStatus.CREATED)
    }

    @DeleteMapping("/{id}")
    fun deleteTReserva(httpRequest: HttpServletRequest,
                     @PathVariable id: ObjectId,
                     authentication: Authentication
    ): ResponseEntity<ReservaDTO> {
        reservaService.deleteReservaById(id, authentication)

        return ResponseEntity.noContent().build()
    }

    @PutMapping("/{id}/cambiar-estado")
    fun cambiarEstadoReserva(
        @PathVariable id: ObjectId,
        @RequestBody nuevoEstado: EstadoReserva, // Nuevo estado (COMPLETADA o PENDIENTE)
        authentication: Authentication
    ): ResponseEntity<ReservaDTO> {
        val reservaActualizada = reservaService.cambiarEstadoReserva(id, nuevoEstado, authentication)
        return ResponseEntity(reservaActualizada, HttpStatus.OK)
    }
}