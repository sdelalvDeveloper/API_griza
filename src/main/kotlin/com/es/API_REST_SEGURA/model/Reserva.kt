package com.es.API_REST_SEGURA.model

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant
import java.util.*

enum class EstadoReserva {
    ACTIVA, CANCELADA
}

@Document("reservas")
data class Reserva(
    @BsonId
    val id: ObjectId? = null,
    val username: String,
    val tallerID: ObjectId,
    var estado: EstadoReserva = EstadoReserva.ACTIVA,
    val fecha: Date = Date.from(Instant.now())
    )
