package com.foodappbackend.foodapp.security.controller;

import com.foodappbackend.foodapp.entity.Empresa;
import com.foodappbackend.foodapp.entity.Rol;
import com.foodappbackend.foodapp.entity.Usuario;
import com.foodappbackend.foodapp.enums.RolEnum;
import com.foodappbackend.foodapp.repository.EmpresaRepository;
import com.foodappbackend.foodapp.security.dto.AuthRequest;
import com.foodappbackend.foodapp.security.dto.AuthResponse;
import com.foodappbackend.foodapp.security.dto.RegistroRequest;
import com.foodappbackend.foodapp.security.dto.repository.RoleRepository;
import com.foodappbackend.foodapp.security.dto.repository.UsuarioRepository;
import com.foodappbackend.foodapp.security.services.JwtUtil;
import com.foodappbackend.foodapp.security.services.UserDetailsServiceImpl;
import com.foodappbackend.foodapp.security.services.UsuarioService;
import com.foodappbackend.foodapp.service.IEmpresaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private IEmpresaService empresaService;

    @Autowired
    private RoleRepository rolRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private EmpresaRepository empresaRepository;


    @PostMapping("/login")
    public ResponseEntity<?> iniciarSesion(@RequestBody AuthRequest authRequest){
        UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getUsername());

        //Verificar si el usuario existe y su contraseña correcta
        if(userDetails != null && passwordEncoder.matches(authRequest.getPassword(),userDetails.getPassword())){
            String jwt = jwtUtil.generateToken(userDetails);
            String nombre = obtenerNombre(authRequest.getUsername());
            String rol = obtenerRol(authRequest.getUsername());
            Long id = obtenerId(authRequest.getUsername());
            return ResponseEntity.ok(new AuthResponse(jwt,nombre,rol,id));
        }else{
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario o contraseña incorecto");
        }
    }

    private Long obtenerId(String username){
        Empresa empresa = empresaRepository.findByUsername(username);
        if(empresa!=null){
            return empresa.getId();
        }
        Usuario usuario = usuarioRepository.findByUsername(username);
        if (usuario != null){
            return usuario.getId();
        }
        return null;
    }

    private String obtenerNombre(String username){
        Empresa empresa = empresaRepository.findByUsername(username);
        if(empresa != null) {
            return empresa.getNombre();
        }
        Usuario usuario = usuarioRepository.findByUsername(username);
        if(usuario != null){
            return usuario.getNombre();
        }
        return null;
    }


    private String obtenerRol(String username){
        Empresa empresa = empresaRepository.findByUsername(username);
        if(empresa != null) {
            return rolesToString(empresa.getRoles());
        }
        Usuario usuario = usuarioRepository.findByUsername(username);
        if(usuario != null){
            return rolesToString(usuario.getRoles());
        }
        return null;
    }

    private String rolesToString(Set<Rol>roles){
        return roles.stream()
                .map(Rol::getNombre)
                .collect(Collectors.joining(","));
    }

    @PostMapping("/registro")
    public ResponseEntity<?> registarUsuario(@RequestBody RegistroRequest registroRequest) {
        if (usuarioService.buscarPorNombre(registroRequest.getUsername()) != null) {
            return ResponseEntity.badRequest().body("El nombre de usuario ya está en uso");
        }

        Usuario usuario = new Usuario();
        usuario.setUsername(registroRequest.getUsername());
        usuario.setPassword(passwordEncoder.encode(registroRequest.getPassword()));
        usuario.setNombre(registroRequest.getNombre());

        Set<Rol> roles = new HashSet<>();

        if (registroRequest.getRoles() != null) {
            for (RolEnum rolEnum : registroRequest.getRoles()) {
                Rol rolObj = rolRepository.findByNombre(rolEnum.name());
                if (rolObj != null) {
                    roles.add(rolObj);
                }
            }
            usuario.setRoles(roles);
        }

        // Si no se proporcionan roles válidos, asignar el rol por defecto de usuario
        if (roles.isEmpty()) {
            Rol userRole = rolRepository.findByNombre(RolEnum.USUARIO.getRol());
            roles.add(userRole);
            usuario.setRoles(roles);
        }


        usuarioService.guardarUsuario(usuario);
        return ResponseEntity.ok().body("{\"message\": \"Usuario registrado exitosamente\"}");
    }


    @PostMapping("/registroe")
    public ResponseEntity<?>registrarEmpresa(@RequestBody Empresa empresa){
        empresa.setNombre(empresa.getNombre());
        empresa.setLogo(empresa.getLogo());
        empresa.setDireccion(empresa.getDireccion());
        empresa.setHorario(empresa.getHorario());
        empresa.setTipo(empresa.getTipo());
        empresa.setUsername(empresa.getUsername());
        empresa.setPassword(passwordEncoder.encode(empresa.getPassword()));
        empresa.setTelefono(empresa.getTelefono());
        empresa.setProductos(empresa.getProductos());

        Set<Rol> roles = new HashSet<>();

        if (empresa.getRoles() != null) {
            for(Rol rolEnum : empresa.getRoles()) {
                Rol rolObj = rolRepository.findByNombre(rolEnum.getNombre());
                if (rolObj != null) {
                    roles.add(rolObj);
                }
            }
            empresa.setRoles(roles);
        }


        // Si no se proporcionan roles válidos, asignar el rol por defecto de usuario
        if (roles.isEmpty()) {
            Rol userRole = rolRepository.findByNombre(RolEnum.EMPRESA.getRol());
            roles.add(userRole);
            empresa.setRoles(roles);
        }

        empresaService.save(empresa);
        return new ResponseEntity<>(empresa, HttpStatus.CREATED);
    }
}
