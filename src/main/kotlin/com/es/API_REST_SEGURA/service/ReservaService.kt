package com.es.API_REST_SEGURA.service

import com.es.API_REST_SEGURA.dto.ReservaDTO
import com.es.API_REST_SEGURA.dto.ReservaFullDTO
import com.es.API_REST_SEGURA.dto.ReservaRegisterDTO
import com.es.API_REST_SEGURA.error.exception.BadRequestException
import com.es.API_REST_SEGURA.error.exception.NotFoundException
import com.es.API_REST_SEGURA.model.EstadoReserva
import com.es.API_REST_SEGURA.repository.ReservaRepository
import com.es.API_REST_SEGURA.util.DtoMapper
import org.bson.types.ObjectId
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service

@Service
class ReservaService {

    @Autowired
    private lateinit var reservaRepository: ReservaRepository

    @Autowired
    private lateinit var tallerService: TallerService

    @Autowired
    private lateinit var usuarioService: UsuarioService

    private val dtoMapper = DtoMapper()

    fun getReservaByUsername(username: String, authentication: Authentication): List<ReservaFullDTO> {
        val reservas =  if (authentication.authorities.any { it.authority == "ROLE_ADMIN" }) {
            reservaRepository.getAll()
        } else {
            reservaRepository.getReservaByUsername(username.lowercase())
        }

        return reservas.map { reserva ->
            // Obtener taller para incluir su título
            val taller = tallerService.getTallerById(reserva.tallerID)
            taller.let {
                ReservaFullDTO(
                    id = reserva.id.toString(),
                    username = reserva.username,
                    tituloTaller = it.titulo,
                    tallerID = it.id.toString(),
                    estado = reserva.estado,
                    fechaTaller = it.fecha
                )
            }
        }.sortedBy { it.fechaTaller }
    }

    fun getAll(): List<ReservaDTO> {
        val reservas = reservaRepository.getAll().map { reserva ->
            dtoMapper.reservaEntityToDTO(reserva)
        }
        return reservas
    }

    fun insertReserva(reservaDTO: ReservaRegisterDTO, authentication: Authentication): ReservaDTO? {
        // Verifica que el usuario esté autenticado (ya que Authentication está presente)
        val username = authentication.name

        // Mapea DTO a entidad y asegura que el username venga del token para evitar spoofing
        val reservaEntity = dtoMapper.reservaDTOToEntity(reservaDTO).copy(username = username)

        val usuario = usuarioService.getUserEntity(username)
        if (usuario.bono == 0) {
            throw BadRequestException("Bono insuficiente")
        }

        // Busca el taller para validar plazas disponibles antes de guardar
        val tallerEntity = tallerService.getTallerById(reservaEntity.tallerID)

        // Validar que aún haya plazas disponibles
        if (tallerEntity.plazas <= 0) {
            throw RuntimeException("No hay plazas disponibles en este taller.")
        }

        // Restar uno al bono
        val usuarioActualizado = usuario.copy(bono = usuario.bono - 1)
        usuarioService.updateUser(username, usuarioActualizado)

        // Guarda la reserva
        val reservaGuardada = reservaRepository.save(reservaEntity)

        // Actualiza la lista de reservas del taller
        val reservasActualizadas = tallerEntity.reservas.toMutableList().apply {
            add(reservaGuardada)
        }

        // Calcular nuevas plazas y estado
        var plazasRestantes = (tallerEntity.plazas - 1).coerceAtLeast(0)
        if (tallerEntity.reservas.size == 6) {
            plazasRestantes = 0
        }

        // Actualiza estado del taller
        val estadoActualizado = tallerService.cambiarEstadoTaller(plazasRestantes)

        // Guarda los cambios en el taller
        val tallerActualizado = tallerEntity.copy(
            reservas = reservasActualizadas,
            plazas = plazasRestantes,
            estado = estadoActualizado
        )

        tallerEntity.id?.let { tallerService.updateTaller(it, tallerActualizado) }

        return dtoMapper.reservaEntityToDTO(reservaGuardada)
    }


    fun deleteReservaById(id: ObjectId, tallerID: ObjectId, authentication: Authentication) {
        val reserva = reservaRepository.getReservaById(id)
            ?: throw NotFoundException("No se ha encontrado ninguna reserva con este id: $id")

        // Cambiar estado a CANCELADA
        val reservaCancelada = reserva.copy(estado = EstadoReserva.CANCELADA)
        reservaRepository.save(reservaCancelada)

        // Obtener el taller relacionado
        val tallerEntity = tallerService.getTallerById(tallerID)


        // Eliminar la reserva de la lista del taller (por ID)
        val reservasActualizadas = tallerEntity.reservas
            .filter { it.id != id }  // Filtramos la reserva cancelada

        // Sumar una plaza (sin superar el total original)
        var plazasActualizadas = (tallerEntity.plazas + 1).coerceAtMost(6)  // 6 es el límite, o usa una constante si es dinámico
        if (tallerEntity.reservas.isEmpty()) {
            plazasActualizadas = 6
        }

        // Actualizar estado
        val nuevoEstado = tallerService.cambiarEstadoTaller(plazasActualizadas)

        val tallerActualizado = tallerEntity.copy(
            reservas = reservasActualizadas,
            estado = nuevoEstado,
            plazas = plazasActualizadas
        )

        // Guardar el taller actualizado
        tallerEntity.id?.let { tallerService.updateTaller(it, tallerActualizado) }
        reserva.id?.let { reservaRepository.deleteReservaById(it) }

        val usuario = usuarioService.getUserEntity(authentication.name)

        // Sumar uno al bono
        val usuarioActualizado = usuario.copy(bono = usuario.bono + 1)
        usuarioService.updateUser(usuario.username, usuarioActualizado)
    }

    fun deleteAll(username: String, authentication: Authentication) {
        val reservas = getReservaByUsername(username, authentication)
        reservas.forEach { deleteReservaById(ObjectId(it.id), ObjectId(it.tallerID), authentication) }
    }

}