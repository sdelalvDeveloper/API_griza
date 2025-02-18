package com.es.API_REST_SEGURA

import com.es.API_REST_SEGURA.security.RSAKeysProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableConfigurationProperties(RSAKeysProperties::class)
class ApiRestSeguraApplication

fun main(args: Array<String>) {
	runApplication<ApiRestSeguraApplication>(*args)
}
