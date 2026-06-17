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

import com.example.ms_pacientes.DTO.PacienteDTO;
import com.example.ms_pacientes.model.Paciente;
import com.example.ms_pacientes.service.PacienteService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/pacientes")
@Tag(name = "Pacientes", description = "Operaciones relacionadas a Pacientes")
public class PacienteController {

    @Autowired
    private PacienteService pacienteService;

    //Obtener todos los pacientes
    @GetMapping
    @Operation(summary = "Muestra todos los pacientes", description = "Muestra una lista de todos los pacientes.")
    public ResponseEntity<List<PacienteDTO>> obtenerTodos() {
        List<PacienteDTO> pacientes = pacienteService.obtenerTodosLosPacientes();
        if (pacientes.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(pacientes, HttpStatus.OK);
    }

    //CONEXION
    //BUSCAR PACIENTE POR ID
    @GetMapping("/id/{id}")
    @Operation(summary = "Muestra al paciente mediante a su rut", description = "Muestra el paciente asociado al rut que ingresemos.")
    public ResponseEntity<Object> obtenerPorId(@PathVariable Integer id) {
        try {
            PacienteDTO paciente = pacienteService.buscarPorID(id);
            return new ResponseEntity<>(paciente, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }


    // Buscar por RUT
    @GetMapping("/rut/{rut}")
    @Operation(summary = "Muestra al paciente mediante a su rut", description = "Muestra el paciente asociado al rut que ingresemos.")
    public ResponseEntity<Object> obtenerPorRut(@PathVariable String rut) {
        try {
            PacienteDTO paciente = pacienteService.buscarPorRut(rut);
            return new ResponseEntity<>(paciente, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    // Guardar un nuevo paciente
    @PostMapping
    @Operation(summary = "Guarda el paciente en la BBDD", description = "Guarda el paciente en la Base de Datos.")
    public ResponseEntity<Object> registrar(@Valid @RequestBody Paciente paciente) {
        try {
            PacienteDTO dto = pacienteService.guardarPaciente(paciente);
            return new ResponseEntity<>(dto, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Error en la transmisión de datos", HttpStatus.BAD_REQUEST);
        }
    }

    //Actualizar paciente existente
    @PutMapping("/rut/{rut}")
    @Operation(summary = "Actualiza informacion de un paciente con RUT", description = "Actualiza la informacion de un paciente mediante el rut.")
    public ResponseEntity<Object> actualizar(@PathVariable String rut, @RequestBody Paciente paciente) {
        try {
            Paciente actualizado = pacienteService.actualizarPacientePorRut(rut, paciente);
            return new ResponseEntity<>(actualizado, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    //Eliminar paciente
    @DeleteMapping("/rut/{rut}")
    @Operation(summary = "Elimina un paciente de la BBDD con RUT", description = "Elimina a un paciente de la base de datos mediante el rut del paciente al que queremos eliminar.")
    public ResponseEntity<String> eliminar(@PathVariable String rut) {
        try {
            pacienteService.eliminarPacientePorRut(rut);
            return new ResponseEntity<>("Paciente eliminado exitosamente", HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

}
