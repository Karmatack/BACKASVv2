package com.adoptasalvavidas.adoptasalvavidasBack.Respository;

import com.adoptasalvavidas.adoptasalvavidasBack.Models.Entity.Perro;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PerroRepository extends JpaRepository<Perro, Integer> {
    List<Perro> findByDisponibleTrue();
}
