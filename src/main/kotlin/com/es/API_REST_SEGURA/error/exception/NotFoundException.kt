package com.es.API_REST_SEGURA.error.exception

class NotFoundException(message: String) : Exception("Not found exception (404). $message") {
}