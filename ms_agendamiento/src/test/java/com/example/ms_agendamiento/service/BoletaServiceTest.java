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

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.ms_agendamiento.DTO.BoletaDTO;
import com.example.ms_agendamiento.model.Boleta;
import com.example.ms_agendamiento.model.ReservaHora;
import com.example.ms_agendamiento.repository.BoletaRepository;
import com.example.ms_agendamiento.repository.ReservaHoraRepository;

import net.datafaker.Faker;

@ExtendWith(MockitoExtension.class)
public class BoletaServiceTest {

    @Mock
    private BoletaRepository boletaRepository;

    @Mock
    private ReservaHoraRepository reservaHoraRepository;

    @InjectMocks
    private BoletaService boletaService;

    private Faker faker = new Faker();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void buscarPorIdExito() {
        // GIVEN
        Integer idSimulado = faker.number().numberBetween(1, 150);
        Integer montoAleatorio = faker.number().numberBetween(10000, 50000);

        Boleta boletaFalsa = new Boleta();
        boletaFalsa.setId(idSimulado);
        boletaFalsa.setMonto(montoAleatorio);
        boletaFalsa.setTipoPago("DEBITO");

        when(boletaRepository.findById(idSimulado)).thenReturn(Optional.of(boletaFalsa));

        // WHEN
        BoletaDTO resultado = boletaService.buscarPorId(idSimulado);

        // THEN
        assertNotNull(resultado, "El DTO no debe ser nulo");
        assertEquals(montoAleatorio, resultado.getMonto());
        verify(boletaRepository, times(1)).findById(idSimulado);
    }

    @Test
    void eliminarBoleta_Exito_Y_CambiaEstadoReserva() {
        // GIVEN
        Integer idBoleta = faker.number().numberBetween(10, 50);
        
        ReservaHora reservaFalsa = new ReservaHora();
        reservaFalsa.setId(1);
        reservaFalsa.setEstado("PAGADO");

        Boleta boletaFalsa = new Boleta();
        boletaFalsa.setId(idBoleta);
        boletaFalsa.setReservaHora(reservaFalsa);

        when(boletaRepository.findById(idBoleta)).thenReturn(Optional.of(boletaFalsa));

        // WHEN
        String mensaje = boletaService.eliminar(idBoleta);

        // THEN
        assertEquals("La boleta " + idBoleta + " ha sido eliminada exitosamente.", mensaje);
        assertNull(reservaFalsa.getBoleta()); // Verifica regla de negocio
        assertEquals("PENDIENTE", reservaFalsa.getEstado());

        verify(reservaHoraRepository, times(1)).save(reservaFalsa);
        verify(boletaRepository, times(1)).delete(boletaFalsa);
    }

    @Test
    void eliminarBoleta_Fallo_IdNoExiste() {
        // GIVEN
        Integer idInvalido = 999;

        when(boletaRepository.findById(idInvalido)).thenReturn(Optional.empty());
        
        // WHEN and THEN
        RuntimeException excepcion = assertThrows(RuntimeException.class, () -> {
            boletaService.eliminar(idInvalido);
        });

        assertEquals("Error! La boleta con ID " + idInvalido + " no fue encontrada.", excepcion.getMessage());
        verify(boletaRepository, never()).delete(any());
    }

    @Test
    void buscarPorId_Fallo_NoEncontrado() {
        // GIVEN
        Integer idInvalido = faker.number().numberBetween(500, 1000);
        when(boletaRepository.findById(idInvalido)).thenReturn(Optional.empty());

        // WHEN and THEN
        RuntimeException excepcion = assertThrows(RuntimeException.class, () -> {
            boletaService.buscarPorId(idInvalido);
        });

        assertEquals("Error! La boleta con ID " + idInvalido + " no fue encontrada.", excepcion.getMessage());
    }

}
