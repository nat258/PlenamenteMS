package com.example.ms_agendamiento.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.ms_agendamiento.DTO.ReservaHoraDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

import com.example.ms_agendamiento.service.ReservaHoraService;

import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/v1/reservas")
@Tag(name = "Reservas", description = "Operaciones para el agendamiento de citas medicas")
public class ReservaHoraController {

    @Autowired
    private ReservaHoraService reservaService;

    @Operation(summary = "Obtener todas las reservas", description = "Retorna una lista de todas las reservas medicas registradas en el sistema.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente",
                     content = @Content(mediaType = "application/json",
                     schema = @Schema(implementation = ReservaHoraDTO.class))),
        @ApiResponse(responseCode = "204", description = "No hay reservas registradas (sin contenido).")
    })
    @GetMapping
    public ResponseEntity<List<ReservaHoraDTO>> obtenerTodos() {
        List<ReservaHoraDTO> reservas = reservaService.obtenerReservas();

        if (reservas.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(reservas, HttpStatus.OK);
    }


    @Operation(summary = "Buscar reserva por ID", description = "Busca y retorna los detalles de una reserva medica usando su identificador unico.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Reserva encontrada exitosamente",
                     content = @Content(mediaType = "application/json",
                     schema = @Schema(implementation = ReservaHoraDTO.class))),
        @ApiResponse(responseCode = "404", description = "La reserva no fue encontrada en la base de datos.")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Object> buscarPorId(@PathVariable Integer id) {
        try {
            ReservaHoraDTO encontrado = reservaService.buscarReservaHoraPorId(id);
            return new ResponseEntity<>(encontrado, HttpStatus.OK);
        } catch (RuntimeException e) {
           return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }


    @Operation(summary = "Agendar una nueva reserva", description = "Crea una nueva reserva medica validando primero la existencia del paciente y psicologo a traves de sus respectivos microservicios.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Reserva creada exitosamente",
                     content = @Content(mediaType = "application/json",
                     schema = @Schema(implementation = ReservaHoraDTO.class))),
        @ApiResponse(responseCode = "400", description = "Error de validacion (ej: el paciente no existe, el psicologo no existe o hay choque de horario)")
    })
    @PostMapping
    public ResponseEntity<Object> guardarReserva(@RequestBody ReservaHoraDTO reservaDTO) {
        try {
            ReservaHoraDTO nuevo = reservaService.guardarReserva(reservaDTO);

            return new ResponseEntity<>(nuevo, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    @Operation(summary = "Actualizar una reserva", description = "Permite modificar la fecha, hora o estado de una reserva existente, validando que el nuevo horario no choque con otras citas del psicologo")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Reserva actualizada exitosamente",
                     content = @Content(mediaType = "application/json",
                     schema = @Schema(implementation = ReservaHoraDTO.class))),
        @ApiResponse(responseCode = "400", description = "Error de validacion (ej: El nuevo horario ya esta ocupado)."),
        @ApiResponse(responseCode = "404", description = "La reserva a actualizar no fue encontrada.")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Object> actualizarReserva(@PathVariable Integer id, @RequestBody ReservaHoraDTO reservaAct) {
        try {
            ReservaHoraDTO actualizada = reservaService.actualizarReserva(id, reservaAct);
            return new ResponseEntity<>(actualizada, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    @Operation(summary = "Eliminar una reserva", description = "Elimina permanentemente una reserva medica del sistema a traves de su ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Reserva eliminada con exito"),
        @ApiResponse(responseCode = "404", description = "La reserva no existe o ya fue eliminada.")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarReserva(@PathVariable Integer id) {
        try {
            String mensaje = reservaService.eliminarReserva(id);

            return new ResponseEntity<>(mensaje, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }


    @Operation(summary = "Desvincular boleta de la reserva", description = "Elimina la relacion de pago de una cita y devuelve su estado a PENDIENTE.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Boleta desvinculada exitosamente."),
        @ApiResponse(responseCode = "400", description = "Error logico (ej: La reserva no tenía boleta asociada)."),
        @ApiResponse(responseCode = "404", description = "La reserva no fue encontrada.")
    })
    @DeleteMapping("/{id}/boleta")
    public ResponseEntity<String> eliminarBoletaDeReserva(@PathVariable Integer id) {
        try {
            String mensaje = reservaService.eliminarBoletaDeReserva(id);
            return new ResponseEntity<>(mensaje, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

}
