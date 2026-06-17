package com.example.ms_pacientes.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
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

import com.example.ms_pacientes.DTO.HistorialDiagnosticoDTO;
import com.example.ms_pacientes.model.HistorialDiagnostico;
import com.example.ms_pacientes.service.HistorialDiagnosticoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/historiales")
@Tag(name = "Historiales Diagnosticos", description = "Operaciones relacionadas a Historiales de diagnosticos")
public class HistorialDiagnosticoController {

    @Autowired
    private HistorialDiagnosticoService historialService;

    @GetMapping
    @Operation(summary = "Muestra todos el historial de diagnosticos", description = "Muestra todos el historial de diagnosticos.")
    public ResponseEntity<List<HistorialDiagnosticoDTO>> obtenerTodos() {
        List<HistorialDiagnosticoDTO> lista = historialService.obtenerTodos();
        if (lista.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(lista, HttpStatus.OK);
    }

    @PostMapping
    @Operation(summary = "Guarda el historial de diagnosticos", description = "Guarda el historial de diagnosticos.")
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
    public ResponseEntity<?> actualizarHistorial(@PathVariable Integer id, @RequestBody HistorialDiagnostico historial) {
        try {
            HistorialDiagnosticoDTO actualizado = historialService.actualizarHistorial(id, historial);
            return new ResponseEntity<>(actualizado, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Elimina historial de diagnosticos por su id", description = "Elimina el historial de diagnosticos por el id.")
    public ResponseEntity<String> eliminarHistorial(@PathVariable Integer id) {
        String mensaje = historialService.eliminarHistorial(id);
        if (mensaje.contains("no se encuentra registrado")) {
            return new ResponseEntity<>(mensaje, HttpStatus.NOT_FOUND);
        }

        if (mensaje.contains("No se puede eliminar")) {
            return new ResponseEntity<>(mensaje, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(mensaje, HttpStatus.OK);
    }

    @GetMapping("/paciente/{id}")
    @Operation(summary = "Muestra y busca todo el historial de diagnosticos por el id del paciente", description = "Muestra y busca todo el historial de diagnosticos por el id del paciente.")
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

    @GetMapping("/diagnostico/{id}")
    @Operation(summary = "Busca y muestra el historial de diagnosticos por el id de diagnostico", description = "Busca y muestra el historial de diagnosticos por el id de diagnostico.")
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
    public ResponseEntity<?> buscarPorFecha(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        try {
            List<HistorialDiagnosticoDTO> resultados = historialService.buscarPorFecha(fecha);
            return new ResponseEntity<>(resultados, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

}
