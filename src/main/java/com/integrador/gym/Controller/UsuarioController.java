package com.integrador.gym.Controller;

import com.integrador.gym.CQRS.UsuarioWriteService;
import com.integrador.gym.Dto.Actualizacion.UsuarioActualizacionDTO;
import com.integrador.gym.Dto.Creacion.UsuarioCreacionDTO;
import com.integrador.gym.Dto.UsuarioDTO;
import com.integrador.gym.Exception.DniYaRegistrado;
import com.integrador.gym.Exception.EmailYaRegistrado;
import com.integrador.gym.Exception.RolNoPermitido;
import com.integrador.gym.Exception.UsuarioNoEncontrado;
import com.integrador.gym.Model.UsuarioModel;
import com.integrador.gym.Service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioWriteService writeService;

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public List<UsuarioModel> listar() {
        return usuarioService.listarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioModel> obtener(@PathVariable Long id) {
        return usuarioService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> crear(@Valid @RequestBody UsuarioCreacionDTO dto) {
        try {
            return ResponseEntity.ok(usuarioService.crear(dto));
        } catch (EmailYaRegistrado | DniYaRegistrado e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @Valid @RequestBody UsuarioActualizacionDTO dto) {
        try {
            return ResponseEntity.ok(usuarioService.actualizar(id, dto));
        } catch (UsuarioNoEncontrado e) {
            return ResponseEntity.notFound().build();
        } catch (EmailYaRegistrado | DniYaRegistrado | RolNoPermitido e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        try {
            usuarioService.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    private Map<String, String> errorResponse(String mensaje) {
        Map<String, String> response = new HashMap<>();
        response.put("error", mensaje);
        return response;
    }
}
