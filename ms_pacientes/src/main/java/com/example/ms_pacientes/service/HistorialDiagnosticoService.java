package com.example.ms_pacientes.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.example.ms_pacientes.DTO.HistorialDiagnosticoDTO;
import com.example.ms_pacientes.model.HistorialDiagnostico;
import com.example.ms_pacientes.repository.HistorialDiagnosticoRepository;
import com.example.ms_pacientes.repository.PacienteRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class HistorialDiagnosticoService {

    @Autowired
    private HistorialDiagnosticoRepository historialDiagnosticoRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    public List<HistorialDiagnosticoDTO> obtenerTodos() {
        List<HistorialDiagnosticoDTO> listaDTOs = new ArrayList<>();
        List<HistorialDiagnostico> HistorialDiagR = historialDiagnosticoRepository.findAll();
        for (HistorialDiagnostico hist : HistorialDiagR) {
            listaDTOs.add(convertirADTO(hist));
        }
        return listaDTOs;
    }

    public HistorialDiagnosticoDTO guardarHistorial(HistorialDiagnostico hist) {
        if(hist.getPaciente() == null || hist.getPaciente().getId() == null) {
            throw new RuntimeException("Debe asignar un paciente valido para registrar el historial!");
        }
        if(!pacienteRepository.existsById(hist.getPaciente().getId())) {
            throw new RuntimeException("El paciente ingresado no se encuentra registrado!");
        }
        if(hist.getDiagnostico() == null || hist.getDiagnostico().getId() == null) {
            throw new RuntimeException("Debe asignar un diagnostico valido para registrar el historial!");
        }
        if(!historialDiagnosticoRepository.existsById(hist.getDiagnostico().getId())) {
            throw new RuntimeException("El diagnostico ingresado no se encuentra registrado!");
        }

        HistorialDiagnostico guardado = historialDiagnosticoRepository.save(hist);
        return convertirADTO(guardado);
    }

    public HistorialDiagnosticoDTO actualizarHistorial(Integer id, HistorialDiagnostico histAct) {
        HistorialDiagnostico historial = historialDiagnosticoRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("El historial con ID " + id + " no se encuentra registrado!"));
        if(histAct.getFecha() != null) {
            historial.setFecha(histAct.getFecha());
        }
        if(histAct.getObservacion() != null) {
            historial.setObservacion(histAct.getObservacion());
        }
        if(histAct.getDiagnostico() != null && histAct.getDiagnostico().getId() != null) {
            if(!historialDiagnosticoRepository.existsById(histAct.getDiagnostico().getId())) {
                throw new RuntimeException("El Diagnostico con ID " + histAct.getDiagnostico().getId() + " no existe!");
            }
            historial.setDiagnostico(histAct.getDiagnostico());
        }

        HistorialDiagnostico guardado = historialDiagnosticoRepository.save(historial);
        return convertirADTO(guardado);
    }

    public String eliminarHistorial(Integer id) {
        try {
            HistorialDiagnostico hist = historialDiagnosticoRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("El historial con ID " + id + " no se encuentra registrado!"));

            historialDiagnosticoRepository.delete(hist);
            return "Historial de diagnostico " + id + " eliminada con exito";

        } catch (DataIntegrityViolationException e) {
            return "No se puede eliminar el historial porque actualmente esta conectado a un paciente";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public List<HistorialDiagnosticoDTO> diagnosticoPorIdPaciente(Integer id) {
        if(!pacienteRepository.existsById(id)) {
            throw new RuntimeException("El paciente ingresado no se encuentra registrado!");
        }

        return historialDiagnosticoRepository.findByPacienteId(id).stream()
                                                                .map(this::convertirADTO)
                                                                .toList();
    }

    public List<HistorialDiagnosticoDTO> buscarPorFecha(LocalDate fecha) {
        if(fecha == null) {
            throw new RuntimeException("Debe ingresar una fecha valida para continuar!");
        }

        List<HistorialDiagnostico> historiales = historialDiagnosticoRepository.findByFecha(fecha);

        if(historiales.isEmpty()) {
            throw new RuntimeException("No se encontraron historiales para la fecha ingresada");
        }
        return historiales.stream()
                .map(this::convertirADTO)
                .toList();
    }

    public Integer buscarIdPorDiagnostico(Integer diagnosticoId) {
        return historialDiagnosticoRepository.findByDiagnosticoId(diagnosticoId)
                .map(historial -> historial.getId())
                .orElse(null);
    }

    //convertir a dto
    private HistorialDiagnosticoDTO convertirADTO(HistorialDiagnostico historial) {
        HistorialDiagnosticoDTO dto = new HistorialDiagnosticoDTO();
        dto.setId(historial.getId());
        if (historial.getFecha() != null) {
            dto.setFecha(historial.getFecha());
        }
        dto.setObservacion(historial.getObservacion());
        return dto;
    }

}
