package com.es.API_REST_SEGURA.controller

import com.es.API_REST_SEGURA.dto.LoginUsuarioDTO
import com.es.API_REST_SEGURA.dto.UsuarioDTO
import com.es.API_REST_SEGURA.dto.UsuarioRegisterDTO
import com.es.API_REST_SEGURA.dto.UsuarioUpdatePasswordDTO
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

        val role = authentication.authorities.firstOrNull()?.authority?.removePrefix("ROLE_") ?: "USER"

        return ResponseEntity(mapOf("token" to token, "role" to role), HttpStatus.OK)
    }

    @PostMapping("/register")
    fun insert(
        httpRequest: HttpServletRequest,
        @RequestBody usuarioRegisterDTO: UsuarioRegisterDTO
    ) : ResponseEntity<UsuarioDTO>?{

        val usuarioInsertado = usuarioService.insertUser(usuarioRegisterDTO)

        return ResponseEntity(usuarioInsertado, HttpStatus.CREATED)
    }

    @GetMapping("/{username}")
    fun getUser(httpRequest: HttpServletRequest, @PathVariable username: String): ResponseEntity<UsuarioDTO>? {
        val usuarioRegistrado = usuarioService.getUserByUsername(username)

        return ResponseEntity(usuarioRegistrado, HttpStatus.OK)
    }

    @GetMapping("/getAll")
    fun getAll(httpRequest: HttpServletRequest, authentication: Authentication): ResponseEntity<List<UsuarioDTO>>? {
        val listaUsuarios = usuarioService.getAll(authentication)
        return ResponseEntity(listaUsuarios, HttpStatus.OK)
    }

    @DeleteMapping("/delete/{username}/{password}")
    fun deleteUser(
        httpRequest: HttpServletRequest,
        @PathVariable username: String,
        @PathVariable password: String,
        authentication: Authentication): ResponseEntity<UsuarioDTO>? {
        val usuarioEliminado = usuarioService.deleteUserByUsername(username, password, authentication)

        return ResponseEntity(usuarioEliminado, HttpStatus.NO_CONTENT)
    }

    @PutMapping("/updatePassword")
    fun updatePassword(
        httpRequest: HttpServletRequest,
        @RequestBody usuarioUpdatePasswordDTO: UsuarioUpdatePasswordDTO,
        authentication: Authentication
    ) : ResponseEntity<Any>? {
        val usuarioActualizado = usuarioService.updatePassword(usuarioUpdatePasswordDTO, authentication)

        return ResponseEntity(usuarioActualizado, HttpStatus.OK)
    }

    @PutMapping("/activarBono/{username}")
    fun updateBonoUsuario(
        httpRequest: HttpServletRequest,
        @PathVariable username: String,
        authentication: Authentication
    ) : ResponseEntity<Any>? {
        val usuarioActualizado = usuarioService.activarBono(username, authentication)

        return ResponseEntity(usuarioActualizado, HttpStatus.OK)
    }

}