package com.example.ms_profesionales.controller;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.ms_profesionales.DTO.EspecialidadDTO;
import com.example.ms_profesionales.service.EspecialidadService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;


@RestController
@RequestMapping("/api/v1/especialidades")
@Tag(name = "Especialidad Controller", description = "Endpoints para gestionar especialidades")
public class EspecialidadController {

    private final EspecialidadService especialidadService;

    EspecialidadController(EspecialidadService especialidadService) {
        this.especialidadService = especialidadService;
    }

    //Busqueda por coincidencia parcial de nombre 
    @GetMapping("/nombre/{nombre}")
    @Operation(summary = "Buscar especialidad por nombre parcial", description = "Obtiene una o más especialidades específicas utilizando una parte de su nombre.")
    @ApiResponse(responseCode = "200", description = "Especialidades encontradas exitosamente")
    @ApiResponse(responseCode = "404", description = "Especialidades no encontradas")
    public ResponseEntity<?> buscarPorNombre(@PathVariable String nombre) {
        try {
            List<EspecialidadDTO> resultados = especialidadService.buscarPorNombreParcial(nombre);
            return new ResponseEntity<>(resultados, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    //Buscar por Id 
    @GetMapping("/{id}")
    @Operation(summary = "Buscar especialidad por ID", description = "Obtiene una especialidad específica utilizando su ID único.")
    @ApiResponse(responseCode = "200", description = "Especialidad encontrada exitosamente")
    @ApiResponse(responseCode = "404", description = "Especialidad no encontrada")
    public ResponseEntity<?> buscarPorId(@PathVariable Integer id) {
        try {
            EspecialidadDTO dto = especialidadService.buscarPorId(id);
            return new ResponseEntity<>(dto, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    //Buscar todas las especialidades que tiene psicologo. 
    @GetMapping("/psicologo/{idPsicologo}")
    @Operation(summary = "Buscar especialidades por psicólogo", description = "Obtiene una lista de especialidades asociadas a un psicólogo específico.")
    @ApiResponse(responseCode = "200", description = "Especialidades encontradas exitosamente")
    @ApiResponse(responseCode = "404", description = "Psicólogo no encontrado o sin especialidades")
    public ResponseEntity<?> buscarPorPsicologo(@PathVariable Integer idPsicologo) {
        try {
            List<EspecialidadDTO> lista = especialidadService.buscarPorPsicologo(idPsicologo);
            return new ResponseEntity<>(lista, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    //Agregar nueva especialidad 
    @PostMapping
    @Operation(summary = "Registrar nueva especialidad", description = "Crea una nueva especialidad utilizando los datos proporcionados en el cuerpo de la solicitud.")
    @ApiResponse(responseCode = "201", description = "Especialidad registrada exitosamente")
    @ApiResponse(responseCode = "400", description = "Datos de la especialidad inválidos")
    public ResponseEntity<?> crearEspecialidad(@RequestBody EspecialidadDTO dto) {
        try {
            return new ResponseEntity<>(especialidadService.registrarEspecialidad(dto), HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}