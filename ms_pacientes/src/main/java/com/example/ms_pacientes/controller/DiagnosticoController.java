package com.example.ms_pacientes.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.ms_pacientes.DTO.DiagnosticoDTO;
import com.example.ms_pacientes.model.Diagnostico;
import com.example.ms_pacientes.service.DiagnosticoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/diagnosticos")
@Tag(name = "Diagnosticos", description = "Operaciones relacionadas a diagnosticos")
public class DiagnosticoController {

    private final DiagnosticoService diagnosticoService;


    DiagnosticoController(DiagnosticoService diagnosticoService) {
        this.diagnosticoService = diagnosticoService;
    }
    

    @GetMapping
    @Operation(summary = "Muestra todos los diagnosticos", description = "Muestra una lista de todos los diagnosticos existentes.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Diagnosticos encontrados exitosamente",
                content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = DiagnosticoDTO.class))),
            @ApiResponse(responseCode = "204", description = "No se encontraron diagnosticos")
    })
    public ResponseEntity<List<DiagnosticoDTO>> obtenerTodos() {
        List<DiagnosticoDTO> diagnosticos = diagnosticoService.obtenerTodos();
        if (diagnosticos.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(diagnosticos, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca y muestra un diagnostico por ID", description = "Muestra un diagnostico a base de la ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Diagnostico encontrado",
                content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = DiagnosticoDTO.class))),
            @ApiResponse(responseCode = "404", description = "Diagnostico no encontrado")
    })
    public ResponseEntity<Object> buscarPorId(@PathVariable Integer id) {
        try {
            DiagnosticoDTO encontrado = diagnosticoService.buscarPorId(id);
            return new ResponseEntity<>(encontrado, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/rut/{rut}")
    @Operation(summary = "Busca y muestra un diagnostico por rut", description = "Muestra un diagnostico a base del rut del paciente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Diagnostico encontrado",
                content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = DiagnosticoDTO.class))),
            @ApiResponse(responseCode = "404", description = "Diagnostico no encontrado")
    })
    public ResponseEntity<Object> buscarPorRut(@PathVariable String rut) {
        try {
            DiagnosticoDTO encontrador = diagnosticoService.buscarPorRut(rut);
            return new ResponseEntity<>(encontrador, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }


    @PostMapping
    @Operation(summary = "Guarda un diagostico", description = "Guarda un diagostico en la base de datos.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Diagnostico creado",
                content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = DiagnosticoDTO.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud incorrecta")
    })
    public ResponseEntity<Object> guardarDiagnostico(@Valid @RequestBody Diagnostico diagnostico) {
        try {
            DiagnosticoDTO nuevo = diagnosticoService.guardarDiagnostico(diagnostico);
            return new ResponseEntity<>(nuevo, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualiza un diagnostico por el id", description = "Actualiza un diagnostico por el id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Diagnostico actualizado",
                content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = DiagnosticoDTO.class))),
            @ApiResponse(responseCode = "404", description = "Diagnostico no encontrado"),
            @ApiResponse(responseCode = "400", description = "Solicitud incorrecta")
    })
    public ResponseEntity<?> actualizarDiagnostico(@PathVariable Integer id, @RequestBody Diagnostico diagnostico) {
        try {
            DiagnosticoDTO actualizado = diagnosticoService.actualizarDiagnostico(id, diagnostico);
            return new ResponseEntity<>(actualizado, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Elimina un diagnostico por la id", description = "Elimina un diagostico en la base de datos a base de la id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Diagnostico eliminado",
                content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "404", description = "Diagnostico no encontrado"),
            @ApiResponse(responseCode = "400", description = "Solicitud incorrecta")
    })
    public ResponseEntity<String> eliminarDiagnostico(@PathVariable Integer id) {
        String mensaje = diagnosticoService.eliminarDiagnostico(id);
        if (mensaje.contains("no se encuentra registrado")) {
            return new ResponseEntity<>(mensaje, HttpStatus.NOT_FOUND);
        }

        if (mensaje.contains("Error: No se puede eliminar")) {
            return new ResponseEntity<>(mensaje, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(mensaje, HttpStatus.OK);
    }

}
