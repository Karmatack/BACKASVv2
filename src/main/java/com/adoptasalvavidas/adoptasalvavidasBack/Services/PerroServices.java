package com.adoptasalvavidas.adoptasalvavidasBack.Services;

import com.adoptasalvavidas.adoptasalvavidasBack.Models.Entity.Perro;
import com.adoptasalvavidas.adoptasalvavidasBack.Respository.PerroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PerroServices {
    @Autowired
    private PerroRepository perroRepository;

    public Perro registrar(Perro perro){
        return perroRepository.save(perro);
    }

    public Optional<Perro> buscarId(int id) {
        return perroRepository.findById(id);
    }
    public boolean eliminarPorId(int id) {
        Optional<Perro> perroOptional = perroRepository.findById(id);
        if (perroOptional.isPresent()) {
            perroRepository.deleteById(id);
            return true; // Eliminación exitosa
        } else {
            return false; // No existe el perro
        }
    }


    public List<Perro> listar(){
        return perroRepository.findAll();
    }

    public List<Perro> listarDisponibles() {
        return perroRepository.findByDisponibleTrue();
    }

    public void noDisponible(int id) {
        Perro perro = perroRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Perro no encontrado"));
        perro.setDisponible(false);
        perroRepository.save(perro);
    }


    public void disponible(int id) {
        Perro perro = perroRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Perro no encontrado"));
        perro.setDisponible(true);
        perroRepository.save(perro);
    }





}
