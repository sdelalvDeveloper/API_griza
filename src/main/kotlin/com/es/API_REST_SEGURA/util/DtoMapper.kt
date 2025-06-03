package com.es.API_REST_SEGURA.util

import com.es.API_REST_SEGURA.dto.*
import com.es.API_REST_SEGURA.model.*
import org.bson.types.ObjectId
import java.util.*

class DtoMapper {

     // Mapeo de usuarios
    fun userDTOToEntity(usuario: UsuarioRegisterDTO): Usuario {
        val usuarioMapeado =
            Usuario(
                null,
                usuario.username.lowercase(),
                usuario.password,
                usuario.email,
                usuario.telefono
            )
        return usuarioMapeado
    }

    fun userEntityToDTO(usuario: Usuario) : UsuarioDTO {
        val usuarioDTO =
            UsuarioDTO(
                usuario.username,
                usuario.email,
                usuario.telefono,
                usuario.bono
            )
        return usuarioDTO
    }

    // Mapeo de talleres
    fun tallerRegisterDTOToEntity(taller: TallerRegisterDTO): Taller {
        val tallerRegistrar = Taller(
            null,
            taller.titulo.lowercase(),
            taller.descripcion,
            taller.fecha
        )
        return tallerRegistrar
    }

    fun tallerDTOToEntity(taller: TallerDTO): Taller {
        val tallerEntity = Taller(
            null,
            taller.titulo,
            taller.descripcion,
            taller.fecha
        )
        return tallerEntity
    }

    fun tallerEntityToDTO(taller: Taller): TallerDTO {
        val tallerDTO = TallerDTO(
            taller.id.toString(),
            taller.titulo,
            taller.descripcion,
            taller.fecha,
            taller.plazas,
            taller.estado
        )
        return tallerDTO
    }

    fun tallerEntityToRegisterDto(taller: Taller): TallerRegisterDTO {
        val tallerDTO = TallerRegisterDTO(
            taller.titulo,
            taller.descripcion,
            taller.fecha,
        )
        return tallerDTO
    }

    // Mapeo de reservas
    fun reservaDTOToEntity(reserva: ReservaRegisterDTO): Reserva {
        val reservaRegistrar = Reserva(
            null,
            reserva.username,
            ObjectId(reserva.tallerID)
        )
        return reservaRegistrar
    }

    fun reservaEntityToDTO(reserva: Reserva): ReservaDTO {
        val reservaDTO = ReservaDTO(
            reserva.id.toString(),
            reserva.tallerID.toString(),
            reserva.estado,
            reserva.fecha
        )
        return reservaDTO
    }

    fun reservaEntityToFullDTO(reserva: Reserva, taller: Taller): ReservaFullDTO {
        val reservaFullDTO = ReservaFullDTO(
            reserva.id.toString(),
            reserva.username,
            taller.titulo,
            reserva.tallerID.toString(),
            reserva.estado,
            reserva.fecha
        )
        return reservaFullDTO
    }

}