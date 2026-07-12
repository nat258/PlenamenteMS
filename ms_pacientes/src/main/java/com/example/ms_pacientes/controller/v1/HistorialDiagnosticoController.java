package com.example.ms_pacientes.controller.v1;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.ms_pacientes.DTO.HistorialDiagnosticoDTO;
import com.example.ms_pacientes.model.HistorialDiagnostico;
import com.example.ms_pacientes.service.HistorialDiagnosticoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController("HistorialDiagnosticoControllerV1")
@RequestMapping("/api/v1/historiales")
@Tag(name = "Historiales Diagnosticos", description = "Operaciones relacionadas a Historiales de diagnosticos")
public class HistorialDiagnosticoController {

    private final HistorialDiagnosticoService historialService;

    HistorialDiagnosticoController(HistorialDiagnosticoService historialService) {
        this.historialService = historialService;
    }

    @GetMapping
    @Operation(summary = "Muestra todos el historial de diagnosticos", description = "Muestra todos el historial de diagnosticos.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Historiales de diagnosticos encontrados",
                content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = HistorialDiagnosticoDTO.class))),
            @ApiResponse(responseCode = "204", description = "No se encontraron historiales de diagnosticos")
    })
    public ResponseEntity<List<HistorialDiagnosticoDTO>> obtenerTodos() {
        List<HistorialDiagnosticoDTO> lista = historialService.obtenerTodos();
        if (lista.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(lista, HttpStatus.OK);
    }

    @PostMapping
    @Operation(summary = "Guarda el historial de diagnosticos", description = "Guarda el historial de diagnosticos.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Historial de diagnosticos creado",
                content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = HistorialDiagnosticoDTO.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud incorrecta")
    })
    public ResponseEntity<?> guardarHistorial(@Valid @RequestBody HistorialDiagnostico historial) {
        try {
            HistorialDiagnosticoDTO nuevo = historialService.guardarHistorial(historial);
            return new ResponseEntity<>(nuevo, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualiza el historial de diagnosticos mediante el id", description = "Actualiza el historial de diagnosticos mediante el id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Historial de diagnosticos actualizado",
                content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = HistorialDiagnosticoDTO.class))),
            @ApiResponse(responseCode = "404", description = "Historial de diagnosticos no encontrado"),
            @ApiResponse(responseCode = "400", description = "Solicitud incorrecta")
    })
    public ResponseEntity<?> actualizarHistorial(@PathVariable Integer id, @RequestBody HistorialDiagnostico historial) {
        try {
            HistorialDiagnosticoDTO actualizado = historialService.actualizarHistorial(id, historial);
            return new ResponseEntity<>(actualizado, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/paciente/{id}")
    @Operation(summary = "Muestra y busca todo el historial de diagnosticos por el id del paciente", description = "Muestra y busca todo el historial de diagnosticos por el id del paciente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Historiales de diagnosticos encontrados",
                content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = HistorialDiagnosticoDTO.class))),
            @ApiResponse(responseCode = "204", description = "No se encontraron historiales de diagnosticos")
    })
    public ResponseEntity<?> buscarPorPaciente(@PathVariable Integer id) {
        try {
            List<HistorialDiagnosticoDTO> ficha = historialService.diagnosticoPorIdPaciente(id);

            if (ficha.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(ficha, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca y muestra el historial de diagnosticos por el id", description = "Busca y muestra el historial de diagnosticos por el id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Historial de diagnosticos encontrado",
                content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = HistorialDiagnosticoDTO.class))),
            @ApiResponse(responseCode = "404", description = "Historial de diagnosticos no encontrado")
    })
    public ResponseEntity<?> buscarPorId(@PathVariable Integer id) {
        try {
            HistorialDiagnosticoDTO historial = historialService.obtenerPorId(id);
            return new ResponseEntity<>(historial, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/diagnostico/{id}")
    @Operation(summary = "Busca y muestra el historial de diagnosticos por el id de diagnostico", description = "Busca y muestra el historial de diagnosticos por el id de diagnostico.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Historial de diagnosticos encontrado",
                content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = HistorialDiagnosticoDTO.class))),
            @ApiResponse(responseCode = "204", description = "No se encontró el historial de diagnosticos")
    })
    public ResponseEntity<Object> buscarPorDiagnostico(@PathVariable Integer id) {
        try {
            Integer idHistorial = historialService.buscarIdPorDiagnostico(id);
        
            if (idHistorial == null) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(idHistorial, HttpStatus.OK);
        
    } catch (RuntimeException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }
    }


    @GetMapping("/fecha/{fecha}")
    @Operation(summary = "Busca y muestra historial de diagnosticos por la fecha", description = "Muestra y busca el historial de diagnosticos por la fecha.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Historial de diagnosticos encontrado",
                content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = HistorialDiagnosticoDTO.class))),
            @ApiResponse(responseCode = "204", description = "No se encontró el historial de diagnosticos")
    })
    public ResponseEntity<?> buscarPorFecha(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        try {
            List<HistorialDiagnosticoDTO> resultados = historialService.buscarPorFecha(fecha);
            return new ResponseEntity<>(resultados, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

}
