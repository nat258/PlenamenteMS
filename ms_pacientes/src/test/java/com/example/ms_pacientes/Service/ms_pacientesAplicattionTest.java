package com.example.ms_pacientes.Service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.ms_pacientes.DTO.PacienteDTO;
import com.example.ms_pacientes.model.Paciente;
import com.example.ms_pacientes.repository.PacienteRepository;
import com.example.ms_pacientes.service.PacienteService;

import net.datafaker.Faker;

@ExtendWith(MockitoExtension.class)
class MsPacientesApplicationTests {

	@Mock
	private PacienteRepository pacienteRepository;
	
	@InjectMocks
	private PacienteService pacienteService;
	private Faker faker = new Faker();
	@BeforeEach
	void setUp() {
		// Inicializa los componentes de simulación antes de ejecutar cada prueba
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testBuscarPorId_Exitoso() {
		// GIVEN: Dado un escenario inicial en la galaxia
		Integer idSimulado = 42;
		String p_nombreAleatorio = faker.name().firstName();
		String s_nombreAleatorio = faker.name().firstName();
		String p_apellidoAleatorio = faker.name().lastName();
		String s_apellidoAleatorio = faker.name().lastName();
		Paciente pacienteFalso = new Paciente();
		pacienteFalso.setId(idSimulado);
		pacienteFalso.setRut(faker.number().digits(8));
		pacienteFalso.setPNombre(p_nombreAleatorio);
		pacienteFalso.setSNombre(s_nombreAleatorio);
		pacienteFalso.setPApellido(p_apellidoAleatorio);
		pacienteFalso.setSApellido(s_apellidoAleatorio);
		pacienteFalso.setCorreo(faker.internet().emailAddress());
		pacienteFalso.setNumero(faker.number().digits(10));
		// Entrenamos al Mock: Cuando el repositorio busque este ID, responderá con nuestro PacienteFalso
		when(pacienteRepository.findById(idSimulado)).thenReturn(Optional.of(pacienteFalso));
		// WHEN: Cuando ejecutamos la acción del servicio que queremos evaluar

		PacienteDTO resultado = pacienteService.buscarPorID(idSimulado);
		// THEN: Entonces validamos que las compuertas de datos funcionen de forma idónea
		assertNotNull(resultado, "El DTO resultante no debería ser nulo");
		assertEquals(p_nombreAleatorio, resultado.getP_nombre(), "El primer nombre transformado al DTO debe coincidir con el de la DB");
		// Verificamos que el servicio realmente haya consultado al repositorio exactamente 1 vez
		verify(pacienteRepository, times(1)).findById(idSimulado);
	}
}