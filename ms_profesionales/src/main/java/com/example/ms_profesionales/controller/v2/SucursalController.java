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
import com.example.ms_profesionales.DTO.SucursalDTO;
import com.example.ms_profesionales.assembler.SucursalModelAssembler;
import com.example.ms_profesionales.service.SucursalService;

import jakarta.validation.Valid;

@RestController("SucursalControllerV2")
@RequestMapping("/api/v2/sucursales")
public class SucursalController {

    private final SucursalService sucursalService;
    private final SucursalModelAssembler assembler;

    SucursalController(SucursalService sucursalService, SucursalModelAssembler assembler) {
        this.sucursalService = sucursalService;
        this.assembler = assembler;
    }

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<CollectionModel<EntityModel<SucursalDTO>>> todas() {
        List<EntityModel<SucursalDTO>> sucursales = sucursalService.obtenerTodos().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        if (sucursales.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(CollectionModel.of(sucursales,
                linkTo(methodOn(SucursalController.class).todas()).withSelfRel()));
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<SucursalDTO>> porId(@PathVariable Integer id) {
        try {
            SucursalDTO dto = sucursalService.buscarPorId(id);
            return ResponseEntity.ok(assembler.toModel(dto));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
public ResponseEntity<EntityModel<RegionDTO>> crear(@Valid @RequestBody RegionDTO regionDTO) {
    try {
        // 1. Creamos la entidad Region que el servicio "guardarRegion" espera recibir
        com.example.ms_profesionales.model.Region regionEntidad = new com.example.ms_profesionales.model.Region();
        regionEntidad.setId(regionDTO.getId());
        regionEntidad.setNombre(regionDTO.getNombre());

        // 2. Ahora sí, le pasamos la entidad al método (que devuelve un RegionDTO)
        RegionDTO guardada = regionService.guardarRegion(regionEntidad);

        // 3. Construimos la URI de forma segura y dinámica para evitar problemas con methodOn
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
