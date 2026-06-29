package com.example.ms_profesionales.assembler;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.example.ms_profesionales.DTO.ComunaDTO;
import com.example.ms_profesionales.controller.v1.ComunaController;

@Component
public class ComunaModelAssembler implements RepresentationModelAssembler<ComunaDTO, EntityModel<ComunaDTO>> {

    @Override
    public EntityModel<ComunaDTO> toModel(ComunaDTO comuna) {
        return EntityModel.of(comuna,
                linkTo(methodOn(ComunaController.class).buscarPorId(comuna.getId())).withSelfRel(),
                linkTo(methodOn(ComunaController.class).obtenerTodas()).withRel("comunas")
        );
    }
}
