package com.es.API_REST_SEGURA.util

import com.es.API_REST_SEGURA.dto.TareaDTO
import com.es.API_REST_SEGURA.dto.TareaRegisterDTO
import com.es.API_REST_SEGURA.dto.UsuarioDTO
import com.es.API_REST_SEGURA.dto.UsuarioRegisterDTO
import com.es.API_REST_SEGURA.model.Direccion
import com.es.API_REST_SEGURA.model.Estado
import com.es.API_REST_SEGURA.model.Tarea
import com.es.API_REST_SEGURA.model.Usuario

class DtoMapper {

    fun userDTOToEntity(usuario: UsuarioRegisterDTO): Usuario {
        val direccion = Direccion(
            usuario.calle,
            usuario.num,
            usuario.provincia,
            usuario.municipio,
            usuario.cp
        )
        val usuarioMapeado =
            Usuario(
                null,
                usuario.username.lowercase(),
                usuario.password,
                usuario.email,
                usuario.telefono,
                direccion
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

    fun tareaDTOToEntity(tarea: TareaRegisterDTO): Tarea {
        val tareaRegistrar = Tarea(
            null,
            tarea.username,
            tarea.titulo.lowercase(),
            tarea.descripcion,
            Estado.PENDIENTE
        )
        return tareaRegistrar
    }

    fun tareaEntityToDTO(tarea: Tarea): TareaDTO {
        val tareaDTO = TareaDTO(
            tarea.username,
            tarea.titulo,
            tarea.descripcion,
            tarea.estado
        )
        return tareaDTO
    }

}