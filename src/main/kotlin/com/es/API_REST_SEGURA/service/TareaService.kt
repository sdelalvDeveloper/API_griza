package com.es.API_REST_SEGURA.service

import com.es.API_REST_SEGURA.dto.TareaDTO
import com.es.API_REST_SEGURA.dto.TareaRegisterDTO
import com.es.API_REST_SEGURA.error.exception.ForbiddenException
import com.es.API_REST_SEGURA.error.exception.NotFoundException
import com.es.API_REST_SEGURA.model.Estado
import com.es.API_REST_SEGURA.model.Tarea
import com.es.API_REST_SEGURA.repository.TareaRepository
import com.es.API_REST_SEGURA.util.DtoMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service

@Service
class TareaService {

    @Autowired
    private lateinit var tareaRepository: TareaRepository

    private val dtoMapper = DtoMapper()

    fun getTareasByUsername(username: String, authentication: Authentication): List<TareaDTO> {
        val tareas = tareaRepository.getTareas(username.lowercase()).map { tarea ->
            dtoMapper.tareaEntityToDTO(tarea)
        }
        if (authentication.name == username || authentication.authorities.any {it.authority == "ROLE_ADMIN"}) {
            if(tareas.isEmpty()){
                throw ForbiddenException("No hay tareas para $username")
            } else {
                return tareas
            }
        }
        return tareas
    }

    fun getAll(): List<TareaDTO> {
        val tareas = tareaRepository.getAll().map { tarea ->
            dtoMapper.tareaEntityToDTO(tarea)
        }
        return tareas
    }

    fun insertTarea(tarea: TareaRegisterDTO, authentication: Authentication): Tarea? {
        val dtoMapper = DtoMapper()
        if (authentication.name == tarea.username || authentication.authorities.any {it.authority == "ROLE_ADMIN"}) {
            val tareaRegister = dtoMapper.tareaDTOToEntity(tarea)
            return tareaRepository.save(tareaRegister)
        } else {
            throw ForbiddenException("No puede insertar ${tarea.titulo} para otro usuario.")
        }
    }

    fun deleteTareaByTitulo(titulo: String, authentication: Authentication) {
        val tarea = tareaRepository.getTareaByTitulo(titulo.lowercase())
        if (tarea != null) {
            if (authentication.name == tarea.username || authentication.authorities.any {it.authority == "ROLE_ADMIN"}) {
                return tareaRepository.delete(tarea)
            } else {
                throw ForbiddenException("No puedes eliminar tareas de otro usuario.")
            }
        } else {
            throw NotFoundException("No se ha encontrado ninguna tarea con este titulo: $titulo")
        }
    }

    fun cambiarEstadoTarea(titulo: String, estado: Estado, authentication: Authentication): Tarea? {
        val tarea = tareaRepository.getTareaByTitulo(titulo.lowercase())
        if (tarea != null) {
            if (authentication.name == tarea.username || authentication.authorities.any {it.authority == "ROLE_ADMIN"}) {
                tarea.estado = estado
                return tareaRepository.save(tarea)
            } else {
                throw ForbiddenException("No puedes modificar tareas de otro usuario.")
            }
        } else {
            throw NotFoundException("No se ha encontrado ninguna tarea con este titulo: $titulo")
        }
    }

}