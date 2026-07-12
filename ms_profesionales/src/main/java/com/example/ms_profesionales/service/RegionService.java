package com.example.ms_profesionales.service;

import java.util.List;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.example.ms_profesionales.DTO.RegionDTO;
import com.example.ms_profesionales.model.Region;
import com.example.ms_profesionales.repository.RegionRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class RegionService {

    private final RegionRepository regionRepository;

    RegionService(RegionRepository regionRepository) {
        this.regionRepository = regionRepository;
    }

    public List<RegionDTO> obtenerTodos() {
        return regionRepository.findAll().stream()
                .map(this::convertirADTO)
                .toList();
    }

    public RegionDTO buscarPorId(Integer id) {
        Region encontrada = regionRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException("El ID " + id + " no se encuentra registrado!"));
        return convertirADTO(encontrada);
    }

    public RegionDTO guardarRegion(Region reg) {
        if(regionRepository.existsByNombreIgnoreCase(reg.getNombre())) {
            throw new RuntimeException("El nombre '" + reg.getNombre() + "' ya se encuentra registrado!");
        }
        Region guardado = regionRepository.save(reg);
        return convertirADTO(guardado);
    }

    public RegionDTO actualizarRegion(Integer id, Region regNueva) {
        Region region = regionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("La region con ID " + id + " no se encuentra registrada!"));

        if(regNueva.getNombre() != null && !region.getNombre().equalsIgnoreCase(regNueva.getNombre())) {

            if(regionRepository.findByNombreIgnoreCase(regNueva.getNombre()).isPresent()) {
            throw new RuntimeException("El nombre " + regNueva.getNombre() + " ya se encuentra registrado en otra region!");
            }
            
            region.setNombre(regNueva.getNombre());
        }
        
        Region regActualizada = regionRepository.save(region);
        return convertirADTO(regActualizada);
    }

    public String eliminarRegion(Integer id) {
        try {
            Region region = regionRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("La Region con ID " + id + " no se encuentra registrada!"));
        
            regionRepository.delete(region);
            return "Region '" + region.getNombre() + "' eliminada exitosamente.";
        } catch (DataIntegrityViolationException e) {
            return "No se puede eliminar la Region porque tiene comunas asociadas";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public List<RegionDTO> encontrarPorNombre(String nombre) {
        return regionRepository.findByNombreContainingIgnoreCase(nombre).stream()
                .map(this::convertirADTO)
                .toList();
    }

    //Convertir a DTO
    
    private RegionDTO convertirADTO(Region region) {
        RegionDTO regionDTO = new RegionDTO();
        regionDTO.setId(region.getId());
        regionDTO.setNombre(region.getNombre());
        return regionDTO;
    }
}