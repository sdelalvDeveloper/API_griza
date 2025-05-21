package com.es.API_REST_SEGURA.repository.impl

import com.es.API_REST_SEGURA.model.Reserva
import com.es.API_REST_SEGURA.model.Taller
import com.es.API_REST_SEGURA.repository.TallerCustomRepository
import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import com.mongodb.client.model.Filters
import org.bson.types.ObjectId
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.stereotype.Repository

@Repository
class TallerCustomRepositoryImpl: TallerCustomRepository {

    @Autowired
    private lateinit var mongoTemplate: MongoTemplate

    fun getConnection(): MongoCollection<Taller> {
        val bd: MongoDatabase = mongoTemplate.db
        val coll = bd.getCollection("talleres", Taller::class.java)
        return coll
    }
    override fun getTalleresByUsername(username: String): List<Taller> {
        val coll = getConnection()

        val filtro = Filters.eq("reservas.username", username)
        val taller = coll.find(filtro)
        return taller.toList()
    }

    override fun getAll(): List<Taller> {
        val coll = getConnection()
        val taller = coll.find()
        return taller.toList()
    }

    override fun getTallerById(id: ObjectId): Taller? {
        val coll = getConnection()

        val filtro = Filters.eq("_id", id)
        val tarea = coll.find(filtro)
        return tarea.firstOrNull()
    }

    override fun updateTaller(id: ObjectId, nuevoTaller: Taller): Boolean {
        val coll = getConnection()

        val filtro = Filters.eq("_id", id)
        val resultado = coll.replaceOne(filtro, nuevoTaller)

        return resultado.modifiedCount > 0
    }

}