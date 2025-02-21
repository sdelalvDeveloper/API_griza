package com.es.API_REST_SEGURA.model.externalApi

data class DatosMunicipios(
    val update_date: String,
    val size: Int,
    val data: List<Municipio>
) {
}

