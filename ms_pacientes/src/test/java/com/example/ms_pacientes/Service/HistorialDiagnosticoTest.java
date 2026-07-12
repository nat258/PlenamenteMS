package com.example.ms_pacientes.Service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.ms_pacientes.DTO.HistorialDiagnosticoDTO;
import com.example.ms_pacientes.model.Diagnostico;
import com.example.ms_pacientes.model.HistorialDiagnostico;
import com.example.ms_pacientes.model.Paciente;
import com.example.ms_pacientes.repository.HistorialDiagnosticoRepository;
import com.example.ms_pacientes.repository.PacienteRepository;
import com.example.ms_pacientes.service.HistorialDiagnosticoService;

import net.datafaker.Faker;

@ExtendWith(MockitoExtension.class)
public class HistorialDiagnosticoTest {

	@Mock
	private HistorialDiagnosticoRepository historialDiagnosticoRepository;

	@Mock
	private PacienteRepository pacienteRepository;

	@InjectMocks
	private HistorialDiagnosticoService historialDiagnosticoService;

	private Faker faker = new Faker();

	private Paciente crearPacienteFalso(Integer id) {
		Paciente paciente = new Paciente();
		paciente.setId(id);
		paciente.setRut(faker.number().digits(8));
		paciente.setPNombre(faker.name().firstName());
		paciente.setPApellido(faker.name().lastName());
		paciente.setCorreo(faker.internet().emailAddress());
		paciente.setNumero(faker.number().digits(10));
		paciente.setDireccion(faker.address().fullAddress());
		return paciente;
	}

	private Diagnostico crearDiagnosticoFalso(Integer id) {
		Diagnostico diagnostico = new Diagnostico();
		diagnostico.setId(id);
		diagnostico.setNombre(faker.disease().anyDisease());
		diagnostico.setDescripcion(faker.lorem().sentence());
		return diagnostico;
	}

	private HistorialDiagnostico crearHistorialFalso(Integer id, Paciente paciente, Diagnostico diagnostico) {
		HistorialDiagnostico historial = new HistorialDiagnostico();
		historial.setId(id);
		historial.setFecha(LocalDate.now());
		historial.setObservacion(faker.lorem().sentence());
		historial.setPaciente(paciente);
		historial.setDiagnostico(diagnostico);
		return historial;
	}

	@Test
	void testObtenerTodos_Exitoso() {
		Paciente paciente = crearPacienteFalso(1);
		Diagnostico diagnostico = crearDiagnosticoFalso(1);
		HistorialDiagnostico hist1 = crearHistorialFalso(1, paciente, diagnostico);
		HistorialDiagnostico hist2 = crearHistorialFalso(2, paciente, diagnostico);

		List<HistorialDiagnostico> listaHistoriales = Arrays.asList(hist1, hist2);

		when(historialDiagnosticoRepository.findAll()).thenReturn(listaHistoriales);

		List<HistorialDiagnosticoDTO> resultado = historialDiagnosticoService.obtenerTodos();

		assertNotNull(resultado);
		assertEquals(2, resultado.size());
		verify(historialDiagnosticoRepository, times(1)).findAll();
	}

	@Test
	void testGuardarHistorial_Exitoso() {
		Paciente paciente = crearPacienteFalso(1);
		Diagnostico diagnostico = crearDiagnosticoFalso(1);
		HistorialDiagnostico historialNuevo = crearHistorialFalso(null, paciente, diagnostico);
		HistorialDiagnostico historialGuardado = crearHistorialFalso(1, paciente, diagnostico);

		when(pacienteRepository.existsById(1)).thenReturn(true);
		when(historialDiagnosticoRepository.existsById(1)).thenReturn(true);
		when(historialDiagnosticoRepository.save(any(HistorialDiagnostico.class))).thenReturn(historialGuardado);

		HistorialDiagnosticoDTO resultado = historialDiagnosticoService.guardarHistorial(historialNuevo);

		assertNotNull(resultado);
		assertEquals(1, resultado.getId());
		verify(pacienteRepository, times(1)).existsById(1);
		verify(historialDiagnosticoRepository, times(1)).save(any(HistorialDiagnostico.class));
	}

	@Test
	void testGuardarHistorial_DiagnosticoNulo() {
		Paciente paciente = crearPacienteFalso(1);
		HistorialDiagnostico historialInvalido = crearHistorialFalso(null, paciente, null);

		when(pacienteRepository.existsById(1)).thenReturn(true);

		assertThrows(RuntimeException.class, () -> historialDiagnosticoService.guardarHistorial(historialInvalido));
		verify(pacienteRepository, times(1)).existsById(1);
	}

