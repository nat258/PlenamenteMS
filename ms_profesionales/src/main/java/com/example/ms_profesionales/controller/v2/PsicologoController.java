package com.example.ms_profesionales.controller.v2;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.ms_profesionales.DTO.PsicologoDTO;
import com.example.ms_profesionales.assembler.PsicologoModelAssembler;
import com.example.ms_profesionales.model.Psicologo;
import com.example.ms_profesionales.service.PsicologoService;

import jakarta.validation.Valid;

@RestController("PsicologoControllerV2")
@RequestMapping("/api/v2/psicologos")
public class PsicologoController {

    private final PsicologoService psicologoService;
    private final PsicologoModelAssembler assembler;

    PsicologoController(PsicologoService psicologoService, PsicologoModelAssembler assembler) {
        this.psicologoService = psicologoService;
        this.assembler = assembler;
    }

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<CollectionModel<EntityModel<PsicologoDTO>>> obtenerTodos() {
        List<EntityModel<PsicologoDTO>> psicologos = psicologoService.obtenerTodosLosPsicologos().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        if (psicologos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(CollectionModel.of(psicologos,
                linkTo(methodOn(PsicologoController.class).obtenerTodos()).withSelfRel()));
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<PsicologoDTO>> porId(@PathVariable Integer id) {
        try {
            PsicologoDTO dto = psicologoService.buscarPsicologoPorId(id);
            return ResponseEntity.ok(assembler.toModel(dto));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(value = "/rut/{rut}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<PsicologoDTO>> porRut(@PathVariable String rut) {
        try {
            PsicologoDTO dto = psicologoService.buscarPsicologoPorRut(rut);
            return ResponseEntity.ok(assembler.toModel(dto));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<PsicologoDTO>> registrar(@Valid @RequestBody Psicologo psicologo) {
        try {
            PsicologoDTO guardado = psicologoService.guardarPsicologo(psicologo);
            return ResponseEntity
                    .created(linkTo(methodOn(PsicologoController.class).porId(guardado.getId())).toUri())
                    .body(assembler.toModel(guardado));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        try {
            psicologoService.eliminarPsicologo(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
