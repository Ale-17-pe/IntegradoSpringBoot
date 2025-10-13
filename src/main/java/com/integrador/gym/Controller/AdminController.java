package com.integrador.gym.Controller;

import com.integrador.gym.Dto.Actualizacion.PlanActualizacionDTO;
import com.integrador.gym.Dto.Actualizacion.UsuarioActualizacionDTO;
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
        return "admin/dashboard-admin";
    }

    @GetMapping("/usuarios/crear")
    public String mostrarFormularioCreacionUsuario(Model model) {
        model.addAttribute("usuarioDTO", new UsuarioCreacionDTO());
        model.addAttribute("rolesDisponibles", Roles.values());
        return "admin/crear-usuario";
    }

    @PostMapping("/usuarios/crear")
    public String crearUsuario(@Valid @ModelAttribute("usuarioDTO") UsuarioCreacionDTO dto,
                               BindingResult bindingResult,
                               Model model,
                               RedirectAttributes redirectAttrs) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("rolesDisponibles", Roles.values());
            return "admin/crear-usuario";
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
            planService.crear(dto); // Asumimos un método crearPlan
            redirectAttrs.addFlashAttribute("success",
                    "El Plan '" + dto.getNombre() + "' fue creado exitosamente.");
            return "redirect:/admin/planes/crear";
        } catch (Exception e) {
            redirectAttrs.addFlashAttribute("error", "Error: " + e.getMessage());
            return "redirect:/admin/planes/crear";
        }
    }

    @GetMapping("/usuarios")
    public String listarUsuarios(Model model) {
        System.out.println("Entró al método listarUsuarios()");
        model.addAttribute("usuarios", usuarioService.listarTodos());
        return "admin/lista-usuarios";
    }

    // Muestra el formulario para editar un usuario existente
    @GetMapping("/usuarios/editar/{id}")
    public String mostrarFormularioEdicionUsuario(@PathVariable Long id, Model model) {
        try {
            model.addAttribute("usuarioDTO", usuarioService.obtenerDTOParaEdicion(id));
            model.addAttribute("rolesDisponibles", Roles.values());

            return "admin/editar-usuario"; // Vista: admin/editar-usuario.html
        } catch (Exception e) {
            return "redirect:/admin/usuarios";
        }
    }

    // Procesa la edición del usuario (POST)
    @PostMapping("/usuarios/editar/{id}")
    public String editarUsuario(@PathVariable Long id,
                                @Valid @ModelAttribute("usuarioDTO") UsuarioActualizacionDTO dto,
                                BindingResult bindingResult,
                                Model model,
                                RedirectAttributes redirectAttrs) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("rolesDisponibles", Roles.values());
            return "redirect:/admin/editar-usuario";
        }

        try {
            usuarioService.actualizar(id, dto); // Asume el método actualizar en el servicio
            redirectAttrs.addFlashAttribute("success",
                    "El usuario '" + dto.getDni() + "' fue actualizado exitosamente.");
            return "redirect:/admin/usuarios";
        } catch (Exception e) {
            redirectAttrs.addFlashAttribute("error", "Error al actualizar: " + e.getMessage());
            return "redirect:/admin/usuarios/editar/" + id;
        }
    }

    @GetMapping("/planes")
    public String listarPlanes(Model model) {
        model.addAttribute("planes", planService.listarTodos());
        return "redirect:/admin/lista-planes"; // Vista: admin/lista-planes.html
    }
    @GetMapping("/planes/editar/{id}")
    public String mostrarFormularioEdicionPlan(@PathVariable Long id, Model model) {
        try {
            // Necesitas obtener el DTO de actualización/edición del servicio
            model.addAttribute("planDTO", planService.obtenerDTOParaEdicion(id));
            return "admin/editar-plan"; // Vista: admin/editar-plan.html
        } catch (Exception e) {
            // Manejo de error si el plan no existe
            return "redirect:/admin/planes";
        }
    }
    @PostMapping("/planes/editar/{id}")
    public String editarPlan(@PathVariable Long id,
                             @Valid @ModelAttribute("planDTO") PlanActualizacionDTO dto, // ⬅️ Asume este DTO
                             BindingResult bindingResult,
                             RedirectAttributes redirectAttrs) {

        if (bindingResult.hasErrors()) {
            return "redirect:/admin/editar-plan";
        }

        try {
            planService.actualizar(id, dto); // Asume el método actualizar en el servicio
            redirectAttrs.addFlashAttribute("success",
                    "El plan '" + dto.getNombre() + "' fue actualizado exitosamente.");
            return "redirect:/admin/planes";
        } catch (Exception e) {
            redirectAttrs.addFlashAttribute("error", "Error al actualizar: " + e.getMessage());
            return "redirect:/admin/planes/editar/" + id;
        }
    }

}
