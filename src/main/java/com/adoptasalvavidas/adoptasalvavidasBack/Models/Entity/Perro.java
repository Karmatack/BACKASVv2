package com.adoptasalvavidas.adoptasalvavidasBack.Models.Entity;

import com.adoptasalvavidas.adoptasalvavidasBack.Models.Enums.TamañoPerro;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "perro")
@Entity
public class Perro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;


    private String nombre;

    private int edad;

    @Enumerated(EnumType.STRING)
    private TamañoPerro tamañoPerro;


    private String descripcion;

    @Column(nullable = false)
    private Boolean disponible = true;

    // Relación OneToMany con Imagenes

    @OneToMany(mappedBy = "perro", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference  // Evita el ciclo infinito de serialización
    private List<ImagenPerro> imagenes;
}
