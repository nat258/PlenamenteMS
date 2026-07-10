package com.example.ms_pacientes.Service;
import org.mockito.InjectMocks;	

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.ms_pacientes.DTO.DiagnosticoDTO;
import com.example.ms_pacientes.model.Diagnostico;
import com.example.ms_pacientes.repository.DiagnosticoRepository;
import com.example.ms_pacientes.service.DiagnosticoService;

import net.datafaker.Faker;

@ExtendWith(MockitoExtension.class)
public class DiagnosticoServiceTest {

	@Mock
	private DiagnosticoRepository diagnosticoRepository;

	@InjectMocks
	private DiagnosticoService diagnosticoService;
	private Faker faker = new Faker();

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	private Diagnostico crearDiagnosticoFalso(Integer id, String nombre) {
		Diagnostico diagnostico = new Diagnostico();
		diagnostico.setId(id);
		diagnostico.setNombre(nombre);
		diagnostico.setDescripcion(faker.lorem().sentence());
		return diagnostico;
	}

	@Test
	void testBuscarPorId_Exitoso() {
		Integer idSimulado = 1;
		Diagnostico diagnosticoFalso = crearDiagnosticoFalso(idSimulado, "Diabetes");

		when(diagnosticoRepository.findById(idSimulado)).thenReturn(Optional.of(diagnosticoFalso));

		DiagnosticoDTO resultado = diagnosticoService.buscarPorId(idSimulado);

		assertNotNull(resultado);
		assertEquals("Diabetes", resultado.getNombre());
		verify(diagnosticoRepository, times(1)).findById(idSimulado);
	}

	@Test
	void testBuscarPorId_NoEncontrado() {
		Integer idSimulado = 999;
		when(diagnosticoRepository.findById(idSimulado)).thenReturn(Optional.empty());

		assertThrows(RuntimeException.class, () -> diagnosticoService.buscarPorId(idSimulado));
		verify(diagnosticoRepository, times(1)).findById(idSimulado);
	}

	@Test
	void testGuardarDiagnostico_Exitoso() {
		Diagnostico diagnosticoNuevo = crearDiagnosticoFalso(null, "Asma");
		Diagnostico diagnosticoGuardado = crearDiagnosticoFalso(1, "Asma");

		when(diagnosticoRepository.findByNombreIgnoreCase("Asma")).thenReturn(Optional.empty());
		when(diagnosticoRepository.save(any(Diagnostico.class))).thenReturn(diagnosticoGuardado);

		DiagnosticoDTO resultado = diagnosticoService.guardarDiagnostico(diagnosticoNuevo);

		assertNotNull(resultado);
		assertEquals("Asma", resultado.getNombre());
		verify(diagnosticoRepository, times(1)).save(any(Diagnostico.class));
	}


	@Test
	void testActualizarDiagnostico_Exitoso() {
		Integer idSimulado = 1;
		Diagnostico diagnosticoExistente = crearDiagnosticoFalso(idSimulado, "Diabetes");
		Diagnostico datosNuevos = crearDiagnosticoFalso(idSimulado, "Diabetes Tipo 2");
		Diagnostico diagnosticoActualizado = crearDiagnosticoFalso(idSimulado, "Diabetes Tipo 2");

		when(diagnosticoRepository.findById(idSimulado)).thenReturn(Optional.of(diagnosticoExistente));
		when(diagnosticoRepository.findByNombreIgnoreCase("Diabetes Tipo 2")).thenReturn(Optional.empty());
		when(diagnosticoRepository.save(any(Diagnostico.class))).thenReturn(diagnosticoActualizado);

		DiagnosticoDTO resultado = diagnosticoService.actualizarDiagnostico(idSimulado, datosNuevos);

		assertNotNull(resultado);
		assertEquals("Diabetes Tipo 2", resultado.getNombre());
		verify(diagnosticoRepository, times(1)).save(any(Diagnostico.class));
	}

	@Test
	void testActualizarDiagnostico_NoEncontrado() {
		Integer idSimulado = 999;
		Diagnostico datosNuevos = crearDiagnosticoFalso(null, "Neumonía");

		when(diagnosticoRepository.findById(idSimulado)).thenReturn(Optional.empty());

		assertThrows(RuntimeException.class, () -> diagnosticoService.actualizarDiagnostico(idSimulado, datosNuevos));
		verify(diagnosticoRepository, times(0)).save(any(Diagnostico.class));
	}


	@Test
	void testEliminarDiagnostico_Exitoso() {
		Integer idSimulado = 1;
		Diagnostico diagnosticoFalso = crearDiagnosticoFalso(idSimulado, "Eczema");

		when(diagnosticoRepository.findById(idSimulado)).thenReturn(Optional.of(diagnosticoFalso));

		String resultado = diagnosticoService.eliminarDiagnostico(idSimulado);

		assertNotNull(resultado);
		assertTrue(resultado.contains("eliminado"));
		verify(diagnosticoRepository, times(1)).delete(diagnosticoFalso);
	}

	@Test
	void testEliminarDiagnostico_NoEncontrado() {
		Integer idSimulado = 999;
		when(diagnosticoRepository.findById(idSimulado)).thenReturn(Optional.empty());

		String resultado = diagnosticoService.eliminarDiagnostico(idSimulado);

		assertNotNull(resultado);
		assertTrue(resultado.contains("no se encuentra registrado"));
		verify(diagnosticoRepository, times(0)).delete(any(Diagnostico.class));
	}

}
