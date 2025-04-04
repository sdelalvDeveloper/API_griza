package com.es.API_REST_SEGURA.util

import com.es.API_REST_SEGURA.dto.*
import com.es.API_REST_SEGURA.model.*

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

    fun userEntityToDTO(usuario: Usuario) : UsuarioDTO? {
        val usuarioDTO = usuario.roles?.let {
            UsuarioDTO(
                usuario.username,
                usuario.email,
                it
            )
        }
        return usuarioDTO
    }

    // Mapeo de talleres
    fun tallerDTOToEntity(taller: TallerRegisterDTO): Taller {
        val tallerRegistrar = Taller(
            null,
            taller.titulo.lowercase(),
            taller.descripcion,
            taller.fecha
        )
        return tallerRegistrar
    }

    fun tallerEntityToDTO(taller: Taller): TallerDTO {
        val tallerDTO = TallerDTO(
            taller.titulo,
            taller.descripcion,
            taller.fecha,
            taller.estado
        )
        return tallerDTO
    }

    // Mapeo de reservas
    fun reservaDTOToEntity(reserva: ReservaRegisterDTO): Reserva {
        val reservaRegistrar = Reserva(
            null,
            reserva.username,
            reserva.tallerID
        )
        return reservaRegistrar
    }

    fun reservaEntityToDTO(reserva: Reserva): ReservaDTO {
        val reservaDTO = ReservaDTO(
            reserva.username,
            reserva.estado,
            reserva.fecha
        )
        return reservaDTO
    }

}