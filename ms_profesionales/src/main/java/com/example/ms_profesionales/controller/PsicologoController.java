package com.example.ms_profesionales.controller;
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

import com.example.ms_profesionales.DTO.PsicologoDTO;
import com.example.ms_profesionales.model.Psicologo;
import com.example.ms_profesionales.service.PsicologoService;

@RestController
@RequestMapping("/api/v1/psicologos")
public class PsicologoController {

    @Autowired
    private PsicologoService psicologoService;

    //Obtener todos los psicologos
    @GetMapping
    public ResponseEntity<List<PsicologoDTO>> obtenerTodos() {
        return ResponseEntity.ok(psicologoService.obtenerTodosLosPsicologos());
    }

    //Obtener por id
    @GetMapping("/{id}")
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
