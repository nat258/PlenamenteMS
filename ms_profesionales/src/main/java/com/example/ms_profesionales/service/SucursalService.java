package com.example.ms_profesionales.service;
import java.util.List;
import java.util.Optional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.example.ms_profesionales.DTO.PsicologoDTO;
import com.example.ms_profesionales.DTO.SucursalDTO;
import com.example.ms_profesionales.model.Especialidad;
import com.example.ms_profesionales.model.Psicologo;
import com.example.ms_profesionales.model.Sucursal;
import com.example.ms_profesionales.repository.ComunaRepository;
import com.example.ms_profesionales.repository.SucursalRepository;
import jakarta.transaction.Transactional;

@Service
@Transactional

public class SucursalService {

    private final SucursalRepository sucursalRepository;

    private final ComunaRepository comunaRepository;

    SucursalService(SucursalRepository sucursalRepository, ComunaRepository comunaRepository) {
        this.sucursalRepository = sucursalRepository;
        this.comunaRepository = comunaRepository;
    }

    public List<SucursalDTO> obtenerTodos() {
    return sucursalRepository.findAll().stream()
            .map(this::convertirADTO)
            .toList();
    }

    public SucursalDTO buscarPorId(Integer id) {
        Sucursal sucursal = sucursalRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("La sucursal con ID " + id + " no se encuentra registrado!"));
        return convertirADTO(sucursal);
    }

    public List<SucursalDTO> buscarPorNombre(String nombre) {
        return sucursalRepository.findByNombreContainingIgnoreCase(nombre).stream()
                .map(this::convertirADTO)
                .toList();
    }

    public SucursalDTO guardarSucursal(Sucursal sucursal) {
        if(sucursal.getComuna() == null || sucursal.getComuna().getId() == null) {
            throw new RuntimeException("Debe asignar una Comuna valida para registrar la sucursal!");
        }

        if(!comunaRepository.existsById(sucursal.getComuna().getId())) {
            throw new RuntimeException("La comuna con ID " + sucursal.getComuna().getId() + " no se encuentra registrada!");
        }

        Optional<Sucursal> suc = sucursalRepository.findByNombreIgnoreCase(sucursal.getNombre());
        
        if(suc.isPresent()) {
            throw new RuntimeException("El nombre de sucursal ya se encuentra registrado!");
        }

        Sucursal guardado = sucursalRepository.save(sucursal);
        return convertirADTO(guardado);
    }

    public SucursalDTO actualizarSucursal(Integer id, Sucursal sucursalAct) {

        Sucursal sucursal = sucursalRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("El ID " + id + " no se encuentra registrado!"));

        if(sucursalAct.getNombre() != null && !sucursalAct.getNombre().equalsIgnoreCase(sucursal.getNombre())) {
            Optional<Sucursal> nombre = sucursalRepository.findByNombreIgnoreCase(sucursalAct.getNombre());
            if(nombre.isPresent()) {
                throw new RuntimeException("El nombre '" + sucursalAct.getNombre() + "' ya se encuentra registrado en otra sucursal!");
            }
            sucursal.setNombre(sucursalAct.getNombre());
        }

        if(sucursalAct.getDireccion() != null) {
            sucursal.setDireccion(sucursalAct.getDireccion());
        }

        if(sucursalAct.getComuna()!= null && sucursalAct.getComuna().getId() != null) {
            if(!comunaRepository.existsById(sucursalAct.getComuna().getId())) {
            throw new RuntimeException("La comuna con ID " + sucursalAct.getComuna().getId() + " no se encuentra registrada!");
            }
            sucursal.setComuna(sucursalAct.getComuna());
        }

        Sucursal guardado = sucursalRepository.save(sucursal);
        return convertirADTO(guardado);
    }

    public String eliminarSucursal(Integer id) {
        try {
            Sucursal sucursal = sucursalRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("La sucursal de ID " + id + " no se encuentra registrado!"));

            sucursalRepository.delete(sucursal);
            return "Sucursal '" + sucursal.getNombre() + "' eliminada con exito";
        } catch (DataIntegrityViolationException e) {
            return "No se puede eliminar la sucursal porque tiene psicologos asociados";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public List<PsicologoDTO> obtenerPsicologosPorSucursal(Integer idSucursal) {
        buscarPorId(idSucursal);

        List<Psicologo> psicologos = sucursalRepository.buscarPsicologosPorSucursal(idSucursal);

        if(psicologos.isEmpty()) {
            throw new RuntimeException("La sucursal no tiene psicologos registrados.");
        }

        return psicologos.stream()
                .map(this::convertirPsicologoADTO)
                .toList();
    }

    //convertir a dto
    private SucursalDTO convertirADTO(Sucursal sucursal) {
        SucursalDTO dto = new SucursalDTO();
        dto.setId(sucursal.getId());
        dto.setNombre(sucursal.getNombre());
        dto.setDireccion(sucursal.getDireccion());
        
        if (sucursal.getComuna() != null) {
            dto.setComunaId(sucursal.getComuna().getId());
        }
        return dto;
    }

    // convertir lista de psicologos a dto
    private PsicologoDTO convertirPsicologoADTO(Psicologo psicologo) {
        PsicologoDTO dto = new PsicologoDTO();

        dto.setId(psicologo.getId());
        dto.setRut(psicologo.getRut());
        dto.setDv_rut(psicologo.getDv_rut());
        dto.setP_nombre(psicologo.getPNombre());
        dto.setS_nombre(psicologo.getSNombre());
        dto.setP_apellido(psicologo.getPApellido());
        dto.setS_apellido(psicologo.getSApellido());

        if(psicologo.getEspecialidades() != null) {
            dto.setEspecialidadesId(psicologo.getEspecialidades().stream()
                    .map(Especialidad::getId)
                    .toList());

            dto.setNombresEspecialidades(psicologo.getEspecialidades().stream()
                    .map(Especialidad::getNombre)
                    .toList());
        }

        if(psicologo.getSucursales() != null) {
            dto.setSucursalesId(psicologo.getSucursales().stream()
                    .map(s -> s.getId())
                    .toList());
        }
        return dto;
    }

}
