package com.example.ms_pacientes.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ms_pacientes.DTO.PacienteDTO;
import com.example.ms_pacientes.model.Paciente;
import com.example.ms_pacientes.repository.PacienteRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class PacienteService {
    
    @Autowired
    private PacienteRepository pacienteRepository;


    public String eliminarPacientePorRut(String rut) {
        try {
            Paciente paciente = pacienteRepository.findByRut(rut)
                    .stream()
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Paciente no encontrado con el RUT: " + rut));
            pacienteRepository.delete(paciente);
            return "Paciente eliminado con éxito";
        } catch (Exception e) {
            return e.getMessage();
        }
    }


    public PacienteDTO guardarPaciente(Paciente paciente) {
        validarPaciente(paciente);
        
        Optional<Paciente> pacienteExistente = pacienteRepository.findByRut(paciente.getRut());

        if (!pacienteExistente.isEmpty()) {
            throw new RuntimeException("El RUT " + paciente.getRut()+ " ya se encuentra registrado en el sistema.");
        }
        Paciente pacienteGuardado = pacienteRepository.save(paciente);
        return convertirADTO(pacienteGuardado);
    }


    public Paciente actualizarPacientePorRut(String rut, Paciente datosNuevos) {
        Paciente pacienteExistente = pacienteRepository.findByRut(rut)
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No se puede actualizar: RUT " + rut + " no encontrado."));

        validarPaciente(datosNuevos);

        pacienteExistente.setRut(datosNuevos.getRut());
        pacienteExistente.setPNombre(datosNuevos.getPNombre());
        pacienteExistente.setPApellido(datosNuevos.getPApellido());
        pacienteExistente.setCorreo(datosNuevos.getCorreo());
        pacienteExistente.setPrevision(datosNuevos.getPrevision());

        return pacienteRepository.save(pacienteExistente);
    }


    //DTO
    public List<PacienteDTO> obtenerTodosLosPacientes(){
            return pacienteRepository.findAll().stream()
                    .map(this::convertirADTO)
                    .toList();
        }


    public PacienteDTO buscarPorID(Integer id) {
        Paciente paciente = pacienteRepository.findById(id)
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado con el Id: " + id));
            return convertirADTO(paciente);
    }

    public PacienteDTO buscarPorRut(String rut) {
        Paciente paciente = pacienteRepository.findByRut(rut)
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado con el RUT: " + rut));
        return convertirADTO(paciente);
    }


    private PacienteDTO convertirADTO(Paciente paciente) {
        PacienteDTO dto = new PacienteDTO();
        dto.setId(paciente.getId());
        dto.setRut(paciente.getRut());
        dto.setP_nombre(paciente.getPNombre());
        dto.setP_apellido(paciente.getPApellido());
        dto.setCorreo(paciente.getCorreo());
        
        if (paciente.getPrevision() != null) {
            dto.setPrevisionId(paciente.getPrevision().getId());
            dto.setPrevisionNombre(paciente.getPrevision().getTipo());
        }
        return dto;
    }



    //validaciones
    private void validarPaciente(Paciente paciente) {
        // Validación para String (RUT)
        if (paciente.getRut() == null || paciente.getRut().isEmpty()) {
            throw new RuntimeException("El RUT es obligatorio y debe ser un número válido.");
        }
        // Validación para String (Nombre)
        if (paciente.getPNombre() == null || paciente.getPNombre().trim().isEmpty()) {
            throw new RuntimeException("El nombre es obligatorio.");
        }
        // Validación para String (Correo)
        if (paciente.getCorreo() == null || !paciente.getCorreo().contains("@")) {
            throw new RuntimeException("El formato del correo electrónico no es válido.");
        }
    }

}
