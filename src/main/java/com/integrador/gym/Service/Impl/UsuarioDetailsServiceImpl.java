package com.integrador.gym.Service.Impl;

import com.integrador.gym.Model.UsuarioModel;
import com.integrador.gym.Repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UsuarioDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private final UsuarioRepository usuarioRepository;

    public UsuarioDetailsServiceImpl(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    @Cacheable(value = "    userDetailsCache")
    public UserDetails loadUserByUsername(String dni) throws UsernameNotFoundException {

        UsuarioModel usuario = usuarioRepository.findByDni(dni)

                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con DNI: " + dni));

        if (usuario.getEstado() == null || !usuario.getEstado().name().equalsIgnoreCase("ACTIVO")) {
            throw new UsernameNotFoundException("El usuario está inactivo o deshabilitado: " + dni);
        }

// Código Corregido (Lanza la excepción correcta)
        if (usuario.getEstado() == null || !usuario.getEstado().name().equalsIgnoreCase("ACTIVO")) {
            // Spring Security necesita esta excepción para el manejo de cuentas deshabilitadas
            throw new DisabledException("El usuario está inactivo o deshabilitado: " + dni);
        }

        System.out.println("DEBUG HASH LEÍDO: " + usuario.getPassword() + " | Longitud: " + usuario.getPassword().length());

        return new User(
                usuario.getDni(), // username
                usuario.getPassword(),
                Collections.singletonList(
                        new SimpleGrantedAuthority("ROLE_" + usuario.getRoles().name())
                )
        );
    }
}

