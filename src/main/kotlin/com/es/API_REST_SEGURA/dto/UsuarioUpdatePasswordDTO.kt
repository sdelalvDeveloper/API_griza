package com.es.API_REST_SEGURA.dto

data class UsuarioUpdatePasswordDTO(
    val username: String,
    val password: String,
    val newPassword: String
)
