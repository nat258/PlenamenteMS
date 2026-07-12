package com.example.ms_profesionales.repository;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.example.ms_profesionales.model.Psicologo;
import com.example.ms_profesionales.model.Sucursal;

public interface SucursalRepository extends JpaRepository<Sucursal, Integer> {

    // Buscar por nombre parecido
    List<Sucursal> findByNombreContainingIgnoreCase(String nombre);

    // Buscar por nombre exacto
    Optional<Sucursal> findByNombreIgnoreCase(String nombre);

    //psicologos de la sucursal
    @Query("SELECT p FROM Sucursal s JOIN s.psicologos p WHERE s.id = :idSucursal")
    List<Psicologo> buscarPsicologosPorSucursal(@Param("idSucursal") Integer idSucursal);

}
