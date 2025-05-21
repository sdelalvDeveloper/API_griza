package com.es.API_REST_SEGURA.util

fun isValidPhoneNumber(phone: String): Boolean {
    val cleaned = phone.trim().replace(" ", "").replace("-", "")
    return cleaned.matches(Regex("^\\d{9}$"))
}