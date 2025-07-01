package com.adoptasalvavidas.adoptasalvavidasBack.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import com.adoptasalvavidas.adoptasalvavidasBack.Models.Entity.Contacto;
import com.adoptasalvavidas.adoptasalvavidasBack.Services.ContactoService;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/contacto")
@CrossOrigin(origins = "*") 
public class ContactoController {
    @Autowired
    private ContactoService contactoService;

    @PostMapping
    public ResponseEntity<Contacto> crearContacto(@RequestBody Contacto contacto) {
        return ResponseEntity.ok(contactoService.guardarContacto(contacto));
    }

    @GetMapping
    public ResponseEntity<List<Contacto>> listarContactos() {
        return ResponseEntity.ok(contactoService.obtenerTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Contacto> obtenerPorId(@PathVariable Long id) {
        Contacto contacto = contactoService.obtenerPorId(id);
        return contacto != null ? ResponseEntity.ok(contacto) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarContacto(@PathVariable Long id) {
        contactoService.eliminarPorId(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/page")
    public ResponseEntity<Page<Contacto>> listarContactosPaginados(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(contactoService.obtenerPaginado(pageable));
    }

}
