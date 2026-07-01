package com.example.ms_agendamiento.DTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PacienteResponseDTO {
    
    private Integer id;
    
    private String rut; // Aquí el rut viene listo con guion y todo
    private String p_nombre;
    private String s_nombre;
    private String p_apellido;
    private String s_apellido;
    private String correo;
    private String telefono;
    
}