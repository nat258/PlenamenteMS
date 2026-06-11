package com.example.ms_profesionales.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ms_profesionales.DTO.EspecialidadDTO;
import com.example.ms_profesionales.model.Especialidad;
import com.example.ms_profesionales.repository.EspecialidadRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class EspecialidadService {

    @Autowired
    private EspecialidadRepository especialidadRepository;

    // Buscar por coincidencia parcial al tipo de especialidad .
    public List<EspecialidadDTO> buscarPorNombreParcial(String nombre) {
        List<Especialidad> especialidades = especialidadRepository.findByNombreContainingIgnoreCase(nombre);
        if(especialidades.isEmpty()){
            throw new RuntimeException("No se encontraron especialidades que contengan: " + nombre);
        }
        return especialidades.stream()
                .map(this::convertirADTO)
                .toList();
    }

    //Buscar por Id de especialidad.
    public EspecialidadDTO buscarPorId(Integer id) {
    Especialidad especialidad = especialidadRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("¡Especialidad no encontrada!"));

    return convertirADTO(especialidad);
    }

    //Buscar todas las especialidades que tiene un psicologo 
    public List<EspecialidadDTO> buscarPorPsicologo(Integer idPsicologo) {
        List<Especialidad> especialidades = especialidadRepository.findByPsicologosId(idPsicologo);
        if (especialidades.isEmpty()) {
            throw new RuntimeException("No se encontraron especialidades para el psicólogo con ID: " + idPsicologo);
        }
        return especialidades.stream()
                .map(this::convertirADTO)
                .toList();
    }

    //Convertir a DTO
    private EspecialidadDTO convertirADTO(Especialidad especialidad) {
        EspecialidadDTO especialidadDTO = new EspecialidadDTO();
        especialidadDTO.setId(especialidad.getId());
        especialidadDTO.setNombre(especialidad.getNombre());
        return especialidadDTO;
    }

    public EspecialidadDTO registrarEspecialidad(EspecialidadDTO especialidadDTO) {
        if (especialidadRepository.existsByNombre(especialidadDTO.getNombre())) {
            throw new RuntimeException("La especialidad '" + especialidadDTO.getNombre() + "' ya existe.");
        }
        Especialidad especialidad = new Especialidad();
        especialidad.setNombre(especialidadDTO.getNombre());
        Especialidad especialidadGuardada = especialidadRepository.save(especialidad);
        especialidadDTO.setId(especialidadGuardada.getId());
        return especialidadDTO;
    }

}
