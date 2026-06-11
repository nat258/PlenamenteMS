package com.example.ms_profesionales.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ComunaDTO {
    private Integer id;
    private String nombre;
    private String nombreRegion;

}