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

import com.example.ms_pacientes.DTO.PacienteDTO;
import com.example.ms_pacientes.assembler.PacienteModelAssembler;
import com.example.ms_pacientes.model.Paciente;
import com.example.ms_pacientes.service.PacienteService;

import jakarta.validation.Valid;


@RestController("PacienteControllerV2")
@RequestMapping("/api/v2/pacientes")
public class PacienteController {

    private final PacienteService pacienteService;

    private final PacienteModelAssembler assembler;

    PacienteController(PacienteService pacienteService, PacienteModelAssembler assembler) {
        this.pacienteService = pacienteService;
        this.assembler = assembler;
    }

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<CollectionModel<EntityModel<PacienteDTO>>> todas() {
        List<EntityModel<PacienteDTO>> pacientes = pacienteService.obtenerTodosLosPacientes().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        if (pacientes.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(CollectionModel.of(
                pacientes,
                linkTo(methodOn(PacienteController.class).todas()).withSelfRel()
        ));
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<PacienteDTO>> porId(@PathVariable Integer id) {
        try {
            PacienteDTO dto = pacienteService.buscarPorID(id);
            if (dto == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(assembler.toModel(dto));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(value = "/rut/{rut}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<PacienteDTO>> porRut(@PathVariable String rut) {
        try {
            PacienteDTO dto = pacienteService.buscarPorRut(rut);
            if (dto == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(assembler.toModel(dto));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<PacienteDTO>> registrar(@Valid @RequestBody Paciente paciente) {
        try {
            PacienteDTO newPaciente = pacienteService.guardarPaciente(paciente);
            return ResponseEntity
                    .created(linkTo(methodOn(PacienteController.class).porId(newPaciente.getId())).toUri())
                    .body(assembler.toModel(newPaciente));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping(value = "/rut/{rut}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<Void> eliminarPorRut(@PathVariable String rut) {
        try {
            pacienteService.eliminarPacientePorRut(rut);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}