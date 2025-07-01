package com.adoptasalvavidas.adoptasalvavidasBack.Services;

import com.adoptasalvavidas.adoptasalvavidasBack.Models.Entity.ImagenPerro;
import com.adoptasalvavidas.adoptasalvavidasBack.Models.Entity.Perro;
import com.adoptasalvavidas.adoptasalvavidasBack.Respository.ImagenPerroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ImagenPerroServices {
    @Autowired
    private ImagenPerroRepository imagenPerroRepository;

    public ImagenPerro registrarImagen(Perro perro, ImagenPerro imagenPerro) {
        if (perro == null) {
            throw new IllegalArgumentException("El perro no puede ser nulo");
        }
        imagenPerro.setPerro(perro);
        return imagenPerroRepository.save(imagenPerro);
    }
    public List<ImagenPerro> listarPorIdPerro(Integer idPerro) {
        return imagenPerroRepository.findByPerroId(idPerro);
    }

    // Listar una imagen por su ID
    public Optional<ImagenPerro> listarUno(Integer idImagenPerro) {
        return imagenPerroRepository.findById(idImagenPerro);
    }

    // Crear o editar una imagen
    public ImagenPerro guardar(ImagenPerro imagenPerro) {
        return imagenPerroRepository.save(imagenPerro);
    }

    // Eliminar una imagen por su ID
    public void eliminar(Integer idImagenPerro) {
        Optional<ImagenPerro> imagenPerro = imagenPerroRepository.findById(idImagenPerro);
        if (imagenPerro.isPresent()) {
            imagenPerroRepository.delete(imagenPerro.get());
        } else {
            throw new RuntimeException("Imagen no encontrada");
        }
    }
}