	@Test
	void testActualizarHistorial_Exitoso() {
		Integer idHistorial = 1;
		Paciente paciente = crearPacienteFalso(1);
		Diagnostico diagnostico = crearDiagnosticoFalso(1);
		HistorialDiagnostico historialExistente = crearHistorialFalso(idHistorial, paciente, diagnostico);

		HistorialDiagnostico datosNuevos = new HistorialDiagnostico();
		datosNuevos.setFecha(LocalDate.now().plusDays(1));
		datosNuevos.setObservacion("Nueva observación");

		HistorialDiagnostico historialActualizado = crearHistorialFalso(idHistorial, paciente, diagnostico);
		historialActualizado.setFecha(LocalDate.now().plusDays(1));
		historialActualizado.setObservacion("Nueva observación");

		when(historialDiagnosticoRepository.findById(idHistorial)).thenReturn(Optional.of(historialExistente));
		when(historialDiagnosticoRepository.save(any(HistorialDiagnostico.class))).thenReturn(historialActualizado);

		HistorialDiagnosticoDTO resultado = historialDiagnosticoService.actualizarHistorial(idHistorial, datosNuevos);

		assertNotNull(resultado);
		assertEquals("Nueva observación", resultado.getObservacion());
		verify(historialDiagnosticoRepository, times(1)).save(any(HistorialDiagnostico.class));
	}

	@Test
	void testActualizarHistorial_NoEncontrado() {
		Integer idHistorial = 999;
		HistorialDiagnostico datosNuevos = new HistorialDiagnostico();

		when(historialDiagnosticoRepository.findById(idHistorial)).thenReturn(Optional.empty());

		assertThrows(RuntimeException.class, () -> historialDiagnosticoService.actualizarHistorial(idHistorial, datosNuevos));
		verify(historialDiagnosticoRepository, times(0)).save(any(HistorialDiagnostico.class));
	}

	@Test
	void testDiagnosticoPorIdPaciente_Exitoso() {
		Integer idPaciente = 1;
		Paciente paciente = crearPacienteFalso(idPaciente);
		Diagnostico diagnostico = crearDiagnosticoFalso(1);
		HistorialDiagnostico hist1 = crearHistorialFalso(1, paciente, diagnostico);
		HistorialDiagnostico hist2 = crearHistorialFalso(2, paciente, diagnostico);

		List<HistorialDiagnostico> listaHistoriales = Arrays.asList(hist1, hist2);

		when(pacienteRepository.existsById(idPaciente)).thenReturn(true);
		when(historialDiagnosticoRepository.findByPacienteId(idPaciente)).thenReturn(listaHistoriales);

		List<HistorialDiagnosticoDTO> resultado = historialDiagnosticoService.diagnosticoPorIdPaciente(idPaciente);

		assertNotNull(resultado);
		assertEquals(2, resultado.size());
		verify(pacienteRepository, times(1)).existsById(idPaciente);
		verify(historialDiagnosticoRepository, times(1)).findByPacienteId(idPaciente);
	}

	@Test
	void testDiagnosticoPorIdPaciente_PacienteNoExiste() {
		Integer idPaciente = 999;
		when(pacienteRepository.existsById(idPaciente)).thenReturn(false);

		assertThrows(RuntimeException.class, () -> historialDiagnosticoService.diagnosticoPorIdPaciente(idPaciente));
		verify(pacienteRepository, times(1)).existsById(idPaciente);
	}

	@Test
	void testBuscarPorFecha_Exitoso() {
		LocalDate fechaBusqueda = LocalDate.now();
		Paciente paciente = crearPacienteFalso(1);
		Diagnostico diagnostico = crearDiagnosticoFalso(1);
		HistorialDiagnostico hist1 = crearHistorialFalso(1, paciente, diagnostico);
		HistorialDiagnostico hist2 = crearHistorialFalso(2, paciente, diagnostico);

		List<HistorialDiagnostico> listaHistoriales = Arrays.asList(hist1, hist2);

		when(historialDiagnosticoRepository.findByFecha(fechaBusqueda)).thenReturn(listaHistoriales);

		List<HistorialDiagnosticoDTO> resultado = historialDiagnosticoService.buscarPorFecha(fechaBusqueda);

		assertNotNull(resultado);
		assertEquals(2, resultado.size());
		verify(historialDiagnosticoRepository, times(1)).findByFecha(fechaBusqueda);
	}

	@Test
	void testBuscarPorFecha_FechaNula() {
		LocalDate fechaNula = null;

		assertThrows(RuntimeException.class, () -> historialDiagnosticoService.buscarPorFecha(fechaNula));
		verify(historialDiagnosticoRepository, times(0)).findByFecha(any());
	}

	@Test
	void testBuscarIdPorDiagnostico_Exitoso() {
		Integer diagnosticoId = 1;
		Paciente paciente = crearPacienteFalso(1);
		Diagnostico diagnostico = crearDiagnosticoFalso(diagnosticoId);
		HistorialDiagnostico historialFalso = crearHistorialFalso(5, paciente, diagnostico);

		when(historialDiagnosticoRepository.findByDiagnosticoId(diagnosticoId))
				.thenReturn(Optional.of(historialFalso));

		Integer resultado = historialDiagnosticoService.buscarIdPorDiagnostico(diagnosticoId);

		assertNotNull(resultado);
		assertEquals(5, resultado);
		verify(historialDiagnosticoRepository, times(1)).findByDiagnosticoId(diagnosticoId);
	}

	@Test
	void testBuscarIdPorDiagnostico_NoEncontrado() {
		Integer diagnosticoId = 999;
		when(historialDiagnosticoRepository.findByDiagnosticoId(diagnosticoId)).thenReturn(Optional.empty());

		Integer resultado = historialDiagnosticoService.buscarIdPorDiagnostico(diagnosticoId);

		assertNull(resultado);
		verify(historialDiagnosticoRepository, times(1)).findByDiagnosticoId(diagnosticoId);
	}

}
