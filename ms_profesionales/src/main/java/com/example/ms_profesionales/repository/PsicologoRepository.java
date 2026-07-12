package com.example.ms_profesionales.repository;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.example.ms_profesionales.model.Psicologo;


public interface PsicologoRepository extends JpaRepository<Psicologo, Integer> {

    List<Psicologo> findByRut(Long rut);

    // Buscar psicólogos por especialidad
    List<Psicologo> findByEspecialidadesContainingIgnoreCase(String especialidad);

    //buscar por especialidad con query
    @Query("SELECT p FROM Psicologo p JOIN p.especialidades e WHERE e.nombre = :nombreEsp")
    List<Psicologo> buscarPorEspecialidad(@Param("nombreEsp") String nombreEsp);

    // Buscar por nombre de psicologo
    List<Psicologo> findByPNombreContainingIgnoreCase(String pNombre);


}