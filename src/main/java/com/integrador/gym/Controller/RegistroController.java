package com.integrador.gym.Controller;

import com.integrador.gym.Dto.Creacion.UsuarioCreacionDTO;
import com.integrador.gym.Dto.UsuarioDTO;
import com.integrador.gym.Model.Enum.Roles;
import com.integrador.gym.Service.ClienteService;
import com.integrador.gym.Service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class RegistroController {
    @Autowired
    private ClienteService clienteService;

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/registro")
    public String mostrarFormularioRegistro(Model model) {

        // 🔥 SOLUCIÓN: Agregar una instancia VACÍA del DTO al modelo
        // El nombre de la variable debe coincidir con th:object="${usuario}"
        model.addAttribute("usuario", new UsuarioCreacionDTO());

        // Además, asegúrate de que el Thymeleaf pueda acceder a la clase Enum Género
        // (Esto es clave para el campo select que tienes en la línea 82)
        // Puedes inyectar el Enum si lo necesitas en otros lugares.
        model.addAttribute("generosDisponibles", com.integrador.gym.Model.Enum.Genero.values());

        return "registro";
    }

    // Procesar el formulario de registro
    @PostMapping("/registro")
    public String procesarRegistro(@Valid @ModelAttribute("usuario") UsuarioCreacionDTO dto,
                                   BindingResult bindingResult,
                                   RedirectAttributes redirectAttrs) {
        // Validaciones del formulario
        if (bindingResult.hasErrors()) {

            // 🔥 AÑADE ESTO TEMPORALMENTE PARA VER LA CAUSA REAL
            bindingResult.getAllErrors().forEach(error -> {
                System.out.println("Error de validación: " + error.getDefaultMessage());
                System.out.println("Campo: " + (error.getCodes() != null ? error.getCodes()[0] : "Global"));
            });
            // -----------------------------------------------------------

            redirectAttrs.addFlashAttribute("org.springframework.validation.BindingResult.usuario", bindingResult);
            redirectAttrs.addFlashAttribute("usuario", dto);
            redirectAttrs.addFlashAttribute("error", "Por favor, corrige los errores del formulario.");
            return "redirect:/registro";
        }

        try {
            // 🔥 SOLUCIÓN: FORZAR EL ROL ANTES DE USAR EL DTO
            // Asignamos el rol de seguridad en el backend.
            dto.setRoles(Roles.CLIENTE);

            usuarioService.crear(dto);
            redirectAttrs.addAttribute("success", true);
            return "redirect:/registro";
        } catch (Exception e) {
            // ... manejo de excepciones ...
            return "redirect:/registro";
        }
    }


}
