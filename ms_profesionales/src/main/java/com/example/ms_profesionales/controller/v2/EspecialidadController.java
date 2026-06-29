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

import com.example.ms_profesionales.DTO.EspecialidadDTO;
import com.example.ms_profesionales.assembler.EspecialidadModelAssembler;
import com.example.ms_profesionales.service.EspecialidadService;

import jakarta.validation.Valid;

@RestController("EspecialidadControllerV2")
@RequestMapping("/api/v2/especialidades")
public class EspecialidadController {

    private final EspecialidadService especialidadService;
    private final EspecialidadModelAssembler assembler;

    EspecialidadController(EspecialidadService especialidadService, EspecialidadModelAssembler assembler) {
        this.especialidadService = especialidadService;
        this.assembler = assembler;
    }

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<CollectionModel<EntityModel<EspecialidadDTO>>> todas() {
        List<EntityModel<EspecialidadDTO>> especialidades = especialidadService.obtenerTodas().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        if (especialidades.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(CollectionModel.of(especialidades,
                linkTo(methodOn(EspecialidadController.class).todas()).withSelfRel()));
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<EspecialidadDTO>> porId(@PathVariable Integer id) {
        try {
            EspecialidadDTO dto = especialidadService.buscarPorId(id);
            return ResponseEntity.ok(assembler.toModel(dto));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<EspecialidadDTO>> crear(@Valid @RequestBody EspecialidadDTO especialidadDTO) {
        try {
            EspecialidadDTO guardada = especialidadService.registrarEspecialidad(especialidadDTO);
            return ResponseEntity
                    .created(linkTo(methodOn(EspecialidadController.class).porId(guardada.getId())).toUri())
                    .body(assembler.toModel(guardada));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
