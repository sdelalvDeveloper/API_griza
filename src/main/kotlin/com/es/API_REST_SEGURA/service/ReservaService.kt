package com.es.API_REST_SEGURA.service

import com.es.API_REST_SEGURA.dto.ReservaDTO
import com.es.API_REST_SEGURA.dto.ReservaRegisterDTO
import com.es.API_REST_SEGURA.error.exception.ForbiddenException
import com.es.API_REST_SEGURA.error.exception.NotFoundException
import com.es.API_REST_SEGURA.model.EstadoReserva
import com.es.API_REST_SEGURA.repository.ReservaRepository
import com.es.API_REST_SEGURA.util.DtoMapper
import org.bson.types.ObjectId
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service

@Service
class ReservaService {

    @Autowired
    private lateinit var reservaRepository: ReservaRepository

    private val dtoMapper = DtoMapper()

    fun getReservaByUsername(username: String, authentication: Authentication): List<ReservaDTO> {
        var reservas = reservaRepository.getReservaByUsername(username.lowercase()).map { reserva ->
            dtoMapper.reservaEntityToDTO(reserva)
        }

        if (authentication.authorities.any {it.authority == "ROLE_ADMIN"}) {
            reservas = getAll()
        } else if (authentication.name == username) {
            if(reservas.isEmpty()){
                throw ForbiddenException("No hay talleres para $username")
            } else {
                return reservas
            }
        }

        return reservas
    }

    fun getAll(): List<ReservaDTO> {
        val reservas = reservaRepository.getAll().map { reserva ->
            dtoMapper.reservaEntityToDTO(reserva)
        }
        return reservas
    }

    fun insertReserva(reserva: ReservaRegisterDTO, authentication: Authentication): ReservaDTO? {
        if (authentication.authorities.any {it.authority == "ROLE_ADMIN"}) {
            val reservaRegister = dtoMapper.reservaDTOToEntity(reserva)
            reservaRepository.save(reservaRegister)
            val reservaRegistrado = dtoMapper.reservaEntityToDTO(reservaRegister)
            return reservaRegistrado
        } else {
            throw ForbiddenException("No se pudo insertar la reserva.")
        }
    }

    fun deleteReservaById(id: ObjectId, authentication: Authentication) {
        val reserva = reservaRepository.getReservaById(id)
        if (reserva != null) {
            if (authentication.authorities.any {it.authority == "ROLE_ADMIN"}) {
                return reservaRepository.delete(reserva)
            } else {
                throw ForbiddenException("No se pudo eliminar el taller .")
            }
        } else {
            throw NotFoundException("No se ha encontrado ningún taller con este id: $id")
        }
    }

    fun cambiarEstadoReserva(id: ObjectId, estado: EstadoReserva, authentication: Authentication): ReservaDTO? {
        val reserva = reservaRepository.getReservaById(id)
        if (reserva != null) {
            if (authentication.authorities.any {it.authority == "ROLE_ADMIN"}) {
                reserva.estado = estado
                reservaRepository.save(reserva)
                val reservaActualizada = dtoMapper.reservaEntityToDTO(reserva)
                return reservaActualizada
            } else {
                throw ForbiddenException("No puedes modificar tareas de otro usuario.")
            }
        } else {
            throw NotFoundException("No se ha encontrado ninguna tarea con este id: $id")
        }
    }
}