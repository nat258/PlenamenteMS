package com.example.ms_pacientes.Service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
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

import com.example.ms_pacientes.DTO.PacienteDTO;
import com.example.ms_pacientes.model.Paciente;
import com.example.ms_pacientes.repository.PacienteRepository;
import com.example.ms_pacientes.service.PacienteService;

import net.datafaker.Faker;

@ExtendWith(MockitoExtension.class)
public class PacienteServiceTest {

	@Mock
	private PacienteRepository pacienteRepository;

	private PacienteService pacienteService;
	private Faker faker = new Faker();

	@BeforeEach
	void setUp() {
		pacienteService = new PacienteService(pacienteRepository);
	}

	private Paciente crearPacienteFalso(Integer id, String rut) {
		Paciente paciente = new Paciente();
		paciente.setId(id);
		paciente.setRut(rut);
		paciente.setPNombre(faker.name().firstName());
		paciente.setSNombre(faker.name().firstName());
		paciente.setPApellido(faker.name().lastName());
		paciente.setSApellido(faker.name().lastName());
		paciente.setCorreo(faker.internet().emailAddress());
		paciente.setNumero(faker.number().digits(10));
		paciente.setDireccion(faker.address().fullAddress());
		return paciente;
	}

	@Test
	void testBuscarPorId_Exitoso() {
		Integer idSimulado = 1;
		Paciente pacienteFalso = crearPacienteFalso(idSimulado, "123456789");

		when(pacienteRepository.findById(idSimulado)).thenReturn(Optional.of(pacienteFalso));

		PacienteDTO resultado = pacienteService.buscarPorID(idSimulado);

		assertNotNull(resultado);
		assertEquals(pacienteFalso.getPNombre(), resultado.getP_nombre());
		assertEquals(pacienteFalso.getRut(), resultado.getRut());
		verify(pacienteRepository, times(1)).findById(idSimulado);
	}

	@Test
	void testBuscarPorId_NoEncontrado() {
		Integer idSimulado = 999;
		when(pacienteRepository.findById(idSimulado)).thenReturn(Optional.empty());

		assertThrows(RuntimeException.class, () -> pacienteService.buscarPorID(idSimulado));
		verify(pacienteRepository, times(1)).findById(idSimulado);
	}

	@Test
	void testBuscarPorRut_Exitoso() {
		String rutSimulado = "87654321";
		Paciente pacienteFalso = crearPacienteFalso(1, rutSimulado);

		when(pacienteRepository.findByRut(rutSimulado)).thenReturn(Optional.of(pacienteFalso));

		PacienteDTO resultado = pacienteService.buscarPorRut(rutSimulado);

		assertNotNull(resultado);
		assertEquals(pacienteFalso.getRut(), resultado.getRut());
		verify(pacienteRepository, times(1)).findByRut(rutSimulado);
	}

	@Test
	void testBuscarPorRut_NoEncontrado() {
		String rutSimulado = "99999999";
		when(pacienteRepository.findByRut(rutSimulado)).thenReturn(Optional.empty());

		assertThrows(RuntimeException.class, () -> pacienteService.buscarPorRut(rutSimulado));
		verify(pacienteRepository, times(1)).findByRut(rutSimulado);
	}

	@Test
	void testGuardarPaciente_Exitoso() {
		Paciente pacienteNuevo = crearPacienteFalso(null, "11111111");
		Paciente pacienteGuardado = crearPacienteFalso(1, "11111111");

		when(pacienteRepository.findByRut(pacienteNuevo.getRut())).thenReturn(Optional.empty());
		when(pacienteRepository.save(any(Paciente.class))).thenReturn(pacienteGuardado);

		PacienteDTO resultado = pacienteService.guardarPaciente(pacienteNuevo);

		assertNotNull(resultado);
		assertEquals(pacienteGuardado.getRut(), resultado.getRut());
		verify(pacienteRepository, times(1)).save(any(Paciente.class));
	}

	@Test
	void testGuardarPaciente_RutYaExiste() {
		Paciente pacienteNuevo = crearPacienteFalso(null, "22222222");
		Paciente pacienteExistente = crearPacienteFalso(1, "22222222");

		when(pacienteRepository.findByRut(pacienteNuevo.getRut())).thenReturn(Optional.of(pacienteExistente));

		assertThrows(RuntimeException.class, () -> pacienteService.guardarPaciente(pacienteNuevo));
		verify(pacienteRepository, times(0)).save(any(Paciente.class));
	}

	@Test
	void testActualizarPacientePorRut_Exitoso() {
		String rutSimulado = "44444444";
		Paciente pacienteExistente = crearPacienteFalso(1, rutSimulado);
		Paciente datoNuevo = crearPacienteFalso(1, "44444444");
		datoNuevo.setPNombre("NombreNuevo");
		datoNuevo.setCorreo("nuevocorreo@example.com");

		Paciente pacienteActualizado = crearPacienteFalso(1, "44444444");
		pacienteActualizado.setPNombre("NombreNuevo");

		when(pacienteRepository.findByRut(rutSimulado)).thenReturn(Optional.of(pacienteExistente));
		when(pacienteRepository.save(any(Paciente.class))).thenReturn(pacienteActualizado);

		Paciente resultado = pacienteService.actualizarPacientePorRut(rutSimulado, datoNuevo);

		assertNotNull(resultado);
		assertEquals("NombreNuevo", resultado.getPNombre());
		verify(pacienteRepository, times(1)).save(any(Paciente.class));
	}

	@Test
	void testActualizarPacientePorRut_NoEncontrado() {
		String rutSimulado = "55555555";
		Paciente datoNuevo = crearPacienteFalso(null, "55555555");

		when(pacienteRepository.findByRut(rutSimulado)).thenReturn(Optional.empty());

		assertThrows(RuntimeException.class, () -> pacienteService.actualizarPacientePorRut(rutSimulado, datoNuevo));
		verify(pacienteRepository, times(0)).save(any(Paciente.class));
	}

	@Test
	void testObtenerTodosLosPacientes_Exitoso() {
		Paciente paciente1 = crearPacienteFalso(1, "11111111");
		Paciente paciente2 = crearPacienteFalso(2, "22222222");

		List<Paciente> listaPacientes = Arrays.asList(paciente1, paciente2);

		when(pacienteRepository.findAll()).thenReturn(listaPacientes);

		List<PacienteDTO> resultado = pacienteService.obtenerTodosLosPacientes();

		assertNotNull(resultado);
		assertEquals(2, resultado.size());
		verify(pacienteRepository, times(1)).findAll();
	}

	@Test
	void testEliminarPacientePorRut_Exitoso() {
		String rutSimulado = "66666666";
		Paciente pacienteFalso = crearPacienteFalso(1, rutSimulado);

		when(pacienteRepository.findByRut(rutSimulado)).thenReturn(Optional.of(pacienteFalso));
		doNothing().when(pacienteRepository).delete(pacienteFalso);

		String resultado = pacienteService.eliminarPacientePorRut(rutSimulado);

		assertEquals("Paciente eliminado con éxito", resultado);
		verify(pacienteRepository, times(1)).delete(pacienteFalso);
	}

	@Test
	void testEliminarPacientePorRut_NoEncontrado() {
		String rutSimulado = "77777777";

		when(pacienteRepository.findByRut(rutSimulado)).thenReturn(Optional.empty());

		String resultado = pacienteService.eliminarPacientePorRut(rutSimulado);

		assertTrue(resultado.contains("no encontrado"));
		verify(pacienteRepository, times(0)).delete(any(Paciente.class));
	}

}