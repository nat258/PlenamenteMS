package com.example.ms_profesionales.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ms_profesionales.model.Comuna;

public interface ComunaRepository extends JpaRepository<Comuna,Integer> {
    
    // Busqueda de comuna por nombre exacto
    List<Comuna> findByNombre(String nombre);
  
    //Buscar comuna que contenga parte del nombre
    List<Comuna> findByNombreContainingIgnoreCase(String nombre);

    // Devuelve true si el nombre ya existe en la DB
    boolean existsByNombre(String nombre);

}
