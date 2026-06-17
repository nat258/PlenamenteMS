package com.example.ms_profesionales.model;
import java.util.List;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "comuna")

public class Comuna {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_comuna")
    private Integer id;
 
    @NotBlank(message = "El nombre de comuna no puede estar vacio!")
    @Column(name = "nombre_comuna", nullable = false)
    private String nombre;

    @ManyToOne
    @JoinColumn(name = "region_id")
    private Region region;

    @OneToMany(mappedBy = "comuna")
    private List<Sucursal> sucursales;

}