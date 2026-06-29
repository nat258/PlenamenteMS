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

import com.example.ms_profesionales.DTO.ComunaDTO;
import com.example.ms_profesionales.assembler.ComunaModelAssembler;
import com.example.ms_profesionales.service.ComunaService;

import jakarta.validation.Valid;

@RestController("ComunaControllerV2")
@RequestMapping("/api/v2/comunas")
public class ComunaController {

    private final ComunaService comunaService;
    private final ComunaModelAssembler assembler;

    ComunaController(ComunaService comunaService, ComunaModelAssembler assembler) {
        this.comunaService = comunaService;
        this.assembler = assembler;
    }

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<CollectionModel<EntityModel<ComunaDTO>>> todas() {
        List<EntityModel<ComunaDTO>> comunas = comunaService.obtenerTodas().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        if (comunas.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(CollectionModel.of(comunas,
                linkTo(methodOn(ComunaController.class).todas()).withSelfRel()));
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<ComunaDTO>> porId(@PathVariable Integer id) {
        try {
            ComunaDTO dto = comunaService.buscarPorId(id);
            return ResponseEntity.ok(assembler.toModel(dto));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<ComunaDTO>> crear(@Valid @RequestBody ComunaDTO comunaDTO) {
        try {
            ComunaDTO guardada = comunaService.registrarComuna(comunaDTO);
            return ResponseEntity
                    .created(linkTo(methodOn(ComunaController.class).porId(guardada.getId())).toUri())
                    .body(assembler.toModel(guardada));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
