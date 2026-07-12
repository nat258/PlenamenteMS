package com.example.ms_agendamiento.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.ms_agendamiento.model.Boleta;

@Repository
public interface BoletaRepository extends JpaRepository<Boleta, Integer> {

    //Busqueda por monto exacto 
    List<Boleta> findByMonto(Integer monto); 
    
}
