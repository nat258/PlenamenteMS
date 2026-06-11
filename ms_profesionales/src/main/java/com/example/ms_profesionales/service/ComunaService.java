package com.example.ms_profesionales.service;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ms_profesionales.DTO.ComunaDTO;
import com.example.ms_profesionales.model.Comuna;
import com.example.ms_profesionales.repository.ComunaRepository;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class ComunaService {

    @Autowired
    private ComunaRepository comunaRepository;

    public String eliminarComuna(Integer id) {
        try {
        Comuna comuna = comunaRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Comuna no encontrada con el ID: " + id));
            comunaRepository.delete(comuna);
            return "Comuna eliminada con éxito";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public ComunaDTO registrarComuna(ComunaDTO comunaDTO) {
        if (comunaRepository.existsByNombre(comunaDTO.getNombre())) {
            throw new RuntimeException("La comuna '" + comunaDTO.getNombre() + "' ya se encuentra registrada.");
        }
        Comuna comuna = new Comuna();
        comuna.setNombre(comunaDTO.getNombre());
        Comuna comunaGuardada = comunaRepository.save(comuna);
        comunaDTO.setId(comunaGuardada.getId());
        return comunaDTO;
    }
    
    //Buscar comuna por ID
    public ComunaDTO buscarPorId(Integer id) {
    Comuna comuna = comunaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("¡Comuna no encontrada!"));

    return convertirADTO(comuna);
    }

    //Buscar comuna por nombre exacto
    public List<ComunaDTO> buscarPorNombre(String nombre){
        return comunaRepository.findByNombreContainingIgnoreCase(nombre).stream()
                .map(this::convertirADTO)
                .toList();
    }

    // Buscar comuna que contenga parte del nombre (Busca coincidencias parciales)
    public List<ComunaDTO> buscarPorNombreParcial(String nombre) {
        List<Comuna> comunas = comunaRepository.findByNombreContainingIgnoreCase(nombre);
        if(comunas.isEmpty()){
            throw new RuntimeException("No se encontraron comunas que contengan: " + nombre);
        }
        return comunas.stream()
            .map(this::convertirADTO)
            .toList();
    }

    //convertir dto
    private ComunaDTO convertirADTO(Comuna comuna) {
    ComunaDTO dto = new ComunaDTO();
    dto.setId(comuna.getId());
    dto.setNombre(comuna.getNombre());
    
    if (comuna.getRegion() != null) {
        dto.setNombreRegion(comuna.getRegion().getNombre());
    }
    return dto;
    }

}
