package com.example.ms_pacientes.controller.v2;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.ms_pacientes.DTO.DiagnosticoDTO;
import com.example.ms_pacientes.assembler.DiagnosticoModelAssembler;
import com.example.ms_pacientes.model.Diagnostico;
import com.example.ms_pacientes.service.DiagnosticoService;

import jakarta.validation.Valid;

@RestController("diagnosticoControllerV2")
@RequestMapping("/api/v2/diagnosticos")
public class DiagnosticoController {

    private final DiagnosticoService diagnosticoService;

    private final DiagnosticoModelAssembler assembler;

    DiagnosticoController(DiagnosticoService diagnosticoService, DiagnosticoModelAssembler assembler) {
        this.diagnosticoService = diagnosticoService;
        this.assembler = assembler;
    }

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<CollectionModel<EntityModel<DiagnosticoDTO>>> todas() {
        List<EntityModel<DiagnosticoDTO>> diagnosticos = diagnosticoService.obtenerTodos().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        if (diagnosticos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(CollectionModel.of(
                diagnosticos,
                linkTo(methodOn(DiagnosticoController.class).todas()).withSelfRel()
        ));
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<DiagnosticoDTO>> porId(@PathVariable Integer id) {
        try {
            DiagnosticoDTO dto = diagnosticoService.buscarPorId(id);
            if (dto == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(assembler.toModel(dto));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(value = "/rut/{rut}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<DiagnosticoDTO>> porRut(@PathVariable String rut) {
        try {
            DiagnosticoDTO dto = diagnosticoService.buscarPorRut(rut);
            if (dto == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(assembler.toModel(dto));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<DiagnosticoDTO>> registrar(@Valid @RequestBody Diagnostico diagnostico) {
        try {
            DiagnosticoDTO newDiagnostico = diagnosticoService.guardarDiagnostico(diagnostico);
            return ResponseEntity
                    .created(linkTo(methodOn(DiagnosticoController.class).porId(newDiagnostico.getId())).toUri())
                    .body(assembler.toModel(newDiagnostico));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<DiagnosticoDTO>> actualizar(@PathVariable Integer id, @Valid @RequestBody Diagnostico diagnostico) {
        try {
            DiagnosticoDTO updatedDiagnostico = diagnosticoService.actualizarDiagnostico(id, diagnostico);
            if (updatedDiagnostico == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(assembler.toModel(updatedDiagnostico));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        try {
            diagnosticoService.eliminarDiagnostico(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    
}