package com.example.ms_agendamiento.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.example.ms_agendamiento.DTO.ReservaHoraDTO;
import com.example.ms_agendamiento.model.ReservaHora;
import com.example.ms_agendamiento.repository.ReservaHoraRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ReservaHoraService {

    @Autowired
    private ReservaHoraRepository reservaHoraRepository;

    @Autowired
    private WebClient.Builder webClientBuilder;

    // Agendar reserva
    public ReservaHoraDTO guardarReserva(ReservaHoraDTO reservaDTO) {
        validarReserva(reservaDTO);

        verificarRegistro("http://ms-pacientes/api/v1/pacientes/id/", reservaDTO.getPacienteId(), "paciente");

        verificarRegistro("http://ms-profesionales/api/v1/psicologos/", reservaDTO.getPsicologoId(), "psicologo");

        List<ReservaHora> citasConflictivas = reservaHoraRepository.findByPsicologoIdAndFechaHora(
            reservaDTO.getPsicologoId(),
            reservaDTO.getFechaHora()
        );

        if (!citasConflictivas.isEmpty()) {
            throw new RuntimeException("El psicólogo ya tiene una reserva para este horario.");
        }

        ReservaHora reservaNueva = new ReservaHora();
        reservaNueva.setFechaHora(reservaDTO.getFechaHora());
        reservaNueva.setPacienteId(reservaDTO.getPacienteId());
        reservaNueva.setPsicologoId(reservaDTO.getPsicologoId());

        if (reservaDTO.getEstado() == null) {
            reservaNueva.setEstado("PENDIENTE");
        } else {
            reservaNueva.setEstado(reservaDTO.getEstado());
        }
        ReservaHora reservaSave = reservaHoraRepository.save(reservaNueva);

        return convertirADTO(reservaSave);
    }

    // cancelar reserva
    public String eliminarReserva(Integer id) {
        ReservaHora reserva = reservaHoraRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada con el ID: " + id));

        reservaHoraRepository.delete(reserva);

        return "Reserva eliminada con éxito";
    }

    //actualizar reserva
    public ReservaHoraDTO actualizarReserva(Integer id, ReservaHoraDTO reservaActualizada) {
        validarReserva(reservaActualizada);

        ReservaHora reservaExistente = reservaHoraRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada con el ID: " + id));

        //cambiar fecha y hora
        if (!reservaExistente.getFechaHora().equals(reservaActualizada.getFechaHora())) {
            List<ReservaHora> choques = reservaHoraRepository.findByPsicologoIdAndFechaHora(
                reservaExistente.getPsicologoId(),
                reservaActualizada.getFechaHora()
            );

            if (!choques.isEmpty()) {
                throw new RuntimeException("No se puede cambiar la cita, el nuevo horario ya esta ocupado.");
            }
        }

        reservaExistente.setFechaHora(reservaActualizada.getFechaHora());
        
        if (reservaActualizada.getEstado() != null) {
            reservaExistente.setEstado(reservaActualizada.getEstado());
        }
        
        ReservaHora reservaSave = reservaHoraRepository.save(reservaExistente);
        return convertirADTO(reservaSave);
    }

    // Obtener historial de reservas de paciente
    public List<ReservaHoraDTO> buscarReservasPorPaciente(Integer pacId) {
        return reservaHoraRepository.findByPacienteId(pacId).stream()
                .map(this::convertirADTO)
                .toList();
    }

    // Obtener historial de reservas de psicologo
    public List<ReservaHoraDTO> buscarReservasPorPsicologo(Integer psicologoId) {
        return reservaHoraRepository.findByPsicologoId(psicologoId).stream()
                .map(this::convertirADTO)
                .toList();
    }

    //DTO
    public List<ReservaHoraDTO> obtenerReservas(){
        return reservaHoraRepository.findAll().stream()
                .map(this::convertirADTO)
                .toList();
    }

    public ReservaHoraDTO buscarReservaHoraPorId(Integer id){
        ReservaHora reserva = reservaHoraRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada con el ID: " + id));
        return convertirADTO(reserva);
    }

    //convertir a dto
    private ReservaHoraDTO convertirADTO(ReservaHora reserva) {
        
        ReservaHoraDTO dto = new ReservaHoraDTO();
        dto.setId(reserva.getId());
        dto.setFechaHora(reserva.getFechaHora());
        dto.setEstado(reserva.getEstado());
        
        if (reserva.getPacienteId() != null) {
            dto.setPacienteId(reserva.getPacienteId());
        }

        if (reserva.getPsicologoId() != null) {
            dto.setPsicologoId(reserva.getPsicologoId());
        }

        return dto;
    }

    // VALIDACION FECHA Y HORA
    private void validarReserva(ReservaHoraDTO reservaDTO) {
        if (reservaDTO.getPacienteId() == null || reservaDTO.getPsicologoId() == null) {
            throw new RuntimeException("La reserva debe incluir un paciente y un psicólogo.");
        }
        if (reservaDTO.getFechaHora() == null) {
            throw new RuntimeException("La fecha y hora son obligatorias.");
        }
    }

    // Permite eliminar una boleta de reserva realizada 
    public String eliminarBoletaDeReserva(Integer reservaId) {
        // Buscar la reserva
        ReservaHora reserva = reservaHoraRepository.findById(reservaId)
                .orElseThrow(() -> new RuntimeException("No se encontró la reserva con ID: " + reservaId));

        //Verificar si existe la boleta
        if (reserva.getBoleta() == null) {
            throw new RuntimeException("Esta reserva no tiene ninguna boleta asociada.");
        }

        //Eliminar la relacion
        reserva.setBoleta(null);
        reserva.setEstado("PENDIENTE");

        reservaHoraRepository.save(reserva);

        return "La boleta ha sido eliminada de la reserva " + reservaId;
    }

    // Verificar registro externo

    private void verificarRegistro(String url, Integer id, String nombreEnt) {
        try {
            webClientBuilder.build()
                .get()
                .uri(url + id)
                .retrieve()
                .bodyToMono(Object.class)
                .block();
        } catch (WebClientResponseException e) {
            // Id no existe
            if (e.getStatusCode().value() == 404) {
                throw new RuntimeException("Error! El " + nombreEnt + " con ID " + id + " no se encuentra registrado!");
            }

            // Error interno
            if (e.getStatusCode().is5xxServerError()) {
                throw new RuntimeException("Error interno en ms_" + nombreEnt + ", intente mas tarde.");
            }

            // Otros errores
            throw new RuntimeException("Error de comunicacion con ms_" + nombreEnt + ".");

        } catch (Exception e) {
            // Microservicio no responde
            throw new RuntimeException("Error! El microservicio ms_" + nombreEnt + " se encuentra apagado o no responde.");
        }
    }
    
}