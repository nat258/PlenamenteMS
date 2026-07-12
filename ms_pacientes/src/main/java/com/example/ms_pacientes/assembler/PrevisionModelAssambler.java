package com.example.ms_pacientes.assembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.example.ms_pacientes.DTO.PrevisionDTO;
import com.example.ms_pacientes.controller.v1.PrevisionController;

@Component
public class PrevisionModelAssambler implements RepresentationModelAssembler<PrevisionDTO, EntityModel<PrevisionDTO>> {

    @Override
    public EntityModel<PrevisionDTO> toModel(PrevisionDTO prevision) {
        return EntityModel.of(prevision,
                linkTo(methodOn(PrevisionController.class).buscarPorId(prevision.getId())).withSelfRel(),
                linkTo(methodOn(PrevisionController.class).obtenerTodas()).withRel("previsiones")
        );
    }

}
