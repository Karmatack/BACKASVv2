package com.adoptasalvavidas.adoptasalvavidasBack.Services;

import com.adoptasalvavidas.adoptasalvavidasBack.Models.Entity.Contacto;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ContactoService {
    Contacto guardarContacto(Contacto contacto);
    List<Contacto> obtenerTodos();
    Contacto obtenerPorId(Long id);
    void eliminarPorId(Long id);
    Page<Contacto> obtenerPaginado(Pageable pageable);
}
