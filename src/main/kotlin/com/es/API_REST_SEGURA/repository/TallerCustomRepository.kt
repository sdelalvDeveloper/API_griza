package com.es.API_REST_SEGURA.repository

import com.es.API_REST_SEGURA.model.Taller
import org.bson.types.ObjectId

interface TallerCustomRepository {

    fun getTalleresByUsername(username: String): List<Taller>

    fun getAll(): List<Taller>

    fun getTallerById(id: ObjectId): Taller?

    fun updateTaller(id: ObjectId, nuevoTaller: Taller): Boolean
}