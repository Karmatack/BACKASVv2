package com.adoptasalvavidas.adoptasalvavidasBack.Models.Entity;

import java.time.LocalDateTime;

import com.adoptasalvavidas.adoptasalvavidasBack.Models.Enums.EstadoSolicitud;
import com.adoptasalvavidas.adoptasalvavidasBack.Models.Enums.TiempoDisponible;
import com.adoptasalvavidas.adoptasalvavidasBack.Models.Enums.TipoVivienda;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class FormularioAdopcion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "perro_id")
    private Perro perro;

    private LocalDateTime fechaSolicitud;

    @Enumerated(EnumType.STRING)
    private EstadoSolicitud estado;

    @Column(columnDefinition = "TEXT")
    private String comentario;
    
    private String nombreCompleto;
    private String telefono;
    
    @Column(columnDefinition = "TEXT")
    private String direccion;

    @Enumerated(EnumType.STRING)
    private TipoVivienda tipoVivienda;

    private Boolean tienePatios;
    private Boolean viveEnFamilia;
    private Boolean otrasMascotas;

    @Column(columnDefinition = "TEXT")
    private String experienciaMascotas;

    @Enumerated(EnumType.STRING)
    private TiempoDisponible tiempoDisponible;

    @Column(columnDefinition = "TEXT")
    private String razonesAdopcion;

    private Boolean condicionesAceptadas;
}
