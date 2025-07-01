package com.adoptasalvavidas.adoptasalvavidasBack.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.adoptasalvavidas.adoptasalvavidasBack.Models.Entity.Contacto;
import com.adoptasalvavidas.adoptasalvavidasBack.Respository.ContactoRepository;


import java.util.List;

@Service
public class ContactoServiceImpl implements ContactoService {
    @Autowired
    private ContactoRepository contactoRepository;

    

    @Override
    public Contacto guardarContacto(Contacto contacto) {
        return contactoRepository.save(contacto);
    }

    @Override
    public List<Contacto> obtenerTodos() {
        return contactoRepository.findAllByOrderByFechaEnvioDesc();
    }

    @Override
    public Contacto obtenerPorId(Long id) {
        return contactoRepository.findById(id).orElse(null);
    }

    @Override
    public void eliminarPorId(Long id) {
        contactoRepository.deleteById(id);
    }

    @Override
    public Page<Contacto> obtenerPaginado(Pageable pageable) {
        return contactoRepository.findAll(pageable);
    }

}
