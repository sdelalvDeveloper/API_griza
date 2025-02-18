package com.es.API_REST_SEGURA.error.exception

class BadRequestException(message: String) : Exception("Bad request exception (400). $message") {
}