package com.example.ms_agendamiento.DTO;
import lombok.Data;

@Data
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