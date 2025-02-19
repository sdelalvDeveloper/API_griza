package com.es.API_REST_SEGURA.repository

import com.es.API_REST_SEGURA.model.Tarea

interface TareaCustomRepository {

    fun getTareas(username: String): List<Tarea>?
}