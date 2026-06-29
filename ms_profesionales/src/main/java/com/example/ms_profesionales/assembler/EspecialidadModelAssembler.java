package com.example.ms_profesionales.assembler;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.example.ms_profesionales.DTO.EspecialidadDTO;
import com.example.ms_profesionales.controller.v1.EspecialidadController;

@Component
public class EspecialidadModelAssembler implements RepresentationModelAssembler<EspecialidadDTO, EntityModel<EspecialidadDTO>> {

    @Override
    public EntityModel<EspecialidadDTO> toModel(EspecialidadDTO especialidad) {
        return EntityModel.of(especialidad,
                linkTo(methodOn(EspecialidadController.class).buscarPorId(especialidad.getId())).withSelfRel(),
                linkTo(methodOn(EspecialidadController.class).obtenerTodas()).withRel("especialidades")
        );
    }
}
