package com.es.API_REST_SEGURA.repository.impl

import com.es.API_REST_SEGURA.model.Tarea
import com.es.API_REST_SEGURA.repository.TareaCustomRepository
import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import com.mongodb.client.model.Filters
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.stereotype.Repository

@Repository
class TareaCustomRepositoryImpl: TareaCustomRepository {

    @Autowired
    private lateinit var mongoTemplate: MongoTemplate

    fun getConnection(): MongoCollection<Tarea> {
        val bd: MongoDatabase = mongoTemplate.db
        val coll = bd.getCollection("Tarea", Tarea::class.java)
        return coll
    }
    override fun getTareas(username: String): List<Tarea> {
        val coll = getConnection()

        val filtro = Filters.eq("username", username)
        val tareas = coll.find(filtro)
        return tareas.toList()
    }

    override fun getAll(): List<Tarea> {
        val coll = getConnection()
        val tareas = coll.find()
        return tareas.toList()
    }

    override fun getTareaByTitulo(titulo: String): Tarea? {
        val coll = getConnection()

        val filtro = Filters.eq("titulo", titulo)
        val tarea = coll.find(filtro)
        return tarea.firstOrNull()
    }


}