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

import com.example.ms_profesionales.DTO.ComunaDTO;
import com.example.ms_profesionales.service.ComunaService;

@RestController
@RequestMapping("/api/v1/comunas")
public class ComunaController {

    @Autowired
    private ComunaService comunaService;

    //Busqueda por Id 
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Integer id) {
        try {
            ComunaDTO comunaDTO = comunaService.buscarPorId(id);
            return new ResponseEntity<>(comunaDTO, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    //Busqueda por Nombre.
    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<?> buscarPorNombre(@PathVariable String nombre) {
        try {
            // Como tu service devuelve una List<ComunaDTO>, aquí enviamos la lista completa
            List<ComunaDTO> listaComunas = comunaService.buscarPorNombre(nombre);
            return new ResponseEntity<>(listaComunas, HttpStatus.OK);
        } catch (RuntimeException e) {
            // Si el service lanza la excepción cuando la lista está vacía
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    //Busqueda por palabras que contenga el nombre 
    @GetMapping("/nombrePar/{nombre}")
    public ResponseEntity<?> buscarPorNombreParcial(@PathVariable String nombre) {
        try {
            List<ComunaDTO> listaResultados = comunaService.buscarPorNombreParcial(nombre);
            return new ResponseEntity<>(listaResultados, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    //Registrar nueva comuna 
    @PostMapping
    public ResponseEntity<?> registrarComuna(@RequestBody ComunaDTO comunaDTO) {
        try {
            ComunaDTO nuevaComuna = comunaService.registrarComuna(comunaDTO);
            return new ResponseEntity<>(nuevaComuna, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    //Eliminar comuna 
    public ResponseEntity<String> eliminarComuna(@PathVariable Integer id) {
        String mensaje = comunaService.eliminarComuna(id);
        if (mensaje.contains("éxito")) {
            return ResponseEntity.ok(mensaje);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(mensaje);
        }
    }




}
