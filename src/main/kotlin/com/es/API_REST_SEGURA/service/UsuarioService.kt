package com.es.API_REST_SEGURA.service

import com.es.API_REST_SEGURA.dto.UsuarioRegisterDTO
import com.es.API_REST_SEGURA.error.exception.BadRequestException
import com.es.API_REST_SEGURA.error.exception.NotFoundException
import com.es.API_REST_SEGURA.error.exception.UnauthorizedException
import com.es.API_REST_SEGURA.model.Usuario
import com.es.API_REST_SEGURA.repository.UsuarioRepository
import com.es.API_REST_SEGURA.util.DtoMapper
import com.es.API_REST_SEGURA.util.isLongPassword
import com.es.API_REST_SEGURA.util.isValidEmail
import com.es.API_REST_SEGURA.util.isValidPassword
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UsuarioService() : UserDetailsService {

    @Autowired
    private lateinit var usuarioRepository: UsuarioRepository
    @Autowired
    private lateinit var passwordEncoder: PasswordEncoder

    override fun loadUserByUsername(username: String?): UserDetails {
        val usuario: Usuario = usuarioRepository
            .findByUsername(username!!)
            .orElseThrow() {
                NotFoundException("$username no existe.")
            }
        val roles = usuario.roles?.split("," )?.map { SimpleGrantedAuthority ("ROLE_$it" ) }?.toList() ?: listOf()

        return User.builder()
            .username(usuario.username)
            .password(usuario.password)
            .authorities(roles)
            .build( )
    }

    fun insertUser(usuarioRegisterDTO: UsuarioRegisterDTO): Usuario? {
        val dtoMapper = DtoMapper()

        val usuarioExist = usuarioRegisterDTO.let { usuarioRepository.findByUsername(it.username) }

        if (usuarioExist.isPresent){
            throw BadRequestException("Usuario ${usuarioRegisterDTO.username} ya existe")
        }

        if (!isValidPassword(usuarioRegisterDTO.password, usuarioRegisterDTO.passwordRepeat)) {
            throw BadRequestException("La contraseña no coincide")
        }

        if (!isLongPassword(usuarioRegisterDTO.password)) {
            throw BadRequestException("La contraseña debe tener mínimo 6 caracteres")
        }

        if (!isValidEmail(usuarioRegisterDTO.email)) {
            throw BadRequestException("Email inválido")
        }

        val usuario = dtoMapper.userDTOToEntity(usuarioRegisterDTO)

        usuario.password = passwordEncoder.encode(usuario.password)
        usuarioRepository.save(usuario)
        return usuario
    }

    fun deleteUserByUsername(username: String, authentication: Authentication): ResponseEntity<Usuario> {
        val usuarioRegistrado: Usuario = usuarioRepository
            .findByUsername(username)
            .orElseThrow { NotFoundException("$username no existe.") }
        if (authentication.name == username || authentication.authorities.any { it.authority == "ROLE_ADMIN" }) {
            usuarioRepository.delete(usuarioRegistrado)
        } else {
            throw UnauthorizedException("No tiene permiso para eliminar otro usuario")
        }
        return ResponseEntity(usuarioRegistrado, HttpStatus.OK)
    }

}