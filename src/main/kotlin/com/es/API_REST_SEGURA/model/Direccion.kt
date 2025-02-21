package com.es.API_REST_SEGURA.model

data class Direccion(
    val calle: String,
    val num: String,
    val provincia: String,
    val municipio: String,
    val cp: String
)