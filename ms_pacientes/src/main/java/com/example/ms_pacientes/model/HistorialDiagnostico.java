package com.example.ms_pacientes.model;

import java.time.LocalDate;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "historialDiagnostico")
@Schema(description = "Entidad que representa el historial de diagnóstico de un paciente, con su fecha, observación y su relación con el paciente y el diagnóstico correspondiente.")
public class HistorialDiagnostico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull(message = "La fecha es obligatoria!")
    private LocalDate fecha;

    @Size(max = 100, message = "La observacion debe contener maximo 100 caracteres!")
    private String observacion;

    @ManyToOne
    @JoinColumn(name = "paciente_id")
    private Paciente paciente;

    
    @ManyToOne
    @JoinColumn(name = "diagnostico_id")
    private Diagnostico diagnostico;

}
