package com.example.ms_agendamiento.model;

import java.time.LocalDateTime;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "reservaHora")

public class ReservaHora {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull(message = "La fecha y hora de la reserva es obligatoria!")
    private LocalDateTime fechaHora;

    @NotBlank(message = "El estado de la reserva es obligatorio!")
    private String estado;

    @Column(name = "paciente_id", nullable = false)
    private Integer pacienteId;

    @Column(name = "psicologo_id", nullable = false)
    private Integer psicologoId;

    @OneToOne(cascade = CascadeType.ALL,orphanRemoval = true)
    @JoinColumn(name = "boleta_id")
    private Boleta boleta;

}

