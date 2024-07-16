package com.foodappbackend.foodapp.controller;
import com.foodappbackend.foodapp.entity.Usuario;
import com.foodappbackend.foodapp.security.services.UsuarioService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@AllArgsConstructor
public class UsuarioController {


    @Autowired
    private UsuarioService usuarioService;


    @GetMapping
    public ResponseEntity<List<Usuario>> getAllUsuarios() {
        List<Usuario> usuariosList = usuarioService.findAll();
        return new ResponseEntity<>(usuariosList, HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<Usuario> findUsuarioById(@PathVariable("id") Long id) {
        Usuario usuario = usuarioService.findById(id);
        return new ResponseEntity<>(usuario, HttpStatus.OK);
    }

    @PutMapping("{id}")
    public ResponseEntity<Usuario> updateUsuario(@PathVariable("id") Long id, @RequestBody Usuario usuario) {
        usuario.setId(id);
        Usuario updatedUsuario = usuarioService.updateUsuario(usuario);
        return new ResponseEntity<>(updatedUsuario, HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteUsuario(@PathVariable("id") Long id) {
        usuarioService.deleteById(id);
        return new ResponseEntity<>("Usuario was deleted successfully", HttpStatus.OK);
    }


}
