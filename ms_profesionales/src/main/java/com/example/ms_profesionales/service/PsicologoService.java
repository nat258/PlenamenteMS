package com.example.ms_profesionales.service;

import java.util.List;
import org.springframework.stereotype.Service;

import com.example.ms_profesionales.DTO.PsicologoDTO;
import com.example.ms_profesionales.model.Psicologo;
import com.example.ms_profesionales.repository.PsicologoRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class PsicologoService {

    private final PsicologoRepository psicologoRepository;

    PsicologoService(PsicologoRepository psicologoRepository) {
        this.psicologoRepository = psicologoRepository;
    }

    public String eliminarPsicologo(Integer id) {
        try {
        Psicologo psicologo = psicologoRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Psicólogo no encontrado con el ID: " + id));
            psicologoRepository.delete(psicologo);
            return "Psicólogo eliminado con éxito";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public Psicologo guardarPsicologo(Psicologo psicologo) {
        validarPsicologo(psicologo);
        
        if (psicologo.getRut() != null) {
            List<Psicologo> existentes = psicologoRepository.findByRut(psicologo.getRut());
            if (!existentes.isEmpty()) {
                throw new RuntimeException("Ya existe un psicólogo registrado con el RUT: " + psicologo.getRut());
            }
        }
        
        return psicologoRepository.save(psicologo);
    }

    public Psicologo actualizarPsicologo(Integer id, Psicologo psicologoActualizado) {
        Psicologo psicologoExistente = psicologoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Psicólogo no encontrado con el ID: " + id));

        validarPsicologo(psicologoActualizado);

        psicologoExistente.setRut(psicologoActualizado.getRut());
        psicologoExistente.setPNombre(psicologoActualizado.getPNombre());
        psicologoExistente.setPApellido(psicologoActualizado.getPApellido());
        psicologoExistente.setEspecialidades(psicologoActualizado.getEspecialidades());

        return psicologoRepository.save(psicologoExistente);
    }


    //DTO

    public List<PsicologoDTO> obtenerTodosLosPsicologos(){
        return psicologoRepository.findAll().stream()
                .map(this::convertirADTO)
                .toList();
    }

    public PsicologoDTO buscarPsicologoPorId(Integer id){
        Psicologo psicologo = psicologoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Psicólogo no encontrado con ID: " + id));
        return convertirADTO(psicologo);
    }

    public PsicologoDTO buscarPsicologoPorRut(String rut){
        Psicologo psicologo = psicologoRepository.findByRut(Long.parseLong(rut)).stream().findFirst()
                .orElseThrow(() -> new RuntimeException("Psicologo no encontrado con Rut : " + rut));
        return convertirADTO(psicologo);
    }

    //convertir a dto
    private PsicologoDTO convertirADTO(Psicologo psicologo) {
    PsicologoDTO dto = new PsicologoDTO();
    dto.setId(psicologo.getId());
    dto.setP_nombre(psicologo.getPNombre());
    dto.setP_apellido(psicologo.getPApellido());
    
    // Mapeo de listas (Especialidades)
    if (psicologo.getEspecialidades() != null) {
        dto.setNombresEspecialidades(psicologo.getEspecialidades().stream()
            .map(e -> e.getNombre()).toList());
    }
    return dto;
}

    private void validarPsicologo(Psicologo psicologo) {
        if (psicologo.getRut() == null || psicologo.getRut() <= 0) {
            throw new RuntimeException("El RUT es obligatorio para el registro profesional.");
        }
        if (psicologo.getPNombre() == null || psicologo.getPNombre().trim().isEmpty()) {
            throw new RuntimeException("El nombre es obligatorio.");
        }
        if (psicologo.getPApellido() == null || psicologo.getPApellido().trim().isEmpty()) {
            throw new RuntimeException("El apellido es obligatorio.");
        }
        // Validación opcional: verificar que tenga al menos una especialidad
        if (psicologo.getEspecialidades() == null || psicologo.getEspecialidades().isEmpty()) {
            throw new RuntimeException("El psicólogo debe tener al menos una especialidad asignada.");
        }
    }

}
