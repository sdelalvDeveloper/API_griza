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

        val filtro = Filters.eq("username", username)
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

        val filtro = Filters.eq("_id", id)
        val reserva = coll.find(filtro)
        return reserva.firstOrNull()
    }

    override fun getReservaByIdTaller(idTaller: ObjectId): List<Reserva> {
        val coll = getConnection()

        val filtro = Filters.eq("tallerID", idTaller)
        val reserva = coll.find(filtro)
        return reserva.toList()
    }

    override fun updateReserva(id: ObjectId, nuevaReserva: Reserva): Boolean {
        val coll = getConnection()

        val filtro = Filters.eq("_id", id)
        val resultado = coll.replaceOne(filtro, nuevaReserva)

        return resultado.modifiedCount > 0
    }

    override fun deleteReservaById(id: ObjectId): Boolean {
        val coll = getConnection()

        val filtro = Filters.eq("_id", id)
        val resultado = coll.deleteOne(filtro)

        return resultado.deletedCount > 0
    }

}