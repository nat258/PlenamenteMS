package com.example.ms_pacientes.assembler;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.example.ms_pacientes.DTO.PacienteDTO;
import com.example.ms_pacientes.controller.v1.PacienteController;

@Component
public class PacienteModelAssembler implements RepresentationModelAssembler<PacienteDTO, EntityModel<PacienteDTO>> {

    @Override
    public EntityModel<PacienteDTO> toModel(PacienteDTO paciente) {
        return EntityModel.of(paciente,
                linkTo(methodOn(PacienteController.class).obtenerPorId(paciente.getId())).withSelfRel(),
                linkTo(methodOn(PacienteController.class).obtenerTodos()).withRel("pacientes")
        );
    }
}
