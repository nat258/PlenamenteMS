package com.example.ms_agendamiento.DTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PsicologoResponseDTO {

    private Integer id;

    private Long rut;
    private String dv_rut;
    private String pNombre;
    private String sNombre;
    private String pApellido;
    private String sApellido;

    public String getRutCompleto() {
        if (rut != null && dv_rut != null) {
            return rut + "-" + dv_rut;
        }
        return null;
    }
}