package com.es.API_REST_SEGURA.repository

import com.es.API_REST_SEGURA.model.Usuario
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface UsuarioRepository : MongoRepository<Usuario, String>, UsuarioCustomRepository {

    fun findByUsername(username: String): Optional<Usuario>


}