package com.example.ms_pacientes.controller.v2;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.ms_pacientes.DTO.HistorialDiagnosticoDTO;
import com.example.ms_pacientes.assembler.HistorialDiagnosticoModelAssembler;
import com.example.ms_pacientes.model.HistorialDiagnostico;
import com.example.ms_pacientes.service.HistorialDiagnosticoService;

import jakarta.validation.Valid;

@RestController("HistorialDiagnosticoControllerV2")
@RequestMapping("/api/v2/historiales")
public class HistorialDiagnosticoController {

    private final HistorialDiagnosticoService historialService;

    private final HistorialDiagnosticoModelAssembler assembler;

    HistorialDiagnosticoController(HistorialDiagnosticoService historialService, HistorialDiagnosticoModelAssembler assembler) {
        this.historialService = historialService;
        this.assembler = assembler;
    }

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<CollectionModel<EntityModel<HistorialDiagnosticoDTO>>> todas() {
        List<EntityModel<HistorialDiagnosticoDTO>> historiales = historialService.obtenerTodos().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        if (historiales.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(CollectionModel.of(
                historiales,
                linkTo(methodOn(HistorialDiagnosticoController.class).todas()).withSelfRel()
        ));
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<HistorialDiagnosticoDTO>> porId(@PathVariable Integer id) {
        try {
            HistorialDiagnosticoDTO dto = historialService.obtenerPorId(id);
            if (dto == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(assembler.toModel(dto));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<HistorialDiagnosticoDTO>> registrar(@Valid @RequestBody HistorialDiagnostico historial) {
        try {
            HistorialDiagnosticoDTO newHistorial = historialService.guardarHistorial(historial);
            return ResponseEntity
                    .created(linkTo(methodOn(HistorialDiagnosticoController.class).porId(newHistorial.getId())).toUri())
                    .body(assembler.toModel(newHistorial));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<HistorialDiagnosticoDTO>> actualizar(@PathVariable Integer id, @Valid @RequestBody HistorialDiagnostico historial) {
        try {
            HistorialDiagnosticoDTO updatedHistorial = historialService.actualizarHistorial(id, historial);
            if (updatedHistorial == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(assembler.toModel(updatedHistorial));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

@GetMapping(value = "/paciente/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<CollectionModel<EntityModel<HistorialDiagnosticoDTO>>> porPacienteId(@PathVariable Integer id) {
        List<EntityModel<HistorialDiagnosticoDTO>> historiales = historialService.diagnosticoPorIdPaciente(id).stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        if (historiales.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(CollectionModel.of(
                historiales,
                linkTo(methodOn(HistorialDiagnosticoController.class).porPacienteId(id)).withSelfRel()
        ));
    }

    @GetMapping(value = "/fecha/{fecha}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<CollectionModel<EntityModel<HistorialDiagnosticoDTO>>> porFecha(@PathVariable LocalDate fecha) {
        List<EntityModel<HistorialDiagnosticoDTO>> historiales = historialService.buscarPorFecha(fecha).stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        if (historiales.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(CollectionModel.of(
                historiales,
                linkTo(methodOn(HistorialDiagnosticoController.class).porFecha(fecha)).withSelfRel()
        ));
    }
}
