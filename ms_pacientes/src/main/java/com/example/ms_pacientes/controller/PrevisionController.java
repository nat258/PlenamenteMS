package com.example.ms_pacientes.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.example.ms_pacientes.DTO.PrevisionDTO;
import com.example.ms_pacientes.model.Prevision;
import com.example.ms_pacientes.service.PrevisionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/prevision")
@Tag(name = "Previsiones", description = "Operaciones relacionadas a Previsiones")
public class PrevisionController {

    @Autowired
    private PrevisionService previsionService;

    @GetMapping
    @Operation(summary = "Muestra todas las previsiones.", description = "muestra todas las previsiones existentes.")
    public ResponseEntity<List<PrevisionDTO>> obtenerTodas() {
        List<PrevisionDTO> lista = previsionService.obtenerTodosLasPrevisiones();
        if (lista.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(lista, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca una prevension mediante su id", description = "Busca la prevision asociada a la id de esta")
    public ResponseEntity<Object> buscarPorId(@PathVariable Integer id) {
        try {
            PrevisionDTO dto = previsionService.buscarPrevisionPorId(id);
            return new ResponseEntity<>(dto, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    @Operation(summary = "Guarda prevision", description = "guarda la prevision en la base de datos")
    public ResponseEntity<Object> guardar(@Valid @RequestBody Prevision prevision) {
        try {
            Prevision guardada = previsionService.guardarPrevision(prevision);
            return new ResponseEntity<>(guardada, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualiza la prevision", description = "Axtualiza los datos de la prevision asociada a su id")
    public ResponseEntity<?> actualizar(@PathVariable Integer id, @RequestBody Prevision prevision) {
        try {
            return ResponseEntity.ok(previsionService.actualizarPrevision(id, prevision));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Elimina una prevision", description = "Elimina una prevision asociada a su id")
    public ResponseEntity<String> eliminar(@PathVariable Integer id) {
        try {
            previsionService.eliminarPrevision(id);
            return new ResponseEntity<>("Previsión eliminada exitosamente", HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

}
