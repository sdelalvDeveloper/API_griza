package com.es.API_REST_SEGURA.service

import com.es.API_REST_SEGURA.error.exception.NotFoundException
import com.es.API_REST_SEGURA.error.exception.UnauthorizedException
import com.es.API_REST_SEGURA.model.Estado
import com.es.API_REST_SEGURA.model.Tarea
import com.es.API_REST_SEGURA.repository.TareaRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service

@Service
class TareaService {

    @Autowired
    private lateinit var tareaRepository: TareaRepository

    fun getTareasByUsername(username: String): List<Tarea>{
        val tareas = tareaRepository.getTareas(username)

        if(tareas != null) return tareas else throw NotFoundException("No hay tareas para $username")
    }

    fun insertTarea(tarea: Tarea): Tarea {
        val authentication = SecurityContextHolder.getContext().authentication
        val usernameAuth = (authentication.principal as UserDetails).username

        // Asignar automáticamente el usuario autenticado a la tarea
        val tareaConUsuario = tarea.copy(username = usernameAuth)

        return tareaRepository.save(tareaConUsuario)
    }

    fun deleteTareaByTitulo(tarea: Tarea): Tarea {
        val authentication = SecurityContextHolder.getContext().authentication
        val usernameAuth = (authentication.principal as UserDetails).username

        // Verificar que la tarea pertenece al usuario autenticado
        if (tarea.username != usernameAuth) {
            throw UnauthorizedException("No puedes eliminar tareas de otro usuario.")
        }

        tareaRepository.delete(tarea)

        return tarea
    }

    fun cambiarEstadoTarea(tarea: Tarea): Tarea {
        // Obtener el usuario autenticado desde el token JWT
        val authentication = SecurityContextHolder.getContext().authentication
        val usernameAuth = (authentication.principal as UserDetails).username
        val rolAuth = (authentication.principal as UserDetails).authorities.map { it.authority }

        // Verificar que la tarea pertenece al usuario autenticado
        if (tarea.username != usernameAuth || !rolAuth.contains("ADMIN")) {
            throw UnauthorizedException("No puedes modificar tareas de otro usuario.")
        }

        if (tarea.estado == Estado.PENDIENTE) {
            tarea.estado = Estado.COMPLETADA

        }
        tareaRepository.save(tarea)
        return tarea
    }

}