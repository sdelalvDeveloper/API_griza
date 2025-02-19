package com.es.API_REST_SEGURA.repository.impl

import com.es.API_REST_SEGURA.model.Tarea
import com.es.API_REST_SEGURA.repository.TareaCustomRepository
import com.mongodb.client.MongoDatabase
import com.mongodb.client.model.Filters
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.stereotype.Repository

@Repository
class TareaCustomRepositoryImpl: TareaCustomRepository {

    @Autowired
    private lateinit var mongoTemplate: MongoTemplate
    override fun getTareas(username: String): List<Tarea>? {
        val bd: MongoDatabase = mongoTemplate.db
        val coll = bd.getCollection("Tarea", Tarea::class.java)

        val filtro = Filters.eq("username", username)
        val tareas = coll.find(filtro)
        return tareas.toList()
    }


}