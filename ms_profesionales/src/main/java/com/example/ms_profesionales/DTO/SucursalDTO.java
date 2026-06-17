package com.example.ms_profesionales.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SucursalDTO {

    private Integer id;
    private String nombre;
    private String direccion;
    private Integer comunaId;
    private String comunaNombre;

}