package com.example.ms_profesionales.assembler;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.example.ms_profesionales.DTO.SucursalDTO;
import com.example.ms_profesionales.controller.v1.SucursalController;

@Component
public class SucursalModelAssembler implements RepresentationModelAssembler<SucursalDTO, EntityModel<SucursalDTO>> {

    @Override
    public EntityModel<SucursalDTO> toModel(SucursalDTO sucursal) {
        return EntityModel.of(sucursal,
                linkTo(methodOn(SucursalController.class).buscarPorId(sucursal.getId())).withSelfRel(),
                linkTo(methodOn(SucursalController.class).obtenerTodas()).withRel("sucursales")
        );
    }
}
