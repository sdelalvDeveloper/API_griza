package com.es.API_REST_SEGURA.model

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId
import org.jetbrains.annotations.NotNull
import org.springframework.data.mongodb.core.mapping.Document

@Document("usuarios")
data class Usuario(
    @BsonId
    val id : ObjectId? = null,
    val username: String,
    var password: String,
    val email: String,
    val telefono: String,
    val bono: Int = 0,
    val roles: String? = "USER"
)