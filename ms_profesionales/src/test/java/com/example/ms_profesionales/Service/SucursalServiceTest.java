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
import org.springframework.dao.DataIntegrityViolationException;

import com.example.ms_profesionales.DTO.PsicologoDTO;
import com.example.ms_profesionales.DTO.SucursalDTO;
import com.example.ms_profesionales.model.Comuna;
import com.example.ms_profesionales.model.Psicologo;
import com.example.ms_profesionales.model.Sucursal;
import com.example.ms_profesionales.repository.ComunaRepository;
import com.example.ms_profesionales.repository.SucursalRepository;
import com.example.ms_profesionales.service.SucursalService;

@ExtendWith(MockitoExtension.class) // Habilita el entorno de Mockito en JUnit 5
class SucursalServiceTest {

    @Mock
    private SucursalRepository sucursalRepository;

    @Mock
    private ComunaRepository comunaRepository;

    @InjectMocks
    private SucursalService sucursalService;

    private Sucursal sucursalEjemplo;
    private Comuna comunaEjemplo;

    @BeforeEach
    void setUp() {
        comunaEjemplo = new Comuna();
        comunaEjemplo.setId(1);

        sucursalEjemplo = new Sucursal();
        sucursalEjemplo.setId(10);
        sucursalEjemplo.setNombre("Sucursal Central");
        sucursalEjemplo.setDireccion("Av. Providencia 1234");
        sucursalEjemplo.setComuna(comunaEjemplo);
    }

    
    // Prueba obtener todas las sucursales
    
    @Test
    void obtenerTodos_DeberiaRetornarListaDeDTOs() {
        
        when(sucursalRepository.findAll()).thenReturn(List.of(sucursalEjemplo));

        
        List<SucursalDTO> resultado = sucursalService.obtenerTodos();

        
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Sucursal Central", resultado.get(0).getNombre());
        verify(sucursalRepository, times(1)).findAll();
    }

    
    //Prueba buscar por Id
    
    @Test
    void buscarPorId_CuandoExiste_DeberiaRetornarDTO() {
        
        when(sucursalRepository.findById(10)).thenReturn(Optional.of(sucursalEjemplo));

        SucursalDTO resultado = sucursalService.buscarPorId(10);

        assertNotNull(resultado);
        assertEquals(10, resultado.getId());
        assertEquals("Sucursal Central", resultado.getNombre());
    }

    @Test
    void buscarPorId_CuandoNoExiste_DeberiaLanzarRuntimeException() {
       
        when(sucursalRepository.findById(99)).thenReturn(Optional.empty());

        
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            sucursalService.buscarPorId(99);
        });
        assertTrue(exception.getMessage().contains("no se encuentra registrado"));
    }

    
    //Prueba guardar nueva sucursal
    
    @Test
    void guardarSucursal_CaminoExitoso_DeberiaGuardarCorrectamente() {
        
        when(comunaRepository.existsById(1)).thenReturn(true);
        when(sucursalRepository.findByNombreIgnoreCase("Sucursal Central")).thenReturn(Optional.empty());
        when(sucursalRepository.save(any(Sucursal.class))).thenReturn(sucursalEjemplo);

    
        SucursalDTO resultado = sucursalService.guardarSucursal(sucursalEjemplo);

        assertNotNull(resultado);
        assertEquals("Sucursal Central", resultado.getNombre());
        verify(sucursalRepository, times(1)).save(any(Sucursal.class));
    }

    @Test
    void guardarSucursal_SinComuna_DeberiaLanzarException() {
        
        sucursalEjemplo.setComuna(null);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            sucursalService.guardarSucursal(sucursalEjemplo);
        });
        assertEquals("Debe asignar una Comuna valida para registrar la sucursal!", exception.getMessage());
    }

    @Test
    void guardarSucursal_ComunaNoExiste_DeberiaLanzarException() {
        
        when(comunaRepository.existsById(1)).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            sucursalService.guardarSucursal(sucursalEjemplo);
        });
        assertTrue(exception.getMessage().contains("no se encuentra registrada"));
    }

    @Test
    void guardarSucursal_NombreDuplicado_DeberiaLanzarException() {
        
        when(comunaRepository.existsById(1)).thenReturn(true);
        when(sucursalRepository.findByNombreIgnoreCase("Sucursal Central")).thenReturn(Optional.of(sucursalEjemplo));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            sucursalService.guardarSucursal(sucursalEjemplo);
        });
        assertEquals("El nombre de sucursal ya se encuentra registrado!", exception.getMessage());
    }

    
    // Prueba Eliminar sucursal
    
    @Test
    void eliminarSucursal_Exitoso_DeberiaRetornarMensajeExito() {
        
        when(sucursalRepository.findById(10)).thenReturn(Optional.of(sucursalEjemplo));
        doNothing().when(sucursalRepository).delete(sucursalEjemplo);

        
        String resultado = sucursalService.eliminarSucursal(10);

        
        assertEquals("Sucursal 'Sucursal Central' eliminada con exito", resultado);
        verify(sucursalRepository, times(1)).delete(sucursalEjemplo);
    }

    @Test
    void eliminarSucursal_ConPsicologosAsociados_DeberiaCapturarDataIntegrityViolationException() {
        
        when(sucursalRepository.findById(10)).thenReturn(Optional.of(sucursalEjemplo));
        doThrow(DataIntegrityViolationException.class).when(sucursalRepository).delete(sucursalEjemplo);

        String resultado = sucursalService.eliminarSucursal(10);

        assertEquals("No se puede eliminar la sucursal porque tiene psicologos asociados", resultado);
    }

    
    // Prueba obtener Psicologo por sucursal
    
    @Test
    void obtenerPsicologosPorSucursal_ConPsicologos_DeberiaRetornarLista() {
        
        when(sucursalRepository.findById(10)).thenReturn(Optional.of(sucursalEjemplo));
        
        Psicologo psicologo = new Psicologo();
        psicologo.setId(1);
        psicologo.setRut(12345678L);
        psicologo.setDv_rut("K");
        psicologo.setPNombre("Juan");
        psicologo.setPApellido("Pérez");
        
        when(sucursalRepository.buscarPsicologosPorSucursal(10)).thenReturn(List.of(psicologo));

        
        List<PsicologoDTO> resultado = sucursalService.obtenerPsicologosPorSucursal(10);

        
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Juan", resultado.get(0).getP_nombre());
    }

    @Test
    void obtenerPsicologosPorSucursal_Vacio_DeberiaLanzarException() {
        
        when(sucursalRepository.findById(10)).thenReturn(Optional.of(sucursalEjemplo));
        when(sucursalRepository.buscarPsicologosPorSucursal(10)).thenReturn(Collections.emptyList());

        
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            sucursalService.obtenerPsicologosPorSucursal(10);
        });
        assertEquals("La sucursal no tiene psicologos registrados.", exception.getMessage());
    }
}