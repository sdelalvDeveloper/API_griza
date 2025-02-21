package com.es.API_REST_SEGURA.model.externalApi

data class DatosProvincias(
    val update_date: String,
    val size: Int,
    val data: List<Provincia>
) {
}