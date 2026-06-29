package com.example.ms_profesionales.assembler;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.example.ms_profesionales.DTO.RegionDTO;
import com.example.ms_profesionales.controller.v1.RegionController;

@Component
public class RegionModelAssembler implements RepresentationModelAssembler<RegionDTO, EntityModel<RegionDTO>> {

    @Override
    public EntityModel<RegionDTO> toModel(RegionDTO region) {
        return EntityModel.of(region,
                linkTo(methodOn(RegionController.class).buscarPorId(region.getId())).withSelfRel(),
                linkTo(methodOn(RegionController.class).obtenerTodas()).withRel("regiones")
        );
    }
}
