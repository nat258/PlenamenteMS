package com.example.ms_profesionales.assembler;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.example.ms_profesionales.DTO.PsicologoDTO;
import com.example.ms_profesionales.controller.v1.PsicologoController;

@Component
public class PsicologoModelAssembler implements RepresentationModelAssembler<PsicologoDTO, EntityModel<PsicologoDTO>> {

    @Override
    public EntityModel<PsicologoDTO> toModel(PsicologoDTO psicologo) {
        return EntityModel.of(psicologo,
                linkTo(methodOn(PsicologoController.class).obtenerPorId(psicologo.getId())).withSelfRel(),
                linkTo(methodOn(PsicologoController.class).obtenerTodos()).withRel("psicologos")
        );
    }
}
