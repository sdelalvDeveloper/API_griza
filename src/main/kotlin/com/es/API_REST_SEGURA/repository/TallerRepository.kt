package com.es.API_REST_SEGURA.repository

import com.es.API_REST_SEGURA.model.Taller
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface TallerRepository : MongoRepository<Taller, String>, TallerCustomRepository {

}