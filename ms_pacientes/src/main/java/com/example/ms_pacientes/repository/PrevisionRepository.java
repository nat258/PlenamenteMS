package com.example.ms_pacientes.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.ms_pacientes.model.Prevision;

@Repository
public interface PrevisionRepository extends JpaRepository<Prevision, Integer> {

    List<Prevision> findByTipo(String tipo);

    //buscar prevenssion por nombre
    Prevision findByTipoContainingIgnoreCase(String nombre);

}
