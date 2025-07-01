package com.adoptasalvavidas.adoptasalvavidasBack.Controller;

import com.adoptasalvavidas.adoptasalvavidasBack.Models.Entity.ImagenPerro;
import com.adoptasalvavidas.adoptasalvavidasBack.Services.ImagenPerroServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/imagenes")
@CrossOrigin(origins = "*") 
public class ImagenPerroController {

    @Autowired
    private ImagenPerroServices imagenPerroService;

    // Listar todas las imágenes asociadas a un perro
    @GetMapping("/perro/{idPerro}")
    public List<ImagenPerro> listarPorIdPerro(@PathVariable Integer idPerro) {
        return imagenPerroService.listarPorIdPerro(idPerro);
    }

    // Listar una imagen por su ID
    @GetMapping("/{idImagenPerro}")
    public ResponseEntity<ImagenPerro> listarUno(@PathVariable Integer idImagenPerro) {
        Optional<ImagenPerro> imagenPerro = imagenPerroService.listarUno(idImagenPerro);
        return imagenPerro.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Crear o editar una imagen
    @PostMapping("/guardar")
    public ImagenPerro guardar(@RequestBody ImagenPerro imagenPerro) {
        return imagenPerroService.guardar(imagenPerro);
    }

    // Eliminar una imagen por su ID
    @DeleteMapping("/eliminar/{idImagenPerro}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer idImagenPerro) {
        try {
            imagenPerroService.eliminar(idImagenPerro);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    // Editar una imagen por su ID
    @PutMapping("/editar/{idImagen}")
    public ResponseEntity<ImagenPerro> editar(@PathVariable Integer idImagen, @RequestBody ImagenPerro imagen) {
        Optional<ImagenPerro> imagenExistente = imagenPerroService.listarUno(idImagen);
        if (imagenExistente.isPresent()) {
            ImagenPerro imagenActualizada = imagenExistente.get();

            // Actualiza los campos que deseas modificar
            imagenActualizada.setUrl(imagen.getUrl());  // Ejemplo: actualiza la URL
            imagenActualizada.setPerro(imagen.getPerro());  // Ejemplo: actualiza el ID del perro

            // Guarda la imagen actualizada
            ImagenPerro imagenGuardada = imagenPerroService.guardar(imagenActualizada);
            return ResponseEntity.ok(imagenGuardada);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}