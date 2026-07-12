package com.example.ms_pacientes.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DiagnosticoDTO {

    private Integer id;
    private String nombre;
    private String descripcion;
    private Integer pacienteId;

}
