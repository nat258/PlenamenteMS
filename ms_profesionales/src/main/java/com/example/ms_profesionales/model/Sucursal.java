package com.example.ms_profesionales.model;
import java.util.List;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "sucursal")

public class Sucursal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "El nombre de la sucursal es obligatorio!")
    private String nombre;
    
    @NotBlank(message = "La direccion no puede estar vacia!")
    private String direccion;

    @ManyToMany(mappedBy = "sucursales")
    private List<Psicologo> psicologos;

    @ManyToOne
    @JoinColumn(name = "comuna_id")
    private Comuna comuna;

}