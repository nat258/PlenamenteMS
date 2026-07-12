package com.example.ms_pacientes.model;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "paciente")
@Schema(description = "Entidad que representa un paciente, con su ID, RUT, nombre, apellido, correo, número telefónico, dirección y su relación con los historiales de diagnóstico, reservas de horas y previsión médica.")
public class Paciente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull(message = "El RUT es obligatorio!")
    @Min(value = 10000000, message = "El RUT debe contener al menos 8 caracteres!")
    @Max(value = 999999999, message = "El RUT debe contener máximo 9 caracteres!")
    private String rut;


    @NotBlank(message = "El primer nombre es obligatorio!")
    @Size(min = 3, message = "El primer nombre debe contener al menos 3 caracteres!")
    private String pNombre;

    @Size(min = 3, message = "El segundo nombre debe contener al menos 3 caracteres!")
    private String sNombre;

    @NotBlank(message = "El primer apellido es obligatorio!")
    @Size(min = 3, message = "El primer apellido debe contener al menos 3 caracteres!")
    private String pApellido;

    @NotBlank(message = "El segundo apellido es obligatorio!")
    @Size(min = 3, message = "El segundo apellido debe contener al menos 3 caracteres!")
    private String sApellido;

    @Email(message = "Debe ingresar un correo electronico valido! ejemplo@example.com")
    @NotBlank(message = "El correo electronico es obligatorio!")
    private String correo;

    @NotNull(message = "El numero telefonico es obligatorio!")
    @Min(value = 900000000, message = "El numero telefonico debe contener 9 caracteres!)")
    @Max(value = 999999999, message = "El numero telefonico debe contener 9 caracteres!)")
    private String numero;

    @NotBlank(message = "La direccion es obligatoria!")
    private String direccion;

    @OneToMany(mappedBy = "paciente")
    private List<HistorialDiagnostico> historialesDiagnostico;

    @ManyToOne
    @JoinColumn(name = "prevision_id")
    private Prevision prevision;

}
