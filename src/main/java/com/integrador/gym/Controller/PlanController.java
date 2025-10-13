package com.integrador.gym.Controller;

import com.integrador.gym.Dto.Actualizacion.PlanActualizacionDTO;
import com.integrador.gym.Dto.Creacion.PlanCreacionDTO;
import com.integrador.gym.Dto.PlanDTO;
import com.integrador.gym.Exception.*;
import com.integrador.gym.Model.PlanModel;
import com.integrador.gym.Service.PlanService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/planes")
public class PlanController {

    @Autowired
    private PlanService planService;

    @GetMapping
    public List<PlanDTO> listarTodos() {
        return planService.listarTodos();
    }

    @GetMapping("/activos")
    public List<PlanDTO> listarActivos() {
        return planService.listarActivos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlanModel> obtener(@PathVariable Long id) {
        return planService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> crear(@Valid @RequestBody PlanCreacionDTO dto) {
        try {
            PlanDTO nuevo = planService.crear(dto);
            return ResponseEntity.ok(nuevo);
        } catch (PlanNombreDuplicado | PlanInvalido e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @Valid @RequestBody PlanActualizacionDTO dto) {
        try {
            PlanDTO actualizado = planService.actualizar(id, dto);
            return ResponseEntity.ok(actualizado);
        } catch (PlanNoEncontrado e) {
            return ResponseEntity.notFound().build();
        } catch (PlanNombreDuplicado | PlanInvalido e) {
            return ResponseEntity.badRequest().body(errorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(errorResponse("Error interno: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        try {
            planService.eliminar(id); // Solo desactiva
            return ResponseEntity.noContent().build();
        } catch (PlanNoEncontrado e) {
            return ResponseEntity.notFound().build();
        }
    }

    private Map<String, String> errorResponse(String mensaje) {
        Map<String, String> response = new HashMap<>();
        response.put("error", mensaje);
        return response;
    }
}
