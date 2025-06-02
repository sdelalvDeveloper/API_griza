package com.es.API_REST_SEGURA.controller

import com.es.API_REST_SEGURA.dto.TallerDTO
import com.es.API_REST_SEGURA.dto.TallerRegisterDTO
import com.es.API_REST_SEGURA.model.EstadoTaller
import com.es.API_REST_SEGURA.model.Taller
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

    @GetMapping("/{id}")
    fun getTallerById(httpRequest: HttpServletRequest,
                      @PathVariable id: String
    ): ResponseEntity<Taller> {
        val objectId = ObjectId(id)
        val tallerDTO = tallerService.getTallerById(objectId)
        return ResponseEntity.ok(tallerDTO)
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
                     @RequestBody taller: TallerRegisterDTO,
    ): ResponseEntity<TallerDTO>? {
        val tallerRegistrado = tallerService.insertTaller(taller)

        return ResponseEntity(tallerRegistrado, HttpStatus.CREATED)
    }

    @DeleteMapping("/delete/{id}")
    fun deleteTaller(httpRequest: HttpServletRequest,
                     @PathVariable id: ObjectId,
                     authentication: Authentication
    ): ResponseEntity<TallerDTO> {
        tallerService.deleteTallerById(id, authentication)

        return ResponseEntity.noContent().build()
    }

    @PostMapping("/update/{id}")
    fun updateTaller(
        httpRequest: HttpServletRequest,
                     @PathVariable id: String,
                     @RequestBody taller: TallerRegisterDTO,
                     authentication: Authentication
    ): ResponseEntity<TallerDTO> {
        val tallerActualizado = tallerService.updateTaller(ObjectId(id), taller)
        return ResponseEntity.ok(tallerActualizado)
    }
}