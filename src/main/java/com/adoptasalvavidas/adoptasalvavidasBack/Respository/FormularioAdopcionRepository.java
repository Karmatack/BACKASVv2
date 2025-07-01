package com.adoptasalvavidas.adoptasalvavidasBack.Respository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.adoptasalvavidas.adoptasalvavidasBack.Models.Entity.FormularioAdopcion;
import com.adoptasalvavidas.adoptasalvavidasBack.Models.Enums.EstadoSolicitud;

@Repository
public interface FormularioAdopcionRepository extends JpaRepository<FormularioAdopcion, Long> {
    List<FormularioAdopcion> findByUsuarioId(Long usuarioId);
    List<FormularioAdopcion> findByEstado(EstadoSolicitud estado);
}
