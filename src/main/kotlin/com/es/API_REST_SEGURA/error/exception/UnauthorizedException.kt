package com.es.API_REST_SEGURA.error.exception

class UnauthorizedException(message: String) : Exception("Not authorized exception (401). $message") {
}