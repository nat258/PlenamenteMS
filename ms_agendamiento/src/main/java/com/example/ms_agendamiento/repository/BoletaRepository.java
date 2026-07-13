package com.example.ms_agendamiento.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ms_agendamiento.model.Boleta;


public interface BoletaRepository extends JpaRepository<Boleta, Integer> {

    //Busqueda por monto exacto 
    List<Boleta> findByMonto(Integer monto); 
    
}
