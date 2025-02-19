package com.es.API_REST_SEGURA.service

import com.es.API_REST_SEGURA.dto.UsuarioRegisterDTO
import com.es.API_REST_SEGURA.error.exception.BadRequestException
import com.es.API_REST_SEGURA.error.exception.NotFoundException
import com.es.API_REST_SEGURA.model.Usuario
import com.es.API_REST_SEGURA.repository.UsuarioRepository
import com.es.API_REST_SEGURA.util.DtoMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
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

        return User.builder()
            .username(usuario.username)
            .password(usuario.password)
            .roles(usuario.roles)
            .build( )
    }

    fun insertUser(usuarioInsertadoDTO: UsuarioRegisterDTO) : Usuario? {
        val dtoMapper = DtoMapper()

        val usuarioExist = usuarioInsertadoDTO.let { usuarioRepository.findByUsername(it.username) }

        /*
        val datosProvincias = apiService.obtenerDatosProvincias()

        if (datosProvincias?.data != null) {
            datosProvincias.data.stream().filter {
                it.PRO == (usuario.direccion?.provincia?.uppercase() ?: "")
            }.findFirst().orElseThrow {
                NotFoundException("Provincia ${usuario.direccion?.provincia?.uppercase()} no válida")
            }
        }

         */

        if (usuarioInsertadoDTO.password != usuarioInsertadoDTO.passwordRepeat) {
            throw BadRequestException("La contraseña no coincide")
        }

        if (usuarioExist.isPresent){
            throw BadRequestException("Usuario ${usuarioInsertadoDTO.username} ya existe")
        } else {
            val usuario = dtoMapper.userDTOToEntity(usuarioInsertadoDTO)
            usuario.password = passwordEncoder.encode(usuario.password)
            usuarioRepository.save(usuario)
            return usuario
        }
    }

    fun deleteUserByUsername(username: String): ResponseEntity<Usuario>{
        val usuarioRegistrado: Usuario = usuarioRepository
            .findByUsername(username)
            .orElseThrow() { NotFoundException("$username no existe.") }

        usuarioRepository.delete(usuarioRegistrado)

        return ResponseEntity(usuarioRegistrado, HttpStatus.OK)
    }
}