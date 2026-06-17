package com.example.ms_pacientes.DTO;

import java.time.LocalDate;

import lombok.Data;

@Data
public class HistorialDiagnosticoDTO {

    private Integer id;
    private LocalDate fecha;
    private String observacion;
    private Integer diagnosticoId;
    private Integer pacienteId;

}
