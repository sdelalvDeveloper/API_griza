package com.es.API_REST_SEGURA.dto

import org.bson.types.ObjectId

data class ReservaRegisterDTO(
    val username: String,
    val tallerID: ObjectId
)
