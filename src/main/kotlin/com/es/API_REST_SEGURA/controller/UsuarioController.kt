package com.es.API_REST_SEGURA.controller

import com.es.API_REST_SEGURA.dto.LoginUsuarioDTO
import com.es.API_REST_SEGURA.dto.UsuarioRegisterDTO
import com.es.API_REST_SEGURA.error.exception.UnauthorizedException
import com.es.API_REST_SEGURA.model.Usuario
import com.es.API_REST_SEGURA.service.TokenService
import com.es.API_REST_SEGURA.service.UsuarioService
import jakarta.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/usuarios")
class UsuarioController {

    @Autowired
    private lateinit var authenticationManager: AuthenticationManager

    @Autowired
    private lateinit var tokenService: TokenService

    @Autowired
    private lateinit var usuarioService: UsuarioService

    @PostMapping("/login")
    fun login(@RequestBody usuario: LoginUsuarioDTO) : ResponseEntity<Any>? {

        val authentication: Authentication
        try {
            authentication = authenticationManager.authenticate(UsernamePasswordAuthenticationToken(usuario.username, usuario.password))
        } catch (e: AuthenticationException) {
            throw UnauthorizedException("Credenciales incorrectas")
        }

        // SI PASAMOS LA AUTENTICACIÓN, SIGNIFICA QUE ESTAMOS BIEN AUTENTICADOS
        // PASAMOS A GENERAR EL TOKEN
        val token = tokenService.generarToken(authentication)

        return ResponseEntity(mapOf("token" to token), HttpStatus.OK)
    }

    @PostMapping("/register")
    fun insert(
        httpRequest: HttpServletRequest,
        @RequestBody usuarioRegisterDTO: UsuarioRegisterDTO
    ) : ResponseEntity<Usuario>?{

        val usuarioInsertado = usuarioService.insertUser(usuarioRegisterDTO)

        return ResponseEntity(usuarioInsertado, HttpStatus.CREATED)
    }

    @GetMapping("/{username}")
    fun getUser(httpRequest: HttpServletRequest, @PathVariable username: String): ResponseEntity<Any>? {
        val usuarioRegistrado = usuarioService.loadUserByUsername(username)

        return ResponseEntity(usuarioRegistrado, HttpStatus.OK)
    }

    @DeleteMapping("/{username}")
    fun deleteUser(httpRequest: HttpServletRequest, @PathVariable username: String, authentication: Authentication): ResponseEntity<Any>? {
        val usuarioEliminado = usuarioService.deleteUserByUsername(username, authentication)

        return ResponseEntity(usuarioEliminado, HttpStatus.NO_CONTENT)
    }



}