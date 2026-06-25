package com.example.ms_profesionales.Service;

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

import com.example.ms_profesionales.DTO.EspecialidadDTO;
import com.example.ms_profesionales.model.Especialidad;
import com.example.ms_profesionales.repository.EspecialidadRepository;
import com.example.ms_profesionales.service.EspecialidadService;

@ExtendWith(MockitoExtension.class) // Inicializa el entorno Mockito con JUnit 5
class EspecialidadServiceTest {

    @Mock
    private EspecialidadRepository especialidadRepository;

    @InjectMocks
    private EspecialidadService especialidadService;

    private Especialidad especialidadEjemplo;
    private EspecialidadDTO especialidadDTOEjemplo;

    @BeforeEach
    void setUp() {
        especialidadEjemplo = new Especialidad();
        especialidadEjemplo.setId(5);
        especialidadEjemplo.setNombre("Terapia Cognitivo Conductual");

        especialidadDTOEjemplo = new EspecialidadDTO();
        especialidadDTOEjemplo.setId(5);
        especialidadDTOEjemplo.setNombre("Terapia Cognitivo Conductual");
    }

    
    //Prueba buscar por nombre parcial
    
    @Test
    void buscarPorNombreParcial_CuandoCoincide_DeberiaRetornarListaDTO() {
        
        when(especialidadRepository.findByNombreContainingIgnoreCase("Cognitivo"))
                .thenReturn(List.of(especialidadEjemplo));

        
        List<EspecialidadDTO> resultado = especialidadService.buscarPorNombreParcial("Cognitivo");

        
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Terapia Cognitivo Conductual", resultado.get(0).getNombre());
    }

    @Test
    void buscarPorNombreParcial_SinCoincidencias_DeberiaLanzarRuntimeException() {
        
        when(especialidadRepository.findByNombreContainingIgnoreCase("Inexistente"))
                .thenReturn(Collections.emptyList());

        
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            especialidadService.buscarPorNombreParcial("Inexistente");
        });
        assertTrue(exception.getMessage().contains("No se encontraron especialidades que contengan"));
    }

    
    // Prueba buscar por Id
    
    @Test
    void buscarPorId_CuandoExiste_DeberiaRetornarDTO() {
        
        when(especialidadRepository.findById(5)).thenReturn(Optional.of(especialidadEjemplo));

        EspecialidadDTO resultado = especialidadService.buscarPorId(5);

        assertNotNull(resultado);
        assertEquals(5, resultado.getId());
        assertEquals("Terapia Cognitivo Conductual", resultado.getNombre());
    }

    @Test
    void buscarPorId_CuandoNoExiste_DeberiaLanzarRuntimeException() {
        
        when(especialidadRepository.findById(99)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            especialidadService.buscarPorId(99);
        });
        assertEquals("¡Especialidad no encontrada!", exception.getMessage());
    }

    
    // Prueba buscar por Psicologo

    @Test
    void buscarPorPsicologo_CuandoTieneEspecialidades_DeberiaRetornarLista() {
        
        when(especialidadRepository.findByPsicologosId(1)).thenReturn(List.of(especialidadEjemplo));

        List<EspecialidadDTO> resultado = especialidadService.buscarPorPsicologo(1);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Terapia Cognitivo Conductual", resultado.get(0).getNombre());
    }

    @Test
    void buscarPorPsicologo_SinEspecialidades_DeberiaLanzarRuntimeException() {
        
        when(especialidadRepository.findByPsicologosId(2)).thenReturn(Collections.emptyList());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            especialidadService.buscarPorPsicologo(2);
        });
        assertTrue(exception.getMessage().contains("No se encontraron especialidades para el psicólogo"));
    }

    
    //Prueba registrar especialidad 
    
    @Test
    void registrarEspecialidad_Exitoso_DeberiaGuardarYRetornarDTOConId() {

        especialidadDTOEjemplo.setId(null); // Simulamos que viene de la petición sin ID asignado aún

        when(especialidadRepository.existsByNombre("Terapia Cognitivo Conductual")).thenReturn(false);
        when(especialidadRepository.save(any(Especialidad.class))).thenReturn(especialidadEjemplo);

        EspecialidadDTO resultado = especialidadService.registrarEspecialidad(especialidadDTOEjemplo);

        assertNotNull(resultado);
        assertEquals(5, resultado.getId()); // Comprueba que se mapeó de vuelta el ID generado por la persistencia
        assertEquals("Terapia Cognitivo Conductual", resultado.getNombre());
        verify(especialidadRepository, times(1)).save(any(Especialidad.class));
    }

    @Test
    void registrarEspecialidad_NombreDuplicado_DeberiaLanzarRuntimeException() {
        when(especialidadRepository.existsByNombre("Terapia Cognitivo Conductual")).thenReturn(true);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            especialidadService.registrarEspecialidad(especialidadDTOEjemplo);
        });
        assertTrue(exception.getMessage().contains("ya existe"));
        verify(especialidadRepository, never()).save(any(Especialidad.class));
    }
}