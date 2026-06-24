package com.example.ms_agendamiento.DTO;

import lombok.Data;

@Data
public class PsicologoExternoDTO {

    private Integer id;
    private Long rut;
    private String dv_rut;
    private String p_nombre;
    private String s_nombre;
    private String p_apellido;
    private String s_apellido;

    private Integer reservaHoraIdP;


}
