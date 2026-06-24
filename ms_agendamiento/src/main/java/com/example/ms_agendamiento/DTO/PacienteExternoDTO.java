package com.example.ms_agendamiento.DTO;

import lombok.Data;

@Data
public class PacienteExternoDTO {

    private Integer id;
    private String rut;
    private String p_nombre;
    private String s_nombre;
    private String p_apellido;
    private String s_apellido;
    private String correo;
    private Integer telefono;
    private String direccion;
    private Integer previsionId;
    private String previsionNombre;
    private Integer ReservaHoraId;
}
