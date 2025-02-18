package com.es.API_REST_SEGURA.util

import com.es.API_REST_SEGURA.dto.UsuarioRegisterDTO
import com.es.API_REST_SEGURA.model.Direccion
import com.es.API_REST_SEGURA.model.Usuario

class DtoMapper {

    fun userDTOToEntity(usuario: UsuarioRegisterDTO) : Usuario {
        val direccion = Direccion(
            usuario.calle,
            usuario.num,
            usuario.provincia,
            usuario.cp
        )
        val usuarioMapeado =
            Usuario(
                null,
                usuario.username,
                usuario.password,
                usuario.email,
                usuario.telefono,
                direccion
            )
        return usuarioMapeado
    }

}