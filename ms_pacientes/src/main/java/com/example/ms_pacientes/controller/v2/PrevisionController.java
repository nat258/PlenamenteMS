package com.example.ms_pacientes.controller.v2;

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

import com.example.ms_pacientes.DTO.PrevisionDTO;
import com.example.ms_pacientes.assembler.PrevisionModelAssambler;
import com.example.ms_pacientes.model.Prevision;
import com.example.ms_pacientes.service.PrevisionService;

import jakarta.validation.Valid;



@RestController("PrevisionControllerV2")
@RequestMapping("/api/v2/prevision")
public class PrevisionController {

    private final PrevisionService previsionService;

    private final PrevisionModelAssambler assembler;

    PrevisionController(PrevisionService previsionService, PrevisionModelAssambler assembler) {
        this.previsionService = previsionService;
        this.assembler = assembler;
    }

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<CollectionModel<EntityModel<PrevisionDTO>>> todas() {
        List<EntityModel<PrevisionDTO>> previsiones = previsionService.obtenerTodosLasPrevisiones().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        if (previsiones.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(CollectionModel.of(
                previsiones,
                linkTo(methodOn(PrevisionController.class).todas()).withSelfRel()
        ));
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<PrevisionDTO>> porId(@PathVariable Integer id) {
        try {
            PrevisionDTO dto = previsionService.buscarPrevisionPorId(id);
            if (dto == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(assembler.toModel(dto));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
public ResponseEntity<EntityModel<PrevisionDTO>> crear(@Valid @RequestBody Prevision prevision) { // <-- Aquí cambiamos a Prevision
    try {
        Prevision guardada = previsionService.guardarPrevision(prevision);
        
        PrevisionDTO dtoSalida = new PrevisionDTO();
        dtoSalida.setId(guardada.getId());
        dtoSalida.setTipo(guardada.getTipo());

        java.net.URI location = org.springframework.web.servlet.support.ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(dtoSalida.getId())
                .toUri();

        return ResponseEntity
                .created(location)
                .body(assembler.toModel(dtoSalida));

    } catch (Exception e) {
        return ResponseEntity.badRequest().build();
    }
}

    @DeleteMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        try {
            previsionService.eliminarPrevision(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
