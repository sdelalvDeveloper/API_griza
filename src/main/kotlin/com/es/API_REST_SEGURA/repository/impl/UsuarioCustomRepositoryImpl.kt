package com.es.API_REST_SEGURA.repository.impl


import com.es.API_REST_SEGURA.model.Usuario
import com.es.API_REST_SEGURA.repository.UsuarioCustomRepository
import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import com.mongodb.client.model.Filters
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.stereotype.Repository

@Repository
class UsuarioCustomRepositoryImpl: UsuarioCustomRepository {

    @Autowired
    private lateinit var mongoTemplate: MongoTemplate

    fun getConnection(): MongoCollection<Usuario> {
        val bd: MongoDatabase = mongoTemplate.db
        val coll = bd.getCollection("usuarios", Usuario::class.java)
        return coll
    }

    override fun updateByUsername(username: String, usuario: Usuario): Boolean {
        val coll = getConnection()

        val filtro = Filters.eq("username", username)
        val resultado = coll.replaceOne(filtro, usuario)
        return resultado.modifiedCount > 0
    }

    override fun getAllUsers(): List<Usuario> {
        val coll = getConnection()
        val usuarios = coll.find()
        return usuarios.toList()
    }

}