package com.adoptasalvavidas.adoptasalvavidasBack.Models.DTO;


import com.adoptasalvavidas.adoptasalvavidasBack.Models.Enums.TiempoDisponible;
import com.adoptasalvavidas.adoptasalvavidasBack.Models.Enums.TipoVivienda;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FormularioAdopcionDTO {
    private Long  usuarioId;
    private Integer  perroId;

    private String comentario;
    private String nombreCompleto;
    private String telefono;
    private String direccion;

    private TipoVivienda tipoVivienda;
    private Boolean tienePatios;
    private Boolean viveEnFamilia;
    private Boolean otrasMascotas;
    private String experienciaMascotas;
    private TiempoDisponible tiempoDisponible;
    private String razonesAdopcion;
    private Boolean condicionesAceptadas;
}
