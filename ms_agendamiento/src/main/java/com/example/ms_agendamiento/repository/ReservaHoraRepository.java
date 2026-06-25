package com.example.ms_agendamiento.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ms_agendamiento.model.ReservaHora;

public interface ReservaHoraRepository extends JpaRepository<ReservaHora, Integer> {

    // Busca reservas ya registradas con los parametros ingresados
    List<ReservaHora> findByPsicologoIdAndFechaHora(Integer psicologoId, LocalDateTime fechaHora);

    // Busca las reservas de un paciente a traves de su id
    List<ReservaHora> findByPacienteId(Integer pacienteId);
    
    // Busca las reservas asignadas de un psicologo a traves de su id
    List<ReservaHora> findByPsicologoId(Integer psicologoId);

}
