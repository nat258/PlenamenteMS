package com.example.ms_pacientes.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.example.ms_pacientes.DTO.DiagnosticoDTO;
import com.example.ms_pacientes.model.Diagnostico;
import com.example.ms_pacientes.repository.DiagnosticoRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class DiagnosticoService {

    final DiagnosticoRepository diagnosticoRepository;


    DiagnosticoService(DiagnosticoRepository diagnosticoRepository) {
        this.diagnosticoRepository = diagnosticoRepository;
    }


    //OBTENER TODOS LOS DIAGNOSTICOS
    public List<DiagnosticoDTO> obtenerTodos() {
        List<DiagnosticoDTO> listaDTOs = new ArrayList<>();
        List<Diagnostico> DiagnosticoR = diagnosticoRepository.findAll();
        for (Diagnostico diag : DiagnosticoR) {
            listaDTOs.add(convertirADTO(diag));
        }
        return listaDTOs;
    }

    //GUARDAR UN DIAGNOSTICO
    public DiagnosticoDTO guardarDiagnostico(Diagnostico diag) {
        Optional<Diagnostico> diagnostico = diagnosticoRepository.findByNombreIgnoreCase(diag.getNombre());
        if(diagnostico.isPresent()) {
            throw new RuntimeException("El nombre de diagnostico ya se encuentra registrado!");
        }
        Diagnostico guardado = diagnosticoRepository.save(diag);
        return convertirADTO(guardado);
    }

    //ELIMINAR UN IDAGNOSTICO POR ID
    public String eliminarDiagnostico(Integer id) {
        try {
            Diagnostico diagnostico = diagnosticoRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("El ID " + id + "  no se encuentra registrado!"));

            diagnosticoRepository.delete(diagnostico);
            return "El diagnostico '" + diagnostico.getNombre() + "' ha sido eliminado.";

        } catch (DataIntegrityViolationException e) {
            return "Error: No se puede eliminar el diagnostico porque esta asociado a un historial de un paciente!";
        } catch (Exception e) {
            return e.getMessage();
        }
        
    }

    //ACTUALIZAR UN DIAGNOSTICO POR ID
    public DiagnosticoDTO actualizarDiagnostico(Integer id, Diagnostico diagnostico) {
        Diagnostico diag = diagnosticoRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("El diagnostico ingresado no existe en los registros!"));

        if(diagnostico.getNombre() != null && !diagnostico.getNombre().trim().isEmpty()) {

            if(!diag.getNombre().equalsIgnoreCase(diagnostico.getNombre())) {
                
                if(diagnosticoRepository.findByNombreIgnoreCase(diagnostico.getNombre()).isPresent()) {
                    throw new RuntimeException("El nombre de diagnostico " + diagnostico.getNombre() + " ya se encuentra registrado!");
                }
                diag.setNombre(diagnostico.getNombre());
            }
        }

        if(diagnostico.getDescripcion() != null) {
            diag.setDescripcion(diagnostico.getDescripcion());
        }
        Diagnostico guardado = diagnosticoRepository.save(diag);
        return convertirADTO(guardado);
    }

    //BUSCAR UN DIAGNOSTICO POR ID
    public DiagnosticoDTO buscarPorId(Integer id) {
        Diagnostico diagnostico = diagnosticoRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("¡Diagnostico no encontrado!"));
        return convertirADTO(diagnostico);
    }

    //BUSCAR UN DIAGNOSTICO POR RUT DEL PACIENTE
    public DiagnosticoDTO buscarPorRut(String rut) {
    return diagnosticoRepository.findByPacienteRut(rut)
            .map(this::convertirADTO)
            .orElseThrow(() -> new RuntimeException("No se encontró ningún diagnóstico asociado al RUT: " + rut));
    }


    private DiagnosticoDTO convertirADTO(Diagnostico diagnostico) {
        DiagnosticoDTO dto = new DiagnosticoDTO();
        dto.setId(diagnostico.getId());
        dto.setNombre(diagnostico.getNombre());
        dto.setDescripcion(diagnostico.getDescripcion());
        return dto;
    }

}
