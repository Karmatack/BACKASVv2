package com.adoptasalvavidas.adoptasalvavidasBack.Controller;

import com.adoptasalvavidas.adoptasalvavidasBack.Models.Entity.Perro;
import com.adoptasalvavidas.adoptasalvavidasBack.Services.PerroServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/perro")
@CrossOrigin(origins = "*") 
public class PerroController {
    @Autowired
    private PerroServices perroServices;

    // Registrar un nuevo perro
    @PostMapping("/registrar")
    public ResponseEntity<Perro> registrarPerro(@RequestBody Perro perro) {
        Perro registrado = perroServices.registrar(perro);
        if (registrado != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(registrado);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    // Buscar un perro por ID
    @GetMapping("/{id}")
    public ResponseEntity<Perro> buscarPerroPorId(@PathVariable int id) {
        Optional<Perro> perro = perroServices.buscarId(id);
        return perro.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Eliminar un perro por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarPerroPorId(@PathVariable int id) {
        boolean eliminado = perroServices.eliminarPorId(id);
        if (eliminado) {
            return new ResponseEntity<>("Perro eliminado con éxito", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Perro no encontrado", HttpStatus.NOT_FOUND);
        }
    }

    // Listar todos los perros
    @GetMapping("/listar")
    public ResponseEntity<List<Perro>> listarPerros() {
        List<Perro> perros = perroServices.listar();
        return new ResponseEntity<>(perros, HttpStatus.OK);
    }

    //ver solo los perros disponibles para adopción
    @GetMapping("/disponibles")
    public ResponseEntity<List<Perro>> listarPerrosDisponibles() {
        List<Perro> perrosDisponibles = perroServices.listarDisponibles();
        return new ResponseEntity<>(perrosDisponibles, HttpStatus.OK);
    }

    @PutMapping("/perros/{id}/no-disponible")
    public ResponseEntity<?> marcarNoDisponible(@PathVariable int id) {
        try {
            perroServices.noDisponible(id);
            return ResponseEntity.ok("Perro marcado como no disponible");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping("/perros/{id}/disponible")
    public ResponseEntity<?> marcarDisponible(@PathVariable int id) {
        try {
            perroServices.disponible(id);
            return ResponseEntity.ok("Perro marcado como disponible");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    @PutMapping("/editar/{id}")
    public ResponseEntity<?> editarPerro(@PathVariable int id, @RequestBody Perro perroActualizado) {
        Optional<Perro> perroExistenteOpt = perroServices.buscarId(id);

        if (perroExistenteOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Perro no encontrado");
        }

        Perro perroExistente = perroExistenteOpt.get();

        // Actualizamos solo los campos válidos
        perroExistente.setNombre(perroActualizado.getNombre());
        perroExistente.setEdad(perroActualizado.getEdad());
        perroExistente.setTamañoPerro(perroActualizado.getTamañoPerro());
        perroExistente.setDescripcion(perroActualizado.getDescripcion());
        perroExistente.setDisponible(perroActualizado.getDisponible());

        // Actualizar imágenes: opcionalmente reemplazamos la lista completa
        perroExistente.getImagenes().clear();
        if (perroActualizado.getImagenes() != null) {
            perroActualizado.getImagenes().forEach(imagen -> imagen.setPerro(perroExistente)); // mantener relación bidireccional
            perroExistente.getImagenes().addAll(perroActualizado.getImagenes());
        }

        Perro perroGuardado = perroServices.registrar(perroExistente); // reutilizamos registrar que usa save()
        return ResponseEntity.ok(perroGuardado);
    }


}
