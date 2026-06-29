package com.example.ms_profesionales.service;

import java.util.List;
import org.springframework.stereotype.Service;

import com.example.ms_profesionales.DTO.PsicologoDTO;
import com.example.ms_profesionales.model.Especialidad;
import com.example.ms_profesionales.model.Psicologo;
import com.example.ms_profesionales.model.Sucursal;
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

    public PsicologoDTO guardarPsicologo(Psicologo psicologo) {
        validarPsicologo(psicologo);
        
        if (psicologo.getRut() != null) {
            List<Psicologo> existentes = psicologoRepository.findByRut(psicologo.getRut());
            if (!existentes.isEmpty()) {
                throw new RuntimeException("Ya existe un psicólogo registrado con el RUT: " + psicologo.getRut());
            }
        }
        
        Psicologo guardado = psicologoRepository.save(psicologo);
        return convertirADTO(guardado);
    }

    public PsicologoDTO actualizarPsicologo(Integer id, Psicologo psicologoActualizado) {
        Psicologo psicologoExistente = psicologoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Psicólogo no encontrado con el ID: " + id));

        validarPsicologo(psicologoActualizado);

        psicologoExistente.setRut(psicologoActualizado.getRut());
        psicologoExistente.setDv_rut(psicologoActualizado.getDv_rut());
        psicologoExistente.setPNombre(psicologoActualizado.getPNombre());
        psicologoExistente.setSNombre(psicologoActualizado.getSNombre());
        psicologoExistente.setPApellido(psicologoActualizado.getPApellido());
        psicologoExistente.setSApellido(psicologoActualizado.getSApellido());
        psicologoExistente.setEspecialidades(psicologoActualizado.getEspecialidades());
        psicologoExistente.setSucursales(psicologoActualizado.getSucursales());

        Psicologo actualizado = psicologoRepository.save(psicologoExistente);
        return convertirADTO(actualizado);
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
        dto.setRut(psicologo.getRut());
        dto.setDv_rut(psicologo.getDv_rut());
        dto.setP_nombre(psicologo.getPNombre());
        dto.setS_nombre(psicologo.getSNombre());
        dto.setP_apellido(psicologo.getPApellido());
        dto.setS_apellido(psicologo.getSApellido());

        if (psicologo.getEspecialidades() != null && !psicologo.getEspecialidades().isEmpty()) {
            dto.setEspecialidadesId(psicologo.getEspecialidades().stream()
                .map(Especialidad::getId)
                .toList());
            dto.setNombresEspecialidades(psicologo.getEspecialidades().stream()
                .map(Especialidad::getNombre)
                .toList());
        } else {
            dto.setEspecialidadesId(List.of());
            dto.setNombresEspecialidades(List.of());
        }

        if (psicologo.getSucursales() != null && !psicologo.getSucursales().isEmpty()) {
            dto.setSucursalesId(psicologo.getSucursales().stream()
                .map(Sucursal::getId)
                .toList());
        } else {
            dto.setSucursalesId(List.of());
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
