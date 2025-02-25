package com.es.API_REST_SEGURA.error.exception

class ForbiddenException(message: String) : Exception("Forbidden exception (403). $message") {
}