package com.es.API_REST_SEGURA.controller

import com.es.API_REST_SEGURA.dto.ReservaDTO
import com.es.API_REST_SEGURA.dto.ReservaFullDTO
import com.es.API_REST_SEGURA.dto.ReservaRegisterDTO
import com.es.API_REST_SEGURA.error.exception.NotFoundException
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
    ): ResponseEntity<List<ReservaFullDTO>>? {
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
    ): ResponseEntity<ReservaFullDTO>? {
        val reservaRegistrada = reservaService.insertReserva(reserva, authentication)

        return ResponseEntity(reservaRegistrada, HttpStatus.CREATED)
    }

    @DeleteMapping("delete/{id}/taller/{tallerID}")
    fun deleteReserva(httpRequest: HttpServletRequest,
                     @PathVariable id: String,
                      @PathVariable tallerID: String,
                      authentication: Authentication
    ): ResponseEntity<Any> {
        reservaService.deleteReservaById(ObjectId(id), ObjectId(tallerID), authentication)

        return ResponseEntity.noContent().build()
    }

    @DeleteMapping("delete/{tallerID}")
    fun deleteReservaByIdTaller(httpRequest: HttpServletRequest,
                      @PathVariable tallerID: String,
                      authentication: Authentication
    ): ResponseEntity<Any> {
        reservaService.deleteReservaByIdTaller(ObjectId(tallerID), authentication)

        return ResponseEntity.noContent().build()
    }

    @GetMapping("first/{username}")
    fun getFirstReservaByUsername(httpRequest: HttpServletRequest,
                                  @PathVariable username: String,
                                  authentication: Authentication
    ): ResponseEntity<ReservaFullDTO>? {
        val reserva = reservaService.getReservaByUsername(username, authentication).firstOrNull()

        return if (reserva != null) {
            ResponseEntity(reserva, HttpStatus.OK)
        } else {
            throw NotFoundException("Sin reservas")
        }
    }

    @DeleteMapping("/deleteAll/{username}")
    fun deleteReservaAll(httpRequest: HttpServletRequest,
                      @PathVariable username: String,
                      authentication: Authentication
    ): ResponseEntity<Any> {
        reservaService.deleteAll(username, authentication)

        return ResponseEntity.noContent().build()
    }

}