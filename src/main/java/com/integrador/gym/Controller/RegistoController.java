package com.integrador.gym.Controller;

import com.integrador.gym.Dto.ClienteDTO;
import com.integrador.gym.Dto.Creacion.ClienteCreacionDTO;
import com.integrador.gym.Dto.Creacion.UsuarioCreacionDTO;
import com.integrador.gym.Exception.ClienteDniDuplicado;
import com.integrador.gym.Exception.ClienteNoEncontrado;
import com.integrador.gym.Service.ClienteService;
import com.integrador.gym.Service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
public class RegistoController {
    @Autowired
    private ClienteService clienteService;

    @PostMapping("/registro")
    public ResponseEntity<?> crear(@Valid @RequestBody ClienteCreacionDTO dto) {
        try {
            ClienteDTO nuevo = clienteService.crear(dto);
            return ResponseEntity.ok(nuevo);
        } catch (ClienteDniDuplicado | ClienteNoEncontrado e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "Error interno: " + e.getMessage()));
        }
    }
}
