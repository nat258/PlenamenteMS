package com.example.ms_pacientes.model;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "diagnostico")
@Schema(description = "Entidad que representa un diagnóstico médico, con su nombre, descripción y su relación con los historiales de diagnóstico.")
public class Diagnostico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "El nombre de diagnostico no puede estar vacio!")
    @Size(max = 50, message = "El nombre debe contener maximo 50 caracteres!")
    private String nombre;

    @Size(max = 100, message = "La descripcion debe contener maximo 100 caracteres!")
    private String descripcion;
    
    @OneToMany(mappedBy = "diagnostico")
    private List<HistorialDiagnostico> historialesDiagnostico;

}
