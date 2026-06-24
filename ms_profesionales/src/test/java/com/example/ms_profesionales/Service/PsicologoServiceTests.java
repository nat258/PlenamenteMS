package com.example.ms_profesionales.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.ms_profesionales.DTO.PsicologoDTO;
import com.example.ms_profesionales.model.Especialidad;
import com.example.ms_profesionales.model.Psicologo;
import com.example.ms_profesionales.repository.PsicologoRepository;

@ExtendWith(MockitoExtension.class) // Habilita el entorno Mockito para JUnit 5
class PsicologoServiceTests {

    @Mock
    private PsicologoRepository psicologoRepository;

    @InjectMocks
    private PsicologoService psicologoService;

    private Psicologo psicologoValido;
    private Especialidad especialidadEjemplo;

    @BeforeEach
    void setUp() {
        especialidadEjemplo = new Especialidad();
        especialidadEjemplo.setId(1);
        especialidadEjemplo.setNombre("Psicología Clínica");

        psicologoValido = new Psicologo();
        psicologoValido.setId(100);
        psicologoValido.setRut(12345678L);
        psicologoValido.setPNombre("Claudio");
        psicologoValido.setPApellido("Valenzuela");
        psicologoValido.setEspecialidades(List.of(especialidadEjemplo));
    }

    // ==========================================
    // PRUEBAS: eliminarPsicologo()
    // ==========================================
    @Test
    void eliminarPsicologo_Exitoso_DeberiaRetornarMensajeExito() {
        // Arrange
        when(psicologoRepository.findById(100)).thenReturn(Optional.of(psicologoValido));
        doNothing().when(psicologoRepository).delete(psicologoValido);

        // Act
        String resultado = psicologoService.eliminarPsicologo(100);

        // Assert
        assertEquals("Psicólogo eliminado con éxito", resultado);
        verify(psicologoRepository, times(1)).delete(psicologoValido);
    }

    @Test
    void eliminarPsicologo_NoExiste_DeberiaRetornarMensajeError() {
        // Arrange
        when(psicologoRepository.findById(999)).thenReturn(Optional.empty());

        // Act
        String resultado = psicologoService.eliminarPsicologo(999);

        // Assert
        assertTrue(resultado.contains("Psicólogo no encontrado con el ID: 999"));
        verify(psicologoRepository, never()).delete(any(Psicologo.class));
    }

    // ==========================================
    // PRUEBAS: guardarPsicologo() y validaciones
    // ==========================================
    @Test
    void guardarPsicologo_CaminoExitoso_DeberiaGuardarCorrectamente() {
        // Arrange
        when(psicologoRepository.findByRut(12345678L)).thenReturn(Collections.emptyList());
        when(psicologoRepository.save(any(Psicologo.class))).thenReturn(psicologoValido);

        // Act
        Psicologo resultado = psicologoService.guardarPsicologo(psicologoValido);

        // Assert
        assertNotNull(resultado);
        assertEquals(12345678L, resultado.getRut());
        verify(psicologoRepository, times(1)).save(psicologoValido);
    }

    @Test
    void guardarPsicologo_RutYaExiste_DeberiaLanzarException() {
        // Arrange
        when(psicologoRepository.findByRut(12345678L)).thenReturn(List.of(psicologoValido));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            psicologoService.guardarPsicologo(psicologoValido);
        });
        assertTrue(exception.getMessage().contains("Ya existe un psicólogo registrado con el RUT"));
        verify(psicologoRepository, never()).save(any(Psicologo.class));
    }

    @Test
    void guardarPsicologo_SinNombre_DeberiaLanzarExceptionPorValidacion() {
        // Arrange
        psicologoValido.setPNombre(""); // Nombre vacío invalida las reglas del negocio

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            psicologoService.guardarPsicologo(psicologoValido);
        });
        assertEquals("El nombre es obligatorio.", exception.getMessage());
    }

    @Test
    void guardarPsicologo_SinEspecialidades_DeberiaLanzarExceptionPorValidacion() {
        // Arrange
        psicologoValido.setEspecialidades(new ArrayList<>()); // Lista vacía invalida las reglas

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            psicologoService.guardarPsicologo(psicologoValido);
        });
        assertEquals("El psicólogo debe tener al menos una especialidad asignada.", exception.getMessage());
    }

    // ==========================================
    // PRUEBAS: actualizarPsicologo()
    // ==========================================
    @Test
    void actualizarPsicologo_ExistenteYValido_DeberiaActualizarYGuardar() {
        // Arrange
        Psicologo datosNuevos = new Psicologo();
        datosNuevos.setRut(87654321L);
        datosNuevos.setPNombre("Andrés");
        datosNuevos.setPApellido("Mendoza");
        datosNuevos.setEspecialidades(List.of(especialidadEjemplo));

        when(psicologoRepository.findById(100)).thenReturn(Optional.of(psicologoValido));
        when(psicologoRepository.save(any(Psicologo.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Psicologo resultado = psicologoService.actualizarPsicologo(100, datosNuevos);

        // Assert
        assertNotNull(resultado);
        assertEquals("Andrés", resultado.getPNombre());
        assertEquals(87654321L, resultado.getRut());
    }

    // ==========================================
    // PRUEBAS: Consultas DTO (buscarPorId / buscarPorRut / obtenerTodos)
    // ==========================================
    @Test
    void obtenerTodosLosPsicologos_DeberiaRetornarListaDeDTOs() {
        // Arrange
        when(psicologoRepository.findAll()).thenReturn(List.of(psicologoValido));

        // Act
        List<PsicologoDTO> resultado = psicologoService.obtenerTodosLosPsicologos();

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Claudio", resultado.get(0).getP_nombre());
    }

    @Test
    void buscarPsicologoPorId_CuandoExiste_DeberiaRetornarDTO() {
        // Arrange
        when(psicologoRepository.findById(100)).thenReturn(Optional.of(psicologoValido));

        // Act
        PsicologoDTO resultado = psicologoService.buscarPsicologoPorId(100);

        // Assert
        assertNotNull(resultado);
        assertEquals("Claudio", resultado.getP_nombre());
    }

    @Test
    void buscarPsicologoPorRut_CuandoExiste_DeberiaRetornarDTO() {
        // Arrange
        // Pasamos "12345678" como String para cumplir la firma del método bajo prueba
        when(psicologoRepository.findByRut(12345678L)).thenReturn(List.of(psicologoValido));

        // Act
        PsicologoDTO resultado = psicologoService.buscarPsicologoPorRut("12345678");

        // Assert
        assertNotNull(resultado);
        assertEquals("Claudio", resultado.getP_nombre());
    }

    @Test
    void buscarPsicologoPorRut_CuandoNoExiste_DeberiaLanzarRuntimeException() {
        // Arrange
        when(psicologoRepository.findByRut(99999999L)).thenReturn(Collections.emptyList());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            psicologoService.buscarPsicologoPorRut("99999999");
        });
        assertTrue(exception.getMessage().contains("Psicologo no encontrado con Rut"));
    }
}