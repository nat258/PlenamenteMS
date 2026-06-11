package com.example.ms_profesionales.repository;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.ms_profesionales.model.Region;


@Repository
public interface RegionRepository extends JpaRepository<Region, Integer> {

    //buscar region por nombre parecido
    List<Region> findByNombreContainingIgnoreCase(String nombre);
    
    // buscar por nombre exacto
    Optional<Region> findByNombreIgnoreCase(String nombre);

    boolean existsByNombreIgnoreCase(String nombre);
}
