package com.es.API_REST_SEGURA.controller

import com.es.API_REST_SEGURA.model.Tarea
import com.es.API_REST_SEGURA.service.TareaService
import jakarta.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/tareas")
class TareaController {

    @Autowired
    private lateinit var tareaService: TareaService

    @GetMapping("/{username}")
    fun getTareas(httpRequest: HttpServletRequest,
                 @PathVariable username: String
    ): ResponseEntity<List<Tarea>>? {
        val tarea = tareaService.getTareasByUsername(username)

        return ResponseEntity(tarea, HttpStatus.OK)
    }

    @PostMapping("/register")
    fun insertTarea(httpRequest: HttpServletRequest,
                    @RequestBody tarea: Tarea
    ): ResponseEntity<Tarea>? {
        val tareaRegistrada = tareaService.insertTarea(tarea)

        return ResponseEntity(tareaRegistrada, HttpStatus.CREATED)
    }

    @DeleteMapping("/{titulo}")
    fun deleteTarea(httpRequest: HttpServletRequest,
                    @RequestBody tarea: Tarea
    ): ResponseEntity<Tarea> {
        val tareaEliminada = tareaService.deleteTareaByTitulo(tarea)

        return ResponseEntity(tareaEliminada, HttpStatus.NO_CONTENT)
    }

    @PutMapping()
    fun cambiarEstadoTarea(httpRequest: HttpServletRequest,
                           @RequestBody tarea: Tarea
    ): ResponseEntity<Tarea> {
        val tareaCompletada = tareaService.cambiarEstadoTarea(tarea)

        return ResponseEntity(tareaCompletada, HttpStatus.OK)
    }
}