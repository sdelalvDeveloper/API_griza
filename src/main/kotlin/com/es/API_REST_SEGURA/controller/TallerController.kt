package com.es.API_REST_SEGURA.controller

import com.es.API_REST_SEGURA.dto.TallerDTO
import com.es.API_REST_SEGURA.dto.TallerRegisterDTO
import com.es.API_REST_SEGURA.model.EstadoTaller
import com.es.API_REST_SEGURA.service.TallerService
import jakarta.servlet.http.HttpServletRequest
import org.bson.types.ObjectId
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/talleres")
class TallerController {

    @Autowired
    private lateinit var tallerService: TallerService

    @GetMapping("/{username}")
    fun getTallerByUsername(httpRequest: HttpServletRequest,
                  @PathVariable username: String,
                  authentication: Authentication
    ): ResponseEntity<List<TallerDTO>>? {
        val taller = tallerService.getTallerByUsername(username, authentication)

        return ResponseEntity(taller, HttpStatus.OK)
    }

    @GetMapping("/getAll")
    fun getAllTalleres(httpRequest: HttpServletRequest,
                     authentication: Authentication
    ): ResponseEntity<List<TallerDTO>>? {
        val talleres = tallerService.getAll()

        return ResponseEntity(talleres, HttpStatus.OK)
    }

    @PostMapping("/register")
    fun insertTaller(httpRequest: HttpServletRequest,
                     @RequestBody tarea: TallerRegisterDTO,
                     authentication: Authentication
    ): ResponseEntity<TallerDTO>? {
        val tallerRegistrada = tallerService.insertTaller(tarea, authentication)

        return ResponseEntity(tallerRegistrada, HttpStatus.CREATED)
    }

    @DeleteMapping("/{id}")
    fun deleteTaller(httpRequest: HttpServletRequest,
                     @PathVariable id: ObjectId,
                     authentication: Authentication
    ): ResponseEntity<TallerDTO> {
        tallerService.deleteTallerById(id, authentication)

        return ResponseEntity.noContent().build()
    }

    @PutMapping("/{id}/cambiar-estado")
    fun cambiarEstadoTaller(
        @PathVariable id: ObjectId,
        @RequestBody nuevoEstado: EstadoTaller, // Nuevo estado (COMPLETADA o PENDIENTE)
        authentication: Authentication
    ): ResponseEntity<TallerDTO> {
        val tallerActualizada = tallerService.cambiarEstadoTaller(id, nuevoEstado, authentication)
        return ResponseEntity(tallerActualizada, HttpStatus.OK)
    }
}