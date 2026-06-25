package com.example.ms_pacientes.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.ms_pacientes.model.Diagnostico;

public interface DiagnosticoRepository extends JpaRepository<Diagnostico,Integer> {

    @Query("SELECT d FROM Diagnostico d JOIN d.historialesDiagnostico h WHERE h.paciente.rut = :rut")
    Optional<Diagnostico> findByPacienteRut(@Param("rut") String rut);

    // Buscar por nombre exacto (útil para validaciones)
    Optional<Diagnostico> findByNombreIgnoreCase(String nombre);

}
