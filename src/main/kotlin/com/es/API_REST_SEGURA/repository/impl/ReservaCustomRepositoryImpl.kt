package com.es.API_REST_SEGURA.repository.impl

import com.es.API_REST_SEGURA.model.Reserva
import com.es.API_REST_SEGURA.model.Taller
import com.es.API_REST_SEGURA.repository.ReservaCustomRepository
import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import com.mongodb.client.model.Filters
import org.bson.types.ObjectId
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.stereotype.Repository

@Repository
class ReservaCustomRepositoryImpl: ReservaCustomRepository {

    @Autowired
    private lateinit var mongoTemplate: MongoTemplate

    fun getConnection(): MongoCollection<Reserva> {
        val bd: MongoDatabase = mongoTemplate.db
        val coll = bd.getCollection("reservas", Reserva::class.java)
        return coll
    }
    override fun getReservaByUsername(username: String): List<Reserva> {
        val coll = getConnection()

        val filtro = Filters.eq("reservas.username", username)
        val reserva = coll.find(filtro)
        return reserva.toList()
    }

    override fun getAll(): List<Reserva> {
        val coll = getConnection()
        val reserva = coll.find()
        return reserva.toList()
    }

    override fun getReservaById(id: ObjectId): Reserva? {
        val coll = getConnection()

        val filtro = Filters.eq("id", id)
        val tarea = coll.find(filtro)
        return tarea.firstOrNull()
    }
}