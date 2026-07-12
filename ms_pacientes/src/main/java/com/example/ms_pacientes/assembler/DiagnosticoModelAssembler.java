package com.example.ms_pacientes.assembler;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.example.ms_pacientes.DTO.DiagnosticoDTO;
import com.example.ms_pacientes.controller.v1.DiagnosticoController;

@Component
public class DiagnosticoModelAssembler implements RepresentationModelAssembler<DiagnosticoDTO, EntityModel<DiagnosticoDTO>> {

    @Override
    public EntityModel<DiagnosticoDTO> toModel(DiagnosticoDTO diagnostico) {
        return EntityModel.of(diagnostico,
                linkTo(methodOn(DiagnosticoController.class).buscarPorId(diagnostico.getId())).withSelfRel(),
                linkTo(methodOn(DiagnosticoController.class).obtenerTodos()).withRel("diagnosticos")
        );
    }

}
