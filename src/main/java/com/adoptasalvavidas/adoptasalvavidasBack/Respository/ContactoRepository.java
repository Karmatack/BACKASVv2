package com.adoptasalvavidas.adoptasalvavidasBack.Respository;
import com.adoptasalvavidas.adoptasalvavidasBack.Models.Entity.Contacto;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactoRepository extends JpaRepository<Contacto, Long> {
    List<Contacto> findAllByOrderByFechaEnvioDesc();
}
