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
import com.example.ms_profesionales.DTO.SucursalDTO;
import com.example.ms_profesionales.model.Sucursal;
import com.example.ms_profesionales.service.SucursalService;

@RestController
@RequestMapping("/api/v1/sucursales")
public class SucursalController {

    @Autowired
    private SucursalService sucursalService;

    @GetMapping
    public ResponseEntity<List<SucursalDTO>> obtenerTodas() {
        List<SucursalDTO> sucursales = sucursalService.obtenerTodos();
        if(sucursales.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(sucursales, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Integer id) {
        try {
            SucursalDTO sucursal = sucursalService.buscarPorId(id);
            return new ResponseEntity<>(sucursal, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/buscarNombre/{nombre}")
    public ResponseEntity<List<SucursalDTO>> buscarPorNombre(@PathVariable String nombre) {
        List<SucursalDTO> sucursalesEncontradas = sucursalService.buscarPorNombre(nombre);
        
        if(sucursalesEncontradas.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(sucursalesEncontradas, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> guardarSucursal(@RequestBody Sucursal sucursal) {
        try {
            SucursalDTO nuevaSucursal = sucursalService.guardarSucursal(sucursal);
            return new ResponseEntity<>(nuevaSucursal, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarSucursal(@PathVariable Integer id, @RequestBody Sucursal sucursal) {
        try {
            SucursalDTO actualizada = sucursalService.actualizarSucursal(id, sucursal);
            return new ResponseEntity<>(actualizada, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarSucursal(@PathVariable Integer id) {
        String mensaje = sucursalService.eliminarSucursal(id);
        if (mensaje.contains("No se puede eliminar")) {
            return new ResponseEntity<>(mensaje, HttpStatus.BAD_REQUEST);
        }
        
        if(mensaje.contains("no se encuentra registrado")) {
            return new ResponseEntity<>(mensaje, HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(mensaje, HttpStatus.OK);
    }

    @GetMapping("/{id}/psicologos")
    public ResponseEntity<?> obtenerPsicologosPorSucursal(@PathVariable Integer id) {
        try {
            List<PsicologoDTO> psicologos = sucursalService.obtenerPsicologosPorSucursal(id);
            return new ResponseEntity<>(psicologos, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

}
