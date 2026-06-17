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
import com.example.ms_profesionales.DTO.SucursalDTO;
import com.example.ms_profesionales.model.Sucursal;
import com.example.ms_profesionales.service.SucursalService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/sucursales")
@Tag(name = "Sucursal Controller", description = "Controlador para gestionar sucursales y sus psicólogos asociados")
public class SucursalController {

    private final SucursalService sucursalService;

    SucursalController(SucursalService sucursalService) {
        this.sucursalService = sucursalService;
    }

    @GetMapping
    @Operation(summary = "Obtener todas las sucursales", description = "Devuelve una lista de todas las sucursales registradas en el sistema")
    public ResponseEntity<List<SucursalDTO>> obtenerTodas() {
        List<SucursalDTO> sucursales = sucursalService.obtenerTodos();
        if(sucursales.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(sucursales, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar sucursal por ID", description = "Devuelve los detalles de una sucursal específica utilizando su ID único")
    public ResponseEntity<?> buscarPorId(@PathVariable Integer id) {
        try {
            SucursalDTO sucursal = sucursalService.buscarPorId(id);
            return new ResponseEntity<>(sucursal, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/buscarNombre/{nombre}")
    @Operation(summary = "Buscar sucursal por nombre", description = "Devuelve una lista de sucursales que coinciden con el nombre proporcionado")
    public ResponseEntity<List<SucursalDTO>> buscarPorNombre(@PathVariable String nombre) {
        List<SucursalDTO> sucursalesEncontradas = sucursalService.buscarPorNombre(nombre);
        
        if(sucursalesEncontradas.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(sucursalesEncontradas, HttpStatus.OK);
    }

    @PostMapping
    @Operation(summary = "Crear nueva sucursal", description = "Permite crear una nueva sucursal proporcionando los detalles necesarios en el cuerpo de la solicitud")
    public ResponseEntity<?> guardarSucursal(@RequestBody Sucursal sucursal) {
        try {
            SucursalDTO nuevaSucursal = sucursalService.guardarSucursal(sucursal);
            return new ResponseEntity<>(nuevaSucursal, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar sucursal existente", description = "Permite actualizar los detalles de una sucursal existente utilizando su ID y proporcionando los nuevos datos en el cuerpo de la solicitud")
    public ResponseEntity<?> actualizarSucursal(@PathVariable Integer id, @RequestBody Sucursal sucursal) {
        try {
            SucursalDTO actualizada = sucursalService.actualizarSucursal(id, sucursal);
            return new ResponseEntity<>(actualizada, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar sucursal", description = "Permite eliminar una sucursal utilizando su ID. Si la sucursal tiene psicólogos asociados, no se podrá eliminar y se devolverá un mensaje de error")
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
    @Operation(summary = "Obtener psicólogos por sucursal", description = "Devuelve una lista de psicólogos asociados a una sucursal específica utilizando su ID")
    public ResponseEntity<?> obtenerPsicologosPorSucursal(@PathVariable Integer id) {
        try {
            List<PsicologoDTO> psicologos = sucursalService.obtenerPsicologosPorSucursal(id);
            return new ResponseEntity<>(psicologos, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

}
