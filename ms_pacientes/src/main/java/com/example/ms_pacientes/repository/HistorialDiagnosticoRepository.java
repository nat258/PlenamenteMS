package com.example.ms_pacientes.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.ms_pacientes.model.HistorialDiagnostico;

@Repository
public interface HistorialDiagnosticoRepository extends JpaRepository<HistorialDiagnostico,Integer> {

    Optional<HistorialDiagnostico> findByDiagnosticoId(Integer diagnosticoId);

    //Buscar todos los historiales de un paciente específico ( mostrar ficha completa del paciente)
    List<HistorialDiagnostico> findByPacienteId(Integer pacienteId);
    // Busqueda de  historiales por una fecha exacta
    List<HistorialDiagnostico> findByFecha(LocalDate fecha);

}

