package com.es.API_REST_SEGURA.repository

import com.es.API_REST_SEGURA.model.Usuario

interface UsuarioCustomRepository {

    fun updateByUsername(username: String, usuario: Usuario): Boolean

    fun getAllUsers(): List<Usuario>

}