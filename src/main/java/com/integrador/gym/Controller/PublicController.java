package com.integrador.gym.Controller;

import com.integrador.gym.Service.PlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class PublicController {

    private final PlanService planService;

    // ... (otras rutas como /, /login, /registro)

    // Mapea la ruta pública para ver todos los planes disponibles
    @GetMapping("/planes") // Esta ruta coincide con el archivo planes.html
    public String mostrarPlanes(Model model) {

        model.addAttribute("planesDisponibles", planService.listarTodos());

        return "planes";
    }
}
