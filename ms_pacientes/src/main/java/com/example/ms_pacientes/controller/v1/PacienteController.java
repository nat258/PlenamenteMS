package com.example.ms_pacientes.controller.v1;

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

import com.example.ms_pacientes.DTO.PacienteDTO;
import com.example.ms_pacientes.model.Paciente;
import com.example.ms_pacientes.service.PacienteService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController("PacienteControllerV1")
@RequestMapping("/api/v1/pacientes")
@Tag(name = "Pacientes", description = "Operaciones relacionadas a Pacientes")
public class PacienteController {

    private final PacienteService pacienteService;

    PacienteController(PacienteService pacienteService) {
        this.pacienteService = pacienteService;
    }

    //Obtener todos los pacientes
    @GetMapping
    @Operation(summary = "Muestra todos los pacientes", description = "Muestra una lista de todos los pacientes.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de pacientes obtenida exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PacienteDTO.class))),
            @ApiResponse(responseCode = "204", description = "No hay pacientes registrados")
    })
    public ResponseEntity<List<PacienteDTO>> obtenerTodos() {
        List<PacienteDTO> pacientes = pacienteService.obtenerTodosLosPacientes();
        if (pacientes.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(pacientes, HttpStatus.OK);
    }

    //CONEXION
    //BUSCAR PACIENTE POR ID funcionando
    @GetMapping("/id/{id}")
    @Operation(summary = "Muestra al paciente mediante a su id", description = "Muestra el paciente asociado al id que ingresemos.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Paciente encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PacienteDTO.class))),
            @ApiResponse(responseCode = "204", description = "Paciente no encontrado")
    })
    public ResponseEntity<Object> obtenerPorId(@PathVariable Integer id) {
        try {
            PacienteDTO paciente = pacienteService.buscarPorID(id);
            return new ResponseEntity<>(paciente, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }


    // Buscar por RUT funcionando
    @GetMapping("/rut/{rut}")
    @Operation(summary = "Muestra al paciente mediante a su rut", description = "Muestra el paciente asociado al rut que ingresemos.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Paciente encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PacienteDTO.class))),
            @ApiResponse(responseCode = "204", description = "Paciente no encontrado")
    })
    public ResponseEntity<Object> obtenerPorRut(@PathVariable String rut) {
        try {
            PacienteDTO paciente = pacienteService.buscarPorRut(rut);
            return new ResponseEntity<>(paciente, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    // Guardar un nuevo paciente funcionando
    @PostMapping
    @Operation(summary = "Guarda el paciente en la BBDD", description = "Guarda el paciente en la Base de Datos.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Paciente registrado exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PacienteDTO.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud incorrecta")
    })
    public ResponseEntity<Object> registrar(@Valid @RequestBody Paciente paciente) {
        try {
            PacienteDTO dto = pacienteService.guardarPaciente(paciente);
            return new ResponseEntity<>(dto, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Error en la transmisión de datos", HttpStatus.BAD_REQUEST);
        }
    }

    //Actualizar paciente existente funcionando
    @PutMapping("/rut/{rut}")
    @Operation(summary = "Actualiza informacion de un paciente con RUT", description = "Actualiza la informacion de un paciente mediante el rut.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Paciente actualizado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PacienteDTO.class))),
            @ApiResponse(responseCode = "404", description = "Paciente no encontrado")
    })
    public ResponseEntity<Object> actualizar(@PathVariable String rut, @RequestBody Paciente paciente) {
        try {
            Paciente actualizado = pacienteService.actualizarPacientePorRut(rut, paciente);
            return new ResponseEntity<>(actualizado, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    //Eliminar paciente funcionando
    @DeleteMapping("/rut/{rut}")
    @Operation(summary = "Elimina un paciente de la BBDD con RUT", description = "Elimina a un paciente de la base de datos mediante el rut del paciente al que queremos eliminar.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Paciente eliminado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PacienteDTO.class))),
            @ApiResponse(responseCode = "404", description = "Paciente no encontrado")
    })
    public ResponseEntity<String> eliminar(@PathVariable String rut) {
        try {
            pacienteService.eliminarPacientePorRut(rut);
            return new ResponseEntity<>("Paciente eliminado exitosamente", HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

}
