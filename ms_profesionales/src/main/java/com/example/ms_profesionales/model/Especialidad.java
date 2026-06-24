
package com.example.ms_profesionales.model;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "especialidad")
@Schema(description = "Entidad que representa una especialidad, la cual puede ser ejercida por varios psicólogos.")
public class Especialidad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_especialidad")   
    private Integer id;

    @NotBlank(message = "El nombre de especialidad es obligatorio!")
    @Column(name = "nombre_especialidad")
    private String nombre;

    @ManyToMany(mappedBy = "especialidades")
    private List<Psicologo> psicologos;

}