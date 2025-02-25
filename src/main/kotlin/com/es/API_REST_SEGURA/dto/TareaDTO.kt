package com.es.API_REST_SEGURA.dto

import com.es.API_REST_SEGURA.model.Estado

data class TareaDTO(
    val username: String,
    val titulo: String,
    val descripcion: String,
    val estado: Estado
)
