package com.foodappbackend.foodapp.security.services;

import com.foodappbackend.foodapp.entity.Rol;
import com.foodappbackend.foodapp.entity.Usuario;
import com.foodappbackend.foodapp.security.dto.repository.UsuarioRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Usuario buscarPorNombre(String username) {
        return usuarioRepository.findByUsername(username);
    }

    @Transactional
    public Usuario guardarUsuario(Usuario usuario) {
        usuario.setUsername(usuario.getUsername());
        usuario.setRoles(usuario.getRoles());
        usuario.setPassword(usuario.getPassword());
        usuario.setNombre(usuario.getNombre());

        Usuario usuarioGuardado = usuarioRepository.save(usuario);

        for (Rol rol : usuario.getRoles()) {
            rol.getUsuarios().add(usuarioGuardado);
        }
        return usuarioGuardado;
    }

    public Usuario updateUsuario(Usuario usuario) {
        Usuario existingUsuario = usuarioRepository.findById(usuario.getId()).get();
        existingUsuario.setUsername(usuario.getUsername());
        existingUsuario.setNombre(usuario.getNombre());
        existingUsuario.setApellido(usuario.getApellido());
        existingUsuario.setDireccion(usuario.getDireccion());
        existingUsuario.setTelefono(usuario.getTelefono());

        return usuarioRepository.save(existingUsuario);
    }

    public List<Usuario> findAll() {
        return usuarioRepository.findAll();
    }
    public Usuario findById(Long id) {
        return usuarioRepository.findById(id).get();
    }
    public void deleteById(Long id) {
        usuarioRepository.deleteById(id);
    }


}
