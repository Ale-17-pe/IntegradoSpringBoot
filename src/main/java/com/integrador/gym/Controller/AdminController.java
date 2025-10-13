package com.integrador.gym.Controller;

import com.integrador.gym.Dto.Creacion.PlanCreacionDTO;
import com.integrador.gym.Dto.Creacion.UsuarioCreacionDTO;
import com.integrador.gym.Model.Enum.Roles;
import com.integrador.gym.Service.PlanService;
import com.integrador.gym.Service.UsuarioService;
import jakarta.validation.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private PlanService planService;

    @GetMapping("/dashboard")
    public String adminDashboard() {
        return "admin/dashboard"; // Vista para el menú principal del administrador
    }

    @GetMapping("/usuarios/crear")
    public String mostrarFormularioCreacionUsuario(Model model) {
        model.addAttribute("usuarioDTO", new UsuarioCreacionDTO());
        // Pasamos todos los roles disponibles para el select box
        model.addAttribute("rolesDisponibles", Roles.values());
        return "admin/crear-usuario"; // Vista: admin/crear-usuario.html
    }


    @PostMapping("/usuarios/crear")
    public String crearUsuario(@Valid @ModelAttribute("usuarioDTO") UsuarioCreacionDTO dto,
                               BindingResult bindingResult,
                               Model model,
                               RedirectAttributes redirectAttrs) {

        if (bindingResult.hasErrors()) {
            // Si hay errores, DEBEMOS RE-AGREGAR LOS ROLES AL MODEL (no a redirectAttrs)
            model.addAttribute("rolesDisponibles", Roles.values());
            return "admin/crear-usuario"; // Regresa directamente a la vista
        }

        try {
            usuarioService.crear(dto); // Asumo que tienes un método 'crear' en UsuarioService
            redirectAttrs.addFlashAttribute("success",
                    "El usuario " + dto.getDni() + " fue creado exitosamente.");
            return "redirect:/admin/usuarios/crear";
        } catch (Exception e) {
            redirectAttrs.addFlashAttribute("error", "Error: " + e.getMessage());
            return "redirect:/admin/usuarios/crear";
        }
    }

    // --- GESTIÓN DE PLANES (CREAR) ---

    @GetMapping("/planes/crear")
    public String mostrarFormularioCreacionPlan(Model model) {
        model.addAttribute("planDTO", new PlanCreacionDTO());
        return "admin/crear-plan"; // Vista: admin/crear-plan.html
    }

    @PostMapping("/planes/crear")
    public String crearPlan(@Valid @ModelAttribute("planDTO") PlanCreacionDTO dto,
                            BindingResult bindingResult,
                            RedirectAttributes redirectAttrs) {

        if (bindingResult.hasErrors()) {
            return "admin/crear-plan";
        }

        try {
            // Llamada al PlanService para guardar el plan
            planService.crear(dto); // Asumimos un método crearPlan
            redirectAttrs.addFlashAttribute("success",
                    "El Plan '" + dto.getNombre() + "' fue creado exitosamente.");
            return "redirect:/admin/planes/crear";
        } catch (Exception e) {
            redirectAttrs.addFlashAttribute("error", "Error: " + e.getMessage());
            return "redirect:/admin/planes/crear";
        }
    }
}
