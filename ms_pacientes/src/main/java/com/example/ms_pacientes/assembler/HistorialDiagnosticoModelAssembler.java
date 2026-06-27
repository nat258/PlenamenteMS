package com.example.ms_pacientes.assembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.example.ms_pacientes.DTO.HistorialDiagnosticoDTO;
import com.example.ms_pacientes.controller.v1.HistorialDiagnosticoController;

@Component
public class HistorialDiagnosticoModelAssembler implements RepresentationModelAssembler<HistorialDiagnosticoDTO, EntityModel<HistorialDiagnosticoDTO>> {

    @Override
    public EntityModel<HistorialDiagnosticoDTO> toModel(HistorialDiagnosticoDTO historialDiagnostico) {
        return EntityModel.of(historialDiagnostico,
                linkTo(methodOn(HistorialDiagnosticoController.class).buscarPorDiagnostico(historialDiagnostico.getId())).withSelfRel(),
                linkTo(methodOn(HistorialDiagnosticoController.class).obtenerTodos()).withRel("historiales-diagnosticos")
        );
    }




}
