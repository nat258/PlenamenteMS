package com.example.ms_profesionales.DTO;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PsicologoDTO {

    private Integer id;
    private Long rut;
    private String dv_rut;
    private String p_nombre;
    private String s_nombre;
    private String p_apellido;
    private String s_apellido;

    private List<Integer> especialidadesId;
    private List<String> nombresEspecialidades;
    private List<Integer> sucursalesId;

}