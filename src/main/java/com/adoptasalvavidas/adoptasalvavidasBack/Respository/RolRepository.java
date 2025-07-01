package com.adoptasalvavidas.adoptasalvavidasBack.Respository;

import com.adoptasalvavidas.adoptasalvavidasBack.Models.Entity.Rol;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RolRepository  extends JpaRepository<Rol, Integer> {
    Optional<Rol> findByName(String name); 
}
