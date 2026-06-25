package com.example.ms_pacientes.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.ms_pacientes.DTO.PrevisionDTO;
import com.example.ms_pacientes.model.Prevision;
import com.example.ms_pacientes.repository.PrevisionRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class PrevisionService {

    private final PrevisionRepository previsionRepository;

    public PrevisionService(PrevisionRepository previsionRepository) {
        this.previsionRepository = previsionRepository;
    }

    public String eliminarPrevision(Integer id) {
        try {
        Prevision prevision = previsionRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Previsión no encontrada con el ID: " + id));
            previsionRepository.delete(prevision);
            return "Prevision eliminada con éxito";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public Prevision guardarPrevision(Prevision prevision) {
        validarPrevision(prevision);
        
        if (!previsionRepository.findByTipo(prevision.getTipo()).isEmpty()) {
            throw new RuntimeException("La previsión '" + prevision.getTipo() + "' ya existe.");
        }
        
        return previsionRepository.save(prevision);
    }

    public Prevision actualizarPrevision(Integer id, Prevision previsionActualizada) {
        Prevision previsionExistente = previsionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Previsión no encontrada con ID: " + id));

        validarPrevision(previsionActualizada);
        previsionExistente.setTipo(previsionActualizada.getTipo());
        
        return previsionRepository.save(previsionExistente);
    }

    //DTO
    public List<PrevisionDTO> obtenerTodosLasPrevisiones(){
            return previsionRepository.findAll().stream()
                    .map(this::convertirADTO)
                    .toList();
        }


    public PrevisionDTO buscarPrevisionPorId(Integer id){
        Prevision prevision = previsionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Previsión no encontrada con ID: " + id));
        return convertirADTO(prevision);
    }

    //convertir a dto
    private PrevisionDTO convertirADTO(Prevision prevision) {
        PrevisionDTO previsionDTO = new PrevisionDTO();
        previsionDTO.setId(prevision.getId());
        previsionDTO.setTipo(prevision.getTipo());
        return previsionDTO;
    }

    //validacion
    private void validarPrevision(Prevision prevision) {
        if (prevision.getTipo() == null || prevision.getTipo().trim().isEmpty()) {
            throw new RuntimeException("El nombre del tipo de previsión es obligatorio.");
        }
    }

}
