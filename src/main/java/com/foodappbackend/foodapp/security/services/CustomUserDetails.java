package com.foodappbackend.foodapp.security.services;

import com.foodappbackend.foodapp.entity.Empresa;
import com.foodappbackend.foodapp.entity.Usuario;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public class CustomUserDetails implements UserDetails {

    private  Usuario usuario;
    private  Empresa empresa;
    private Set<GrantedAuthority> authorities;


    public CustomUserDetails(Usuario usuario){
        this.usuario = usuario;
        this.empresa = null;

    }

    public CustomUserDetails(Empresa empresa){
        this.usuario = null;
        this.empresa = empresa;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        if(usuario != null){
            return usuario.getPassword();
        }else{
            return empresa.getPassword();
        }
    }

    @Override
    public String getUsername() {
        if(usuario != null){
            return usuario.getUsername();
        }else{
            return empresa.getUsername();
        }
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
