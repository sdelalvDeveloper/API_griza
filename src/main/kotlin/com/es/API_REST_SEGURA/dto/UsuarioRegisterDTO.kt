package com.es.API_REST_SEGURA.dto

data class UsuarioRegisterDTO(
    val username: String,
    val email: String,
    val password: String,
    val passwordRepeat: String,
    val telefono: String
)
