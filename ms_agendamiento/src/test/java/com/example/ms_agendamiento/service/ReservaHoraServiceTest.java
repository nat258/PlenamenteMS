package com.example.ms_agendamiento.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.ms_agendamiento.DTO.ReservaHoraDTO;
import com.example.ms_agendamiento.model.Boleta;
import com.example.ms_agendamiento.model.ReservaHora;
import com.example.ms_agendamiento.repository.ReservaHoraRepository;

import net.datafaker.Faker;

@ExtendWith(MockitoExtension.class)
public class ReservaHoraServiceTest {

    @Mock
    private ReservaHoraRepository reservaHoraRepository;

    @InjectMocks
    private ReservaHoraService reservaHoraService;

    private Faker faker = new Faker();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void buscarReservaHoraPorId_Exito() {
        // GIVEN: Dado un escenario inicial
        Integer idSimulado = faker.number().numberBetween(1, 150);
        
        ReservaHora reservaFalsa = new ReservaHora();
        reservaFalsa.setId(idSimulado);
        reservaFalsa.setEstado("CONFIRMADA");
        reservaFalsa.setFechaHora(LocalDateTime.now().plusDays(2));

        when(reservaHoraRepository.findById(idSimulado)).thenReturn(Optional.of(reservaFalsa));

        // WHEN
        ReservaHoraDTO resultado = reservaHoraService.buscarReservaHoraPorId(idSimulado);

        // THEN
        assertNotNull(resultado, "El DTO resultante no debe ser nulo");
        assertEquals("CONFIRMADA", resultado.getEstado());
        verify(reservaHoraRepository, times(1)).findById(idSimulado);
    }

    @Test
    void eliminarBoletaDeReserva_Exito_Y_CambiaEstado() {
        // GIVEN
        Integer idReserva = faker.number().numberBetween(10, 100);

        Boleta boletaFalsa = new Boleta();
        boletaFalsa.setId(5);

        ReservaHora reservaFalsa = new ReservaHora();
        reservaFalsa.setId(idReserva);
        reservaFalsa.setEstado("PAGADO");
        reservaFalsa.setBoleta(boletaFalsa);

        when(reservaHoraRepository.findById(idReserva)).thenReturn(Optional.of(reservaFalsa));

        // WHEN
        String mensaje = reservaHoraService.eliminarBoletaDeReserva(idReserva);

        // THEN
        assertEquals("La boleta ha sido eliminada de la reserva " + idReserva, mensaje);
        assertNull(reservaFalsa.getBoleta(), "La boleta debe quedar nula (desvinculada).");
        assertEquals("PENDIENTE", reservaFalsa.getEstado(), "El estado de la reserva debe regresar a PENDIENTE.");
        verify(reservaHoraRepository, times(1)).save(reservaFalsa);
    }

    @Test
    void eliminarBoletaDeReserva_Fallo_SinBoleta() {
        // GIVEN
        Integer idReserva = faker.number().numberBetween(1, 100);

        ReservaHora reservaFalsa = new ReservaHora();
        reservaFalsa.setId(idReserva);
        reservaFalsa.setBoleta(null); // Ojo aquí, no tiene boleta

        when(reservaHoraRepository.findById(idReserva)).thenReturn(Optional.of(reservaFalsa));

        // WHEN and THEN
        RuntimeException excepcion = assertThrows(RuntimeException.class, () -> {
            reservaHoraService.eliminarBoletaDeReserva(idReserva);
        });

        assertEquals("Esta reserva no tiene ninguna boleta asociada.", excepcion.getMessage());
        verify(reservaHoraRepository, never()).save(any()); // Validamos que jamás intentó guardar
    }

    @Test
    void actualizarReserva_Fallo_ChoqueDeHorario() {
        // GIVEN
        Integer idReserva = faker.number().numberBetween(1, 100);
        Integer idPsicologo = faker.number().numberBetween(1, 10);
        LocalDateTime horaAntigua = LocalDateTime.now().plusDays(1);
        LocalDateTime horaNueva = LocalDateTime.now().plusDays(2);

        ReservaHora reservaExistente = new ReservaHora();
        reservaExistente.setId(idReserva);
        reservaExistente.setPsicologoId(idPsicologo);
        reservaExistente.setFechaHora(horaAntigua);

        ReservaHoraDTO reservaActualizada = new ReservaHoraDTO();
        reservaActualizada.setPsicologoId(idPsicologo);
        reservaActualizada.setPacienteId(1);
        reservaActualizada.setFechaHora(horaNueva);

        List<ReservaHora> listaChoques = new ArrayList<>();
        listaChoques.add(new ReservaHora());

        when(reservaHoraRepository.findById(idReserva)).thenReturn(Optional.of(reservaExistente));
        when(reservaHoraRepository.findByPsicologoIdAndFechaHora(idPsicologo, horaNueva)).thenReturn(listaChoques);

        // WHEN and THEN
        RuntimeException excepcion = assertThrows(RuntimeException.class, () -> {
            reservaHoraService.actualizarReserva(idReserva, reservaActualizada);
        });

        assertEquals("No se puede cambiar la cita, el nuevo horario ya esta ocupado.", excepcion.getMessage());
        verify(reservaHoraRepository, never()).save(any());
    }

}