package com.example.ms_profesionales.service;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.ms_profesionales.DTO.ComunaDTO;
import com.example.ms_profesionales.model.Comuna;
import com.example.ms_profesionales.model.Region;
import com.example.ms_profesionales.repository.ComunaRepository;


@ExtendWith(MockitoExtension.class) // Habilita el entorno Mockito para JUnit 5
class ComunaServiceTest {

    @Mock
    private ComunaRepository comunaRepository;

    @InjectMocks
    private ComunaService comunaService;

    private Comuna comunaEjemplo;
    private ComunaDTO comunaDTOEjemplo;
    private Region regionEjemplo;

    @BeforeEach
    void setUp() {
        regionEjemplo = new Region();
        regionEjemplo.setId(1);
        regionEjemplo.setNombre("Metropolitana");

        comunaEjemplo = new Comuna();
        comunaEjemplo.setId(50);
        comunaEjemplo.setNombre("Providencia");
        comunaEjemplo.setRegion(regionEjemplo);

        comunaDTOEjemplo = new ComunaDTO();
        comunaDTOEjemplo.setId(50);
        comunaDTOEjemplo.setNombre("Providencia");
    }

    //Pruebas eliminar comunas
    @Test
    void eliminarComuna_Exitoso_DeberiaRetornarMensajeExito() {
        // Arrange
        when(comunaRepository.findById(50)).thenReturn(Optional.of(comunaEjemplo));
        doNothing().when(comunaRepository).delete(comunaEjemplo);

        // Act
        String resultado = comunaService.eliminarComuna(50);

        // Assert
        assertEquals("Comuna eliminada con éxito", resultado);
        verify(comunaRepository, times(1)).delete(comunaEjemplo);
    }

    @Test
    void eliminarComuna_NoExiste_DeberiaRetornarMensajeError() {
        when(comunaRepository.findById(999)).thenReturn(Optional.empty());

        String resultado = comunaService.eliminarComuna(999);

        assertTrue(resultado.contains("Comuna no encontrada con el ID: 999"));
        verify(comunaRepository, never()).delete(any(Comuna.class));
    }

    
    // Prueba registrar comuna ()
    
    @Test
    void registrarComuna_Exitoso_DeberiaGuardarYRetornarDTOConId() {
        // Arrange
        comunaDTOEjemplo.setId(null); // Simulamos que llega desde el request sin ID

        when(comunaRepository.existsByNombre("Providencia")).thenReturn(false);
        when(comunaRepository.save(any(Comuna.class))).thenReturn(comunaEjemplo);

        ComunaDTO resultado = comunaService.registrarComuna(comunaDTOEjemplo);

        assertNotNull(resultado);
        assertEquals(50, resultado.getId()); // Verifica que se asignó el ID generado
        assertEquals("Providencia", resultado.getNombre());
        verify(comunaRepository, times(1)).save(any(Comuna.class));
    }

    @Test
    void registrarComuna_NombreDuplicado_DeberiaLanzarRuntimeException() {

        when(comunaRepository.existsByNombre("Providencia")).thenReturn(true);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            comunaService.registrarComuna(comunaDTOEjemplo);
        });
        assertTrue(exception.getMessage().contains("ya se encuentra registrada"));
        verify(comunaRepository, never()).save(any(Comuna.class));
    }

    // Prueba buscar por Id.
    @Test
    void buscarPorId_CuandoExiste_DeberiaRetornarDTO() {
        // Arrange
        when(comunaRepository.findById(50)).thenReturn(Optional.of(comunaEjemplo));

        // Act
        ComunaDTO resultado = comunaService.buscarPorId(50);

        // Assert
        assertNotNull(resultado);
        assertEquals(50, resultado.getId());
        assertEquals("Providencia", resultado.getNombre());
        assertEquals("Metropolitana", resultado.getNombreRegion()); // Evalúa el mapeo de la relación
    }

    @Test
    void buscarPorId_CuandoNoExiste_DeberiaLanzarRuntimeException() {
        when(comunaRepository.findById(999)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            comunaService.buscarPorId(999);
        });
        assertEquals("¡Comuna no encontrada!", exception.getMessage());
    }

    
    // Prueba de busqueda por nombre
    
    @Test
    void buscarPorNombre_DeberiaRetornarListaDeDTOs() {
        // Arrange
        when(comunaRepository.findByNombreContainingIgnoreCase("Providencia")).thenReturn(List.of(comunaEjemplo));

        // Act
        List<ComunaDTO> resultado = comunaService.buscarPorNombre("Providencia");

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Providencia", resultado.get(0).getNombre());
    }

    
    // Prueba busqueda por nombre parcial
    
    @Test
    void buscarPorNombreParcial_CuandoCoincide_DeberiaRetornarListaDTO() {
        // Arrange
        when(comunaRepository.findByNombreContainingIgnoreCase("Provi")).thenReturn(List.of(comunaEjemplo));

        // Act
        List<ComunaDTO> resultado = comunaService.buscarPorNombreParcial("Provi");

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Providencia", resultado.get(0).getNombre());
    }

    @Test
    void buscarPorNombreParcial_SinCoincidencias_DeberiaLanzarRuntimeException() {
        
        when(comunaRepository.findByNombreContainingIgnoreCase("Inexistente")).thenReturn(Collections.emptyList());

        
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            comunaService.buscarPorNombreParcial("Inexistente");
        });
        assertTrue(exception.getMessage().contains("No se encontraron comunas que contengan"));
    }
}