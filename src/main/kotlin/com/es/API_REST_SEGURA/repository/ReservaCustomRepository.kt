package com.es.API_REST_SEGURA.repository

import com.es.API_REST_SEGURA.model.Reserva
import com.es.API_REST_SEGURA.model.Taller
import org.bson.types.ObjectId

interface ReservaCustomRepository {

    fun getReservaByUsername(username: String): List<Reserva>

    fun getAll(): List<Reserva>

    fun getReservaById(id: ObjectId): Reserva?
}