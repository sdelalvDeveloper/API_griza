package com.es.API_REST_SEGURA.service

import com.es.API_REST_SEGURA.model.externalApi.DatosMunicipios
import com.es.API_REST_SEGURA.model.externalApi.DatosProvincias
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient

@Service
class ExternalApiService(private val webClient: WebClient.Builder) {
    @Value("\${API_KEY}")
    private lateinit var apiKey: String

    fun obtenerDatosProvincias(): DatosProvincias? {
        return webClient.build()
            .get()
            .uri("https://apiv1.geoapi.es/provincias?type=JSON&key=$apiKey")
            .retrieve()
            .bodyToMono(DatosProvincias::class.java)
            .block()
    }

    @Value("\${API_KEY}")
    fun obtenerDatosMunicipios(CPRO: String): DatosMunicipios? {
        return webClient.build()
            .get()
            .uri("https://apiv1.geoapi.es/municipios?CPRO=${CPRO}&type=JSON&key=$apiKey")
            .retrieve()
            .bodyToMono(DatosMunicipios::class.java)
            .block()
    }
}