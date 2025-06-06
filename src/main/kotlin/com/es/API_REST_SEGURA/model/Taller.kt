package com.es.API_REST_SEGURA.model

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer
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
    @JsonSerialize(using = ToStringSerializer::class)
    @JsonProperty("_id")
    val id: ObjectId? = null,
    val titulo: String,
    val descripcion: String,
    val fecha: Date,
    val plazas: Int = 6,
    var estado: EstadoTaller = EstadoTaller.DISPONIBLE,
    val reservas: List<Reserva> = listOf()
)