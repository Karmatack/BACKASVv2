package com.adoptasalvavidas.adoptasalvavidasBack.Services;


import com.adoptasalvavidas.adoptasalvavidasBack.Models.Entity.Rol;
import com.adoptasalvavidas.adoptasalvavidasBack.Respository.RolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class RolServices {

    @Autowired
    private RolRepository rolRepository;

    @PostConstruct
    public void init() {
        // Lista de roles que quieres verificar
        List<String> roleNames = Arrays.asList("ADMIN", "CLIENTE");

        // Verificar si los roles existen uno a uno
        for (String roleName : roleNames) {
            Optional<Rol> existingRole = rolRepository.findByName(roleName);
            
            // Si el rol no existe, lo creamos
            if (existingRole.isEmpty()) {
                // Si el rol no existe, lo creamos
                rolRepository.save(new Rol(roleName));
            }
        }
    }
    
}
