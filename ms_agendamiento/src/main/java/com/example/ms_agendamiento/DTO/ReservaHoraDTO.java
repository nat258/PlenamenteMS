package com.example.ms_agendamiento.DTO;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservaHoraDTO {

    private Integer id;
    private LocalDateTime fechaHora;
    private String estado;

    private Integer pacienteId;
    private Integer psicologoId;

    private Integer boletaId;

}