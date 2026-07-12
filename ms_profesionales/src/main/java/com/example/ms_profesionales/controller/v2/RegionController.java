package com.example.ms_profesionales.controller.v2;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.ms_profesionales.DTO.RegionDTO;
import com.example.ms_profesionales.assembler.RegionModelAssembler;
import com.example.ms_profesionales.service.RegionService;

import jakarta.validation.Valid;

@RestController("RegionControllerV2")
@RequestMapping("/api/v2/regiones")
public class RegionController {

    private final RegionService regionService;
    private final RegionModelAssembler assembler;

    RegionController(RegionService regionService, RegionModelAssembler assembler) {
        this.regionService = regionService;
        this.assembler = assembler;
    }

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<CollectionModel<EntityModel<RegionDTO>>> todas() {
        List<EntityModel<RegionDTO>> regiones = regionService.obtenerTodos().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        if (regiones.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(CollectionModel.of(regiones,
                linkTo(methodOn(RegionController.class).todas()).withSelfRel()));
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<RegionDTO>> porId(@PathVariable Integer id) {
        try {
            RegionDTO dto = regionService.buscarPorId(id);
            return ResponseEntity.ok(assembler.toModel(dto));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<RegionDTO>> crear(@Valid @RequestBody RegionDTO regionDTO) {
    try {
        // 1. Creamos el objeto Region (Entidad) que el servicio necesita recibir
        com.example.ms_profesionales.model.Region regionEntidad = new com.example.ms_profesionales.model.Region();
        regionEntidad.setId(regionDTO.getId());
        regionEntidad.setNombre(regionDTO.getNombre()); // Asegúrate de que se llame setNombre en tu entidad

        // 2. Ahora sí, le pasamos la entidad al método guardarRegion
        RegionDTO guardada = regionService.guardarRegion(regionEntidad);

        // 3. Construimos la URI de forma dinámica para blindarla contra errores de compilación
        java.net.URI location = org.springframework.web.servlet.support.ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(guardada.getId())
                .toUri();

        return ResponseEntity
                .created(location)
                .body(assembler.toModel(guardada));

    } catch (RuntimeException e) {
        return ResponseEntity.badRequest().build();
    }
    }
}