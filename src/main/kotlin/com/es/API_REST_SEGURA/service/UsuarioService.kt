package com.es.API_REST_SEGURA.service

import com.es.API_REST_SEGURA.dto.UsuarioDTO
import com.es.API_REST_SEGURA.dto.UsuarioRegisterDTO
import com.es.API_REST_SEGURA.dto.UsuarioUpdatePasswordDTO
import com.es.API_REST_SEGURA.error.exception.BadRequestException
import com.es.API_REST_SEGURA.error.exception.NotFoundException
import com.es.API_REST_SEGURA.error.exception.UnauthorizedException
import com.es.API_REST_SEGURA.model.Usuario
import com.es.API_REST_SEGURA.repository.UsuarioRepository
import com.es.API_REST_SEGURA.util.*
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

    val dtoMapper = DtoMapper()

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

    fun insertUser(usuarioRegisterDTO: UsuarioRegisterDTO): UsuarioDTO {
        val dtoMapper = DtoMapper()

        val usuarioExist = usuarioRegisterDTO.let { usuarioRepository.findByUsername(it.username) }

        if (usuarioExist.isPresent){
            throw BadRequestException("Usuario ${usuarioRegisterDTO.username} ya existe")
        }

        if (!isValidPassword(usuarioRegisterDTO.password, usuarioRegisterDTO.passwordRepeat)) {
            throw BadRequestException("La contraseña no coincide")
        }

        if (!isLongPassword(usuarioRegisterDTO.password)) {
            throw BadRequestException("La contraseña debe tener 6 caracteres")
        }

        if (!isValidEmail(usuarioRegisterDTO.email)) {
            throw BadRequestException("Email inválido")
        }

        if(!isValidPhoneNumber(usuarioRegisterDTO.telefono)){
            throw BadRequestException("Teléfono inválido")
        }

        val usuario = dtoMapper.userDTOToEntity(usuarioRegisterDTO)

        usuario.password = passwordEncoder.encode(usuario.password)
        usuarioRepository.save(usuario)
        val usuarioResult = dtoMapper.userEntityToDTO(usuario)
        return usuarioResult
    }

    fun getUserByUsername(username: String): UsuarioDTO {
        val dtoMapper = DtoMapper()
        val usuarioRegistrado: Usuario = usuarioRepository
            .findByUsername(username)
            .orElseThrow { NotFoundException("$username no existe.") }

        val usuarioResult = dtoMapper.userEntityToDTO(usuarioRegistrado)
        return usuarioResult
    }

    fun getUserEntity(username: String): Usuario {
        val usuarioRegistrado: Usuario = usuarioRepository
            .findByUsername(username)
            .orElseThrow { NotFoundException("$username no existe.") }

        return usuarioRegistrado
    }

    fun deleteUserByUsername(username: String, password: String, authentication: Authentication): UsuarioDTO {
        val usuarioRegistrado: Usuario = usuarioRepository
            .findByUsername(username)
            .orElseThrow { NotFoundException("$username no existe.") }
        if (
            (authentication.name == username && passwordEncoder.matches(password, usuarioRegistrado.password))
            || authentication.authorities.any { it.authority == "ROLE_ADMIN" }
            ) {
            usuarioRepository.delete(usuarioRegistrado)
        } else {
            throw UnauthorizedException("No tiene permiso para eliminar otro usuario")
        }
        val usuarioEliminado = dtoMapper.userEntityToDTO(usuarioRegistrado)
        return usuarioEliminado
    }

    fun updateUser(username: String, usuario: Usuario): Boolean {
        return usuarioRepository.updateByUsername(usuario.username, usuario)
    }

    fun updatePassword(usuario: UsuarioUpdatePasswordDTO, authentication: Authentication): Boolean {
        val usuarioExiste = getUserEntity(usuario.username)

        if (usuario.password != usuario.passwordRepeat) {
            throw BadRequestException("Las contraseñas deben coincidir.")
        }

        val password = passwordEncoder.encode(usuario.password)

        if (passwordEncoder.matches(password, usuarioExiste.password)) {
            throw UnauthorizedException("La contraseña introducida no es correcta")
        }

        return usuarioRepository.updateByUsername(usuario.username, usuarioExiste)
    }

}