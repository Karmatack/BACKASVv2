package com.adoptasalvavidas.adoptasalvavidasBack.Models.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "imagenPerro")
@Entity
public class ImagenPerro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 255, nullable = false)
    private String url;

    // Relación ManyToOne con Mascota
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idPerro", nullable = false)
    @JsonBackReference  // Evita el ciclo infinito de serialización
    private Perro perro;

    // Getters y Setters
}
