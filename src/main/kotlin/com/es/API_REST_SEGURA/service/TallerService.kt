package com.es.API_REST_SEGURA.service

import com.es.API_REST_SEGURA.dto.TallerDTO
import com.es.API_REST_SEGURA.dto.TallerRegisterDTO
import com.es.API_REST_SEGURA.error.exception.BadRequestException
import com.es.API_REST_SEGURA.error.exception.ForbiddenException
import com.es.API_REST_SEGURA.error.exception.NotFoundException
import com.es.API_REST_SEGURA.model.EstadoTaller
import com.es.API_REST_SEGURA.model.Reserva
import com.es.API_REST_SEGURA.model.Taller
import com.es.API_REST_SEGURA.repository.TallerRepository
import com.es.API_REST_SEGURA.util.DtoMapper
import org.bson.types.ObjectId
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service

@Service
class TallerService {

    @Autowired
    private lateinit var tallerRepository: TallerRepository

    private val dtoMapper = DtoMapper()

    fun getTallerById(id: ObjectId): Taller {
        val taller = tallerRepository.getTallerById(id)
            ?: throw RuntimeException("Taller no encontrado con id: $id")

        return taller
    }


    fun getAll(): List<TallerDTO> {
        val talleres = tallerRepository.getAll().map { tarea ->
            dtoMapper.tallerEntityToDTO(tarea)
        }
        return talleres
    }

    fun insertTaller(taller: TallerRegisterDTO): TallerDTO {
        val tallerRegister = dtoMapper.tallerRegisterDTOToEntity(taller)
        tallerRepository.save(tallerRegister)
        return dtoMapper.tallerEntityToDTO(tallerRegister)
    }


    fun deleteTallerById(id: ObjectId, authentication: Authentication) {
        val taller = tallerRepository.getTallerById(id)
        if (taller != null) {
            if (authentication.authorities.any {it.authority == "ROLE_ADMIN"}) {
                return tallerRepository.delete(taller)
            } else {
                throw ForbiddenException("No se pudo eliminar el taller .")
            }
        } else {
            throw NotFoundException("No se ha encontrado ningún taller con este id: $id")
        }
    }

    fun updateReservasTaller(id: ObjectId, tallerID: ObjectId): Boolean {
        // Obtener el taller relacionado
        val tallerEntity = getTallerById(tallerID)

        // Eliminar la reserva de la lista del taller (por ID)
        val reservasActualizadas = tallerEntity.reservas
            .filter { it.id != id }  // Filtramos la reserva cancelada

        // Sumar una plaza (sin superar el total original)
        var plazasActualizadas = (tallerEntity.plazas + 1).coerceAtMost(6)  // 6 es el límite
        if (tallerEntity.reservas.isEmpty()) {
            plazasActualizadas = 6
        }

        // Actualizar estado
        val nuevoEstado = cambiarEstadoTaller(plazasActualizadas)

        val tallerActualizado = tallerEntity.copy(
            reservas = reservasActualizadas,
            estado = nuevoEstado,
            plazas = plazasActualizadas
        )

        // Guardar el taller actualizado
        tallerEntity.id?.let { tallerRepository.updateTaller(it, tallerActualizado) }

        return tallerRepository.updateTaller(id, tallerActualizado)
    }

    fun updateTaller(id: ObjectId, nuevoTaller: TallerRegisterDTO): TallerDTO{
        val tallerEntity = tallerRepository.getTallerById(id)

        val tallerActualizado = tallerEntity?.copy(
            titulo = nuevoTaller.titulo,
            descripcion = nuevoTaller.descripcion,
            fecha = nuevoTaller.fecha
        )
        val actualizado = tallerActualizado?.let { tallerRepository.updateTaller(id, it) }
        if (!actualizado!!) {
            throw BadRequestException("No se pudo actualizar el taller.")
        }

        return dtoMapper.tallerEntityToDTO(tallerActualizado)
    }

    fun cambiarEstadoTaller(plazas: Int): EstadoTaller {
        return if (plazas == 0) EstadoTaller.COMPLETO else EstadoTaller.DISPONIBLE
    }

}