package com.example.ms_pacientes.DTO;

import lombok.Data;

@Data
public class DiagnosticoDTO {

    private Integer id;
    private String nombre;
    private String descripcion;
    private Integer pacienteId;

}
