package com.integrador.gym.Service.Impl;

import com.integrador.gym.Model.UsuarioModel;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Collection;
import java.util.List;

public class CustomUserDetails implements UserDetails {
    private final UsuarioModel usuario;

    public CustomUserDetails(UsuarioModel usuario) {
        this.usuario = usuario;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + usuario.getRoles().name()));
    }

    @Override
    public String getPassword() {
        return usuario.getPassword();
    }

    @Override
    public String getUsername() {
        // Aquí usamos el DNI como identificador principal
        return usuario.getDni();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return usuario.getEstado().name().equalsIgnoreCase("ACTIVO");
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return usuario.getEstado().name().equalsIgnoreCase("ACTIVO");
    }

    public UsuarioModel getUsuario() {
        return usuario;
    }
}
