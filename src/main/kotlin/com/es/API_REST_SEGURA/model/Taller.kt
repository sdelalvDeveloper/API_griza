package com.es.API_REST_SEGURA.model

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant
import java.util.*

enum class EstadoTaller() {
        DISPONIBLE, COMPLETO
}

@Document("talleres")
data class Taller(
    @BsonId
    val id: ObjectId? = null,
    val titulo: String,
    val descripcion: String,
    val fecha: Date,
    val plazas: Int = 6,
    var estado: EstadoTaller = EstadoTaller.DISPONIBLE,
    val reservas: List<Reserva> = listOf()
)