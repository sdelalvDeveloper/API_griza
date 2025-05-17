package com.es.API_REST_SEGURA.util

fun isValidPassword(password: String, passwordRepeat: String): Boolean {
    return password == passwordRepeat
}

fun isLongPassword(password: String): Boolean {
    return password.length == 6
}