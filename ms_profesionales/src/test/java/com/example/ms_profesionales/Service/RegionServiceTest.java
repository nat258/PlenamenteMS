package com.example.ms_profesionales.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import com.example.ms_profesionales.DTO.RegionDTO;
import com.example.ms_profesionales.model.Region;
import com.example.ms_profesionales.repository.RegionRepository;
import com.example.ms_profesionales.service.RegionService;

@ExtendWith(MockitoExtension.class) 
class RegionServiceTest {

    @Mock
    private RegionRepository regionRepository;

    @InjectMocks
    private RegionService regionService;

    private Region regionEjemplo;

    @BeforeEach
    void setUp() {
        regionEjemplo = new Region();
        regionEjemplo.setId(1);
        regionEjemplo.setNombre("Metropolitana");
    }

    // Prueba obtenerTodos()

    @Test
    void obtenerTodos_DeberiaRetornarListaDeDTOs() {
        
        when(regionRepository.findAll()).thenReturn(List.of(regionEjemplo));

        List<RegionDTO> resultado = regionService.obtenerTodos();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Metropolitana", resultado.get(0).getNombre());
        verify(regionRepository, times(1)).findAll();
    }

    // PRUEBAS: buscarPorId()
    @Test
    void buscarPorId_CuandoExiste_DeberiaRetornarDTO() {
        
        when(regionRepository.findById(1)).thenReturn(Optional.of(regionEjemplo));

        RegionDTO resultado = regionService.buscarPorId(1);

        assertNotNull(resultado);
        assertEquals(1, resultado.getId());
        assertEquals("Metropolitana", resultado.getNombre());
    }

    @Test
    void buscarPorId_CuandoNoExiste_DeberiaLanzarRuntimeException() {
        
        when(regionRepository.findById(99)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            regionService.buscarPorId(99);
        });
        assertTrue(exception.getMessage().contains("no se encuentra registrado"));
    }

    
    // Prueba guardarRegion()
    @Test
    void guardarRegion_CaminoExitoso_DeberiaGuardarCorrectamente() {
        // Arrange
        when(regionRepository.existsByNombreIgnoreCase("Metropolitana")).thenReturn(false);
        when(regionRepository.save(any(Region.class))).thenReturn(regionEjemplo);

        // Act
        RegionDTO resultado = regionService.guardarRegion(regionEjemplo);

        // Assert
        assertNotNull(resultado);
        assertEquals("Metropolitana", resultado.getNombre());
        verify(regionRepository, times(1)).save(any(Region.class));
    }

    @Test
    void guardarRegion_NombreYaExiste_DeberiaLanzarException() {
        // Arrange
        when(regionRepository.existsByNombreIgnoreCase("Metropolitana")).thenReturn(true);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            regionService.guardarRegion(regionEjemplo);
        });
        assertTrue(exception.getMessage().contains("ya se encuentra registrado"));
        verify(regionRepository, never()).save(any(Region.class));
    }

    
    //Prueba actualizar region
    
    @Test
    void actualizarRegion_NombreCambiaYNoEstaDuplicado_DeberiaActualizar() {
        
        Region regNueva = new Region();
        regNueva.setNombre("Valparaíso");

        when(regionRepository.findById(1)).thenReturn(Optional.of(regionEjemplo));
        when(regionRepository.findByNombreIgnoreCase("Valparaíso")).thenReturn(Optional.empty());
        
        Region regionGuardada = new Region();
        regionGuardada.setId(1);
        regionGuardada.setNombre("Valparaíso");
        when(regionRepository.save(any(Region.class))).thenReturn(regionGuardada);

       
        RegionDTO resultado = regionService.actualizarRegion(1, regNueva);

        
        assertNotNull(resultado);
        assertEquals("Valparaíso", resultado.getNombre());
    }

    @Test
    void actualizarRegion_NombreDuplicadoEnOtraRegion_DeberiaLanzarException() {
        // Arrange
        Region regNueva = new Region();
        regNueva.setNombre("Biobío");

        when(regionRepository.findById(1)).thenReturn(Optional.of(regionEjemplo));
        
        Region otraRegion = new Region();
        otraRegion.setId(2);
        otraRegion.setNombre("Biobío");
        when(regionRepository.findByNombreIgnoreCase("Biobío")).thenReturn(Optional.of(otraRegion));

        
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            regionService.actualizarRegion(1, regNueva);
        });
        assertTrue(exception.getMessage().contains("ya se encuentra registrado en otra region"));
    }

    
    // Prueba eliminarRegion()
    
    @Test
    void eliminarRegion_Exitoso_DeberiaRetornarMensajeExito() {
        // Arrange
        when(regionRepository.findById(1)).thenReturn(Optional.of(regionEjemplo));
        doNothing().when(regionRepository).delete(regionEjemplo);

        String resultado = regionService.eliminarRegion(1);

        assertEquals("Region 'Metropolitana' eliminada exitosamente.", resultado);
        verify(regionRepository, times(1)).delete(regionEjemplo);
    }

    @Test
    void eliminarRegion_ConComunasAsociadas_DeberiaCapturarDataIntegrityViolationException() {
        
        when(regionRepository.findById(1)).thenReturn(Optional.of(regionEjemplo));
        doThrow(DataIntegrityViolationException.class).when(regionRepository).delete(regionEjemplo);

        String resultado = regionService.eliminarRegion(1);

        assertEquals("No se puede eliminar la Region porque tiene comunas asociadas", resultado);
    }

    // ==========================================
    // PRUEBAS: encontrarPorNombre()
    // ==========================================
    @Test
    void encontrarPorNombre_DeberiaRetornarListaFiltrada() {
        // Arrange
        when(regionRepository.findByNombreContainingIgnoreCase("metro")).thenReturn(List.of(regionEjemplo));

        // Act
        List<RegionDTO> resultado = regionService.encontrarPorNombre("metro");

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Metropolitana", resultado.get(0).getNombre());
    }
}
