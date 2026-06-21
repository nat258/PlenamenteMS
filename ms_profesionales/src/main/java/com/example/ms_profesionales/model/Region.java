package com.example.ms_profesionales.model;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "region")
@Schema(description = "Entidad que representa una región, la cual puede contener varias comunas asociadas.")

public class Region {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_region")
    private Integer id;

    @NotBlank(message = "El nombre de region no puede estar vacio!")
    @Column(name = "nombre_region", unique = true)
    private String nombre;

    @OneToMany(mappedBy = "region")
    private List<Comuna> comunas;

}