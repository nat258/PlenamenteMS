package com.example.ms_profesionales.model;
import java.util.List;

import org.hibernate.validator.constraints.br.CPF;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "psicologo")
@Schema(description = "Entidad que representa un psicólogo, el cual tiene un RUT, nombres, apellidos, especialidades y sucursales asociadas.")
public class Psicologo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_psicologo")
    private Integer id;

    @NotNull(message = "El RUT es obligatorio!")
    @Min(value = 1000000, message = "El RUT debe contener al menos 7 caracteres!")
    @Max(value = 99999999, message = "El RUT debe contener máximo 8 caracteres!")
    @Column(name = "rut", unique = true)

    private Long rut;

    @NotBlank(message = "El digito verificador del RUT es obligatorio!")
    @Size(min = 1, max = 1, message = "El digito verificador del RUT debe ser de 1 caracter!")
    @Column(name = "dv_rut")
    private String dv_rut;

    @NotBlank(message = "El primer nombre es obligatorio!")
    @Size(min = 3, message = "El primer nombre debe contener al menos 3 caracteres!")
    @Column(name = "p_nombre")
    private String pNombre;

    @Size(min = 3, message = "El segundo nombre debe contener al menos 3 caracteres!")
    @Column(name = "s_nombre")
    private String sNombre;

    @NotBlank(message = "El primer apellido es obligatorio!")
    @Size(min = 3, message = "El primer apellido debe contener al menos 3 caracteres!")
    @Column(name = "p_apellido")    
    private String pApellido;

    @NotBlank(message = "El segundo apellido es obligatorio!")
    @Size(min = 3, message = "El segundo apellido debe contener al menos 3 caracteres!")
    @Column(name = "s_apellido")
    private String sApellido;

    @OneToMany(mappedBy = "psicologo")
    private List<ReservaHora> reservas;
    
    @ManyToMany
    @JoinTable(name = "psicologo_especialidad",
               joinColumns = @JoinColumn(name = "id_psicologo"),
               inverseJoinColumns = @JoinColumn(name = "id_especialidad"))
    @ToString.Exclude
    private List<Especialidad> especialidades;
    
    @ManyToMany
    @JoinTable(name = "psicologo_sucursal",
               joinColumns = @JoinColumn(name = "id_psicologo"),
               inverseJoinColumns = @JoinColumn(name = "id_sucursal"))
    @ToString.Exclude
    private List<Sucursal> sucursales;

}
