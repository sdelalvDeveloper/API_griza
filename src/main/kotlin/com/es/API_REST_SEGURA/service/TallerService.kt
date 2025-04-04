package com.es.API_REST_SEGURA.service

import com.es.API_REST_SEGURA.dto.TallerDTO
import com.es.API_REST_SEGURA.dto.TallerRegisterDTO
import com.es.API_REST_SEGURA.error.exception.ForbiddenException
import com.es.API_REST_SEGURA.error.exception.NotFoundException
import com.es.API_REST_SEGURA.model.EstadoTaller
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

    fun getTallerByUsername(username: String, authentication: Authentication): List<TallerDTO> {
        var talleres = tallerRepository.getTalleresByUsername(username.lowercase()).map { taller ->
            dtoMapper.tallerEntityToDTO(taller)
        }

        if (authentication.authorities.any {it.authority == "ROLE_ADMIN"}) {
            talleres = getAll()
        } else if (authentication.name == username) {
            if(talleres.isEmpty()){
                throw ForbiddenException("No hay talleres para $username")
            } else {
                return talleres
            }
        }

        return talleres
    }

    fun getAll(): List<TallerDTO> {
        val talleres = tallerRepository.getAll().map { tarea ->
            dtoMapper.tallerEntityToDTO(tarea)
        }
        return talleres
    }

    fun insertTaller(taller: TallerRegisterDTO, authentication: Authentication): TallerDTO? {
        if (authentication.authorities.any {it.authority == "ROLE_ADMIN"}) {
            val tallerRegister = dtoMapper.tallerDTOToEntity(taller)
            tallerRepository.save(tallerRegister)
            val tallerRegistrado = dtoMapper.tallerEntityToDTO(tallerRegister)
            return tallerRegistrado
        } else {
            throw ForbiddenException("No se pudo insertar ${taller.titulo}.")
        }
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

    fun cambiarEstadoTaller(id: ObjectId, estado: EstadoTaller, authentication: Authentication): TallerDTO? {
        val taller = tallerRepository.getTallerById(id)
        if (taller != null) {
            if (authentication.authorities.any {it.authority == "ROLE_ADMIN"}) {
                taller.estado = estado
                tallerRepository.save(taller)
                val tallerActualizado = dtoMapper.tallerEntityToDTO(taller)
                return tallerActualizado
            } else {
                throw ForbiddenException("No puedes modificar tareas de otro usuario.")
            }
        } else {
            throw NotFoundException("No se ha encontrado ninguna tarea con este id: $id")
        }
    }

}