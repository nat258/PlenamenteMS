package com.example.ms_profesionales.controller;
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

import com.example.ms_profesionales.DTO.PsicologoDTO;
import com.example.ms_profesionales.model.Psicologo;
import com.example.ms_profesionales.service.PsicologoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/psicologos")
@Tag(name = "Psicologo Controller", description = "Endpoints para gestionar psicólogos")
public class PsicologoController {

    private final PsicologoService psicologoService;

    PsicologoController(PsicologoService psicologoService) {
        this.psicologoService = psicologoService;
    }

    //Obtener todos los psicologos
    @GetMapping
    @Operation(summary = "Obtener todos los psicólogos", description = "Obtiene una lista de todos los psicólogos registrados.")
    public ResponseEntity<List<PsicologoDTO>> obtenerTodos() {
        return ResponseEntity.ok(psicologoService.obtenerTodosLosPsicologos());
    }

    //Obtener por id
    @GetMapping("/{id}")
    @Operation(summary = "Obtener psicólogo por ID", description = "Obtiene un psicólogo específico utilizando su ID único.")
    public ResponseEntity<Object> obtenerPorId(@PathVariable Integer id){
        try{
            PsicologoDTO psicologo = psicologoService.buscarPsicologoPorId(id);
            return new ResponseEntity<>(psicologo, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        
        }
    }

    //Obtener por rut 
    @GetMapping("/rut/{rut}")
    public ResponseEntity<Object> obtenerPorRut(@PathVariable String rut) {
        try {
            PsicologoDTO psicologo = psicologoService.buscarPsicologoPorRut(rut);
            return new ResponseEntity<>(psicologo, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    //Guardar nuevo psicologo
    @PostMapping
    @Operation(summary = "Registrar nuevo psicólogo", description = "Crea un nuevo psicólogo utilizando los datos proporcionados en el cuerpo de la solicitud.")
    public ResponseEntity<Object> agregarPsicologo(@RequestBody Psicologo psicologo) {
        try {
            Psicologo guardado = psicologoService.guardarPsicologo(psicologo);
            return new ResponseEntity<>(guardado, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    //Actualizar por id
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar psicólogo", description = "Actualiza los datos de un psicólogo específico utilizando su ID único.")
    public ResponseEntity<Object> actualizarPsicologo(@PathVariable Integer id, @RequestBody Psicologo psicologo) {
        try {
            Psicologo editado = psicologoService.actualizarPsicologo(id, psicologo);
            return new ResponseEntity<>(editado, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
    //Eliminar por id
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarPsicologo(@PathVariable Integer id) {
        String resultado = psicologoService.eliminarPsicologo(id);
        if (resultado.contains("Se ha eliminado con éxito el psicólogo")) {
            return new ResponseEntity<>(resultado, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(resultado, HttpStatus.NOT_FOUND);
        }
    }
}
