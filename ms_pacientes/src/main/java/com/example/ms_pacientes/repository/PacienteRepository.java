package com.example.ms_pacientes.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.ms_pacientes.model.Paciente;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente,Integer> {

    //Busqueda por id
    Optional<Paciente> findById(Integer id);

    //Busqueda por Rut 
    Optional<Paciente> findByRut(String rut);

    //Buscar por nombre y apellido
    List<Paciente> findByPNombreContainingIgnoreCaseOrPApellidoContainingIgnoreCase(String p_nombre, String p_apellido);
    
    //Verificar si rut esta registrado 
    boolean existsByRut(String rut);

}
