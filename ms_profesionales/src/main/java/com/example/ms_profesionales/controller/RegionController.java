package com.example.ms_profesionales.controller;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.ms_profesionales.DTO.RegionDTO;
import com.example.ms_profesionales.model.Region;
import com.example.ms_profesionales.service.RegionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;


@RestController
@RequestMapping("/api/v1/regiones")
@Tag(name = "Region Controller", description = "Endpoints para gestionar regiones")

public class RegionController {

    private final RegionService regionService;

    RegionController(RegionService regionService) {
        this.regionService = regionService;
    }

    @GetMapping
    @Operation(summary = "Obtener todas las regiones", description = "Obtiene una lista de todas las regiones registradas.")
    public ResponseEntity<List<RegionDTO>> obtenerTodas() {
        List<RegionDTO> regiones = regionService.obtenerTodos();
        if (regiones.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(regiones, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener región por ID", description = "Obtiene una región específica utilizando su ID único.")
    public ResponseEntity<?> buscarPorId(@PathVariable Integer id) {
        try {
            RegionDTO region = regionService.buscarPorId(id);
            return new ResponseEntity<>(region, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    @Operation(summary = "Registrar nueva región", description = "Crea una nueva región utilizando los datos proporcionados en el cuerpo de la solicitud.")
    public ResponseEntity<?> guardarRegion(@RequestBody Region region) {
        try {
            RegionDTO nueva = regionService.guardarRegion(region);
            return new ResponseEntity<>(nueva, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar región", description = "Actualiza los datos de una región específica utilizando su ID único.")
    public ResponseEntity<?> actualizarRegion(@PathVariable Integer id, @RequestBody Region region) {
        try {
            RegionDTO actualizada = regionService.actualizarRegion(id, region);
            return new ResponseEntity<>(actualizada, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar región", description = "Elimina una región específica utilizando su ID único.")
    public ResponseEntity<String> eliminarRegion(@PathVariable Integer id) {
        String mensaje = regionService.eliminarRegion(id);
        if(mensaje.contains("No se puede eliminar")) {
            return new ResponseEntity<>(mensaje, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(mensaje, HttpStatus.OK);
    }

    @GetMapping("/buscar/{nombre}")
    @Operation(summary = "Buscar región por nombre", description = "Obtiene una lista de regiones que coinciden con el nombre proporcionado.")
    public ResponseEntity<List<RegionDTO>> encontrarPorNombre(@PathVariable String nombre) {
        List<RegionDTO> regionesEncontradas = regionService.encontrarPorNombre(nombre);
        if (regionesEncontradas.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(regionesEncontradas, HttpStatus.OK);
    }
}