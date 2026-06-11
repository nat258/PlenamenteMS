package com.example.ms_profesionales.controller;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
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


@RestController
@RequestMapping("/api/v1/especialidades")
public class EspecialidadController {

    @Autowired 
    private EspecialidadService especialidadService;

    //Busqueda por coincidencia parcial de nombre 
    @GetMapping("/nombre/{nombre}")
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
    public ResponseEntity<?> crearEspecialidad(@RequestBody EspecialidadDTO dto) {
        try {
            return new ResponseEntity<>(especialidadService.registrarEspecialidad(dto), HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}