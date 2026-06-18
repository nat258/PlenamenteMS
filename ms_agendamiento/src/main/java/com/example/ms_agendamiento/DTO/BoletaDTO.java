package com.example.ms_agendamiento.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoletaDTO {

    private Integer id;
    private String TipoPago;
    private Integer monto;
    private Integer reservaHoraId;

}