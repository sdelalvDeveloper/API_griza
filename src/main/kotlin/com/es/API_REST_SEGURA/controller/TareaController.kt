package com.es.API_REST_SEGURA.controller

import com.es.API_REST_SEGURA.dto.TareaDTO
import com.es.API_REST_SEGURA.dto.TareaRegisterDTO
import com.es.API_REST_SEGURA.model.Tarea
import com.es.API_REST_SEGURA.service.TareaService
import jakarta.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/tareas")
class TareaController {

    @Autowired
    private lateinit var tareaService: TareaService

    @GetMapping("/{username}")
    fun getTareas(httpRequest: HttpServletRequest,
                 @PathVariable username: String,
                  authentication: Authentication
    ): ResponseEntity<List<TareaDTO>>? {
        val tarea = tareaService.getTareasByUsername(username, authentication)

        return ResponseEntity(tarea, HttpStatus.OK)
    }

    @GetMapping("/getAll")
    fun getAllTareas(httpRequest: HttpServletRequest, authentication: Authentication): ResponseEntity<List<TareaDTO>>? {
        val tareas = tareaService.getAll()
        return ResponseEntity(tareas, HttpStatus.OK)
    }

    @PostMapping("/register")
    fun insertTarea(httpRequest: HttpServletRequest,
                    @RequestBody tarea: TareaRegisterDTO,
                    authentication: Authentication
    ): ResponseEntity<Tarea>? {
        val tareaRegistrada = tareaService.insertTarea(tarea, authentication)

        return ResponseEntity(tareaRegistrada, HttpStatus.CREATED)
    }

    @DeleteMapping("/{titulo}")
    fun deleteTarea(httpRequest: HttpServletRequest,
                    @PathVariable titulo: String,
                    authentication: Authentication
    ): ResponseEntity<Tarea> {
        tareaService.deleteTareaByTitulo(titulo, authentication)

        return ResponseEntity.noContent().build()
    }

    @PutMapping("/estado/{titulo}")
    fun cambiarEstadoTarea(httpRequest: HttpServletRequest,
                           @PathVariable titulo: String,
                           authentication: Authentication
    ): ResponseEntity<Tarea> {
        val tareaCompletada = tareaService.cambiarEstadoTarea(titulo, authentication)

        return ResponseEntity(tareaCompletada, HttpStatus.OK)
    }
}