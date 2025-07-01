package com.adoptasalvavidas.adoptasalvavidasBack.Respository;

import com.adoptasalvavidas.adoptasalvavidasBack.Models.Entity.ImagenPerro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ImagenPerroRepository extends JpaRepository<ImagenPerro, Integer> {
    // Buscar todas las imágenes asociadas a un perro
    List<ImagenPerro> findByPerroId(Integer idPerro);

    // Buscar una imagen por su ID
    Optional<ImagenPerro> findById(Integer id);
}
