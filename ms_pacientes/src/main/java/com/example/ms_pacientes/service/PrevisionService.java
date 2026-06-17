package com.example.ms_pacientes.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ms_pacientes.DTO.PrevisionDTO;
import com.example.ms_pacientes.model.Prevision;
import com.example.ms_pacientes.repository.PrevisionRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class PrevisionService {

    @Autowired
    private PrevisionRepository previsionRepositary;

    
    public String eliminarPrevision(Integer id) {
        try {
        Prevision prevision = previsionRepositary.findById(id)
                    .orElseThrow(() -> new RuntimeException("Previsión no encontrada con el ID: " + id));
            previsionRepositary.delete(prevision);
            return "Prevision eliminada con éxito";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public Prevision guardarPrevision(Prevision prevision) {
        validarPrevision(prevision);
        
        if (!previsionRepositary.findByTipo(prevision.getTipo()).isEmpty()) {
            throw new RuntimeException("La previsión '" + prevision.getTipo() + "' ya existe.");
        }
        
        return previsionRepositary.save(prevision);
    }

    public Prevision actualizarPrevision(Integer id, Prevision previsionActualizada) {
        Prevision previsionExistente = previsionRepositary.findById(id)
                .orElseThrow(() -> new RuntimeException("Previsión no encontrada con ID: " + id));

        validarPrevision(previsionActualizada);
        previsionExistente.setTipo(previsionActualizada.getTipo());
        
        return previsionRepositary.save(previsionExistente);
    }

    //DTO
    public List<PrevisionDTO> obtenerTodosLasPrevisiones(){
            return previsionRepositary.findAll().stream()
                    .map(this::convertirADTO)
                    .toList();
        }


    public PrevisionDTO buscarPrevisionPorId(Integer id){
        Prevision prevision = previsionRepositary.findById(id)
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
