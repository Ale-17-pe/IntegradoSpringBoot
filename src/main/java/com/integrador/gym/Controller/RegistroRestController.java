package com.integrador.gym.Controller;

import com.integrador.gym.Dto.Creacion.UsuarioCreacionDTO;
import com.integrador.gym.Dto.UsuarioDTO;
import com.integrador.gym.Service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public class RegistroRestController {
    @Autowired
    private UsuarioService usuarioService;
    @PostMapping
    public ResponseEntity<UsuarioDTO> registrarCliente(@Valid @RequestBody UsuarioCreacionDTO dto) {
        try {
            UsuarioDTO nuevoUsuario = usuarioService.crear(dto);
            return new ResponseEntity<>(nuevoUsuario, HttpStatus.CREATED);

        } catch (Exception e) {
            throw new RuntimeException("Error en el registro: " + e.getMessage(), e);
        }
    }
}
