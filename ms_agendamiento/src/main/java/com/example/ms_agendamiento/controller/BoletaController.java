package com.example.ms_agendamiento.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

import com.example.ms_agendamiento.DTO.BoletaDTO;
import com.example.ms_agendamiento.service.BoletaService;

import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/v1/boletas")
@Tag(name = "Boletas", description = "Operaciones relacionadas con el registro de pagos y facturacion de reservas.")
public class BoletaController {

    private final BoletaService boletaService;


    BoletaController(BoletaService boletaService) {
        this.boletaService = boletaService;
    }


    @Operation(summary = "Obtener todas las boletas", description = "Retorna el historial completo de todas las boletas emitidas dentro del sistema.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente.",
                     content = @Content(mediaType = "application/json",
                     schema = @Schema(implementation = BoletaDTO.class))),
        @ApiResponse(responseCode = "204", description = "No hay boletas registradas (sin contenido).")
    })
    @GetMapping
    public ResponseEntity<List<BoletaDTO>> obtenerTodos() {
        List<BoletaDTO> boletas = boletaService.obtenerBoletas();

        if (boletas.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        
        return new ResponseEntity<>(boletas, HttpStatus.OK);
    }


    @Operation(summary = "Emitir una nueva boleta", description = "Crea un registro de pago asociado a una reserva medica existente.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Boleta emitida y guardada exitosamente.",
                     content = @Content(mediaType = "application/json",
                     schema = @Schema(implementation = BoletaDTO.class))),
        @ApiResponse(responseCode = "400", description = "Error de validacion (ej: datos incompletos o reserva no valida).")
    })
    @PostMapping
    public ResponseEntity<Object> guardarBoleta(@RequestBody BoletaDTO boletaDTO) {
        try {
            BoletaDTO nuevo = boletaService.guardarBoleta(boletaDTO);

            return new ResponseEntity<>(nuevo, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    @Operation(summary = "Anular una boleta", description = "Elimina una boleta del registro financiero y desvincula automaticamente el pago de su reserva asociada, dejandola en estado PENDIENTE.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Boleta anulada y eliminada con exito."),
        @ApiResponse(responseCode = "404", description = "La boleta indicada no existe en el sistema.")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarBoleta(@PathVariable Integer id) {
        try {
            String mensaje = boletaService.eliminar(id);

            return new ResponseEntity<>(mensaje, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }


    @Operation(summary = "Buscar boleta por ID", description = "Busca y retorna los detalles financieros de una boleta especifica usando su identificador unico.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Boleta encontrada exitosamente.",
                     content = @Content(mediaType = "application/json",
                     schema = @Schema(implementation = BoletaDTO.class))),
        @ApiResponse(responseCode = "404", description = "La boleta no fue encontrada en la base de datos.")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Object> buscarPorId(@PathVariable Integer id) {
        try {
            BoletaDTO encontrado = boletaService.buscarPorId(id);

            return new ResponseEntity<>(encontrado, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }


    @Operation(summary = "Filtrar boletas por monto", description = "Busca y retorna una lista de todas las boletas que coincidan exactamente con el monto de pago especificado.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Boletas encontradas exitosamente.",
                     content = @Content(mediaType = "application/json",
                     schema = @Schema(implementation = BoletaDTO.class))),
        @ApiResponse(responseCode = "404", description = "No se encontraron boletas con ese monto.")
    })
    @GetMapping("/montoExacto/{monto}")
    public ResponseEntity<Object> buscarPorMonto(@PathVariable Integer monto) {
        try {
            List<BoletaDTO> boletas = boletaService.buscarMontoExacto(monto);

            return new ResponseEntity<>(boletas, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

}
