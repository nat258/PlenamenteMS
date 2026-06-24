package com.example.ms_pacientes.Service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.ms_pacientes.DTO.PrevisionDTO;
import com.example.ms_pacientes.model.Prevision;
import com.example.ms_pacientes.repository.PrevisionRepository;
import com.example.ms_pacientes.service.PrevisionService;

@ExtendWith(MockitoExtension.class)
public class PrevisionServiceTest {

	@Mock
	private PrevisionRepository previsionRepository;

	private PrevisionService previsionService;

	@BeforeEach
	void setUp() {
		previsionService = new PrevisionService(previsionRepository);
	}

	private Prevision crearPrevisionFalsa(Integer id, String tipo) {
		Prevision prevision = new Prevision();
		prevision.setId(id);
		prevision.setTipo(tipo);
		return prevision;
	}

	@Test
	void testBuscarPrevisionPorId_Exitoso() {
		Integer idSimulado = 1;
		Prevision previsionFalsa = crearPrevisionFalsa(idSimulado, "Fonasa");

		when(previsionRepository.findById(idSimulado)).thenReturn(Optional.of(previsionFalsa));

		PrevisionDTO resultado = previsionService.buscarPrevisionPorId(idSimulado);

		assertNotNull(resultado);
		assertEquals("Fonasa", resultado.getTipo());
		verify(previsionRepository, times(1)).findById(idSimulado);
	}

	@Test
	void testBuscarPrevisionPorId_NoEncontrado() {
		Integer idSimulado = 999;
		when(previsionRepository.findById(idSimulado)).thenReturn(Optional.empty());

		assertThrows(RuntimeException.class, () -> previsionService.buscarPrevisionPorId(idSimulado));
		verify(previsionRepository, times(1)).findById(idSimulado);
	}

	@Test
	void testGuardarPrevision_Exitoso() {
		Prevision previsionNueva = crearPrevisionFalsa(null, "Isapre");
		Prevision previsionGuardada = crearPrevisionFalsa(1, "Isapre");

		when(previsionRepository.findByTipo("Isapre")).thenReturn(Arrays.asList());
		when(previsionRepository.save(any(Prevision.class))).thenReturn(previsionGuardada);

		Prevision resultado = previsionService.guardarPrevision(previsionNueva);

		assertNotNull(resultado);
		assertEquals("Isapre", resultado.getTipo());
		verify(previsionRepository, times(1)).save(any(Prevision.class));
	}

	@Test
	void testActualizarPrevision_Exitoso() {
		Integer idSimulado = 1;
		Prevision previsionExistente = crearPrevisionFalsa(idSimulado, "Fonasa");
		Prevision datosNuevos = crearPrevisionFalsa(idSimulado, "Isapre");
		Prevision previsionActualizada = crearPrevisionFalsa(idSimulado, "Isapre");

		when(previsionRepository.findById(idSimulado)).thenReturn(Optional.of(previsionExistente));
		when(previsionRepository.save(any(Prevision.class))).thenReturn(previsionActualizada);

		Prevision resultado = previsionService.actualizarPrevision(idSimulado, datosNuevos);

		assertNotNull(resultado);
		assertEquals("Isapre", resultado.getTipo());
		verify(previsionRepository, times(1)).save(any(Prevision.class));
	}

	@Test
	void testActualizarPrevision_NoEncontrado() {
		Integer idSimulado = 999;
		Prevision datosNuevos = crearPrevisionFalsa(null, "Isapre");

		when(previsionRepository.findById(idSimulado)).thenReturn(Optional.empty());

		assertThrows(RuntimeException.class, () -> previsionService.actualizarPrevision(idSimulado, datosNuevos));
		verify(previsionRepository, times(0)).save(any(Prevision.class));
	}

	@Test
	void testObtenerTodosLasPrevisiones_Exitoso() {
		Prevision prevision1 = crearPrevisionFalsa(1, "Fonasa");
		Prevision prevision2 = crearPrevisionFalsa(2, "Isapre");

		when(previsionRepository.findAll()).thenReturn(Arrays.asList(prevision1, prevision2));

		List<PrevisionDTO> resultado = previsionService.obtenerTodosLasPrevisiones();

		assertNotNull(resultado);
		assertEquals(2, resultado.size());
		verify(previsionRepository, times(1)).findAll();
	}

	@Test
	void testEliminarPrevision_Exitoso() {
		Integer idSimulado = 1;
		Prevision previsionFalsa = crearPrevisionFalsa(idSimulado, "Fonasa");

		when(previsionRepository.findById(idSimulado)).thenReturn(Optional.of(previsionFalsa));

		String resultado = previsionService.eliminarPrevision(idSimulado);

		assertNotNull(resultado);
		assertEquals("Prevision eliminada con éxito", resultado);
		verify(previsionRepository, times(1)).delete(previsionFalsa);
	}

	@Test
	void testEliminarPrevision_NoEncontrado() {
		Integer idSimulado = 999;
		when(previsionRepository.findById(idSimulado)).thenReturn(Optional.empty());

		String resultado = previsionService.eliminarPrevision(idSimulado);

		assertNotNull(resultado);
		verify(previsionRepository, times(0)).delete(any(Prevision.class));
	}

}
