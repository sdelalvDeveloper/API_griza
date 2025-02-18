package com.es.API_REST_SEGURA.model

import org.bson.codecs.pojo.annotations.BsonId
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant
import java.util.*

enum class Estado(val estado: String) {
        PENDIENTE("pendiente"), COMPLETADA("completada")
}

@Document("Tarea")
data class Tarea(
    @BsonId
    val _id: String?,
    val usuario_id: String,  // email
    val titulo: String,
    val descripcion: String,
    val estado: Estado = Estado.PENDIENTE,
    val fecha_created: Date? = Date.from(Instant.now())
)