package com.integrador.gym.Controller;

import com.integrador.gym.Dto.Actualizacion.ClienteActualizacionDTO;
import com.integrador.gym.Model.ClienteModel;
import com.integrador.gym.Service.ClienteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class PerfilController {
    @Autowired
    private ClienteService clienteService;

    @GetMapping("/perfil")
    public String mostrarPerfil(Authentication authentication, Model model) {

        String dniUsuario = authentication.getName();

        ClienteModel cliente = clienteService.obtenerClientePorDni(dniUsuario)
                .orElse(null);

        if (cliente == null) {
            model.addAttribute("error", "No se encontraron los datos de perfil asociados a tu cuenta.");
            return "dashboard";
        }

        model.addAttribute("cliente", cliente);
        model.addAttribute("rol", authentication.getAuthorities().iterator().next().getAuthority().replace("ROLE_", ""));

        return "perfil";
    }

    @GetMapping("/perfil/editar")
    public String mostrarFormularioEdicion(Authentication authentication, Model model) {
        String dniUsuario = authentication.getName();

        ClienteModel cliente = clienteService.obtenerClientePorDni(dniUsuario)
                .orElseThrow(() -> new RuntimeException("Cliente de perfil no encontrado."));

        // Mapeo del Model a DTO para el formulario
        ClienteActualizacionDTO dto = convertirClienteActualizacionDTO(cliente);

        model.addAttribute("perfilDTO", dto); // Usamos "perfilDTO" para la vista
        return "perfil-edicion";
    }
    @PostMapping("/perfil/editar")
    public String procesarEdicionPerfil(@Valid @ModelAttribute("perfilDTO") ClienteActualizacionDTO dto,
                                        BindingResult bindingResult,
                                        Authentication authentication,
                                        RedirectAttributes redirectAttrs) {

        if (bindingResult.hasErrors()) {
            return "perfil-edicion";
        }

        String dniSesion = authentication.getName();

        try {
            // Llamada al nuevo método del servicio
            clienteService.actualizar(dniSesion, dto);
            redirectAttrs.addFlashAttribute("success", "Tu perfil ha sido actualizado exitosamente.");

            return "redirect:/perfil";

        } catch (Exception e) {
            bindingResult.rejectValue("dni", "error.dni", e.getMessage());
            return "perfil-edicion";
        }
    }
    private ClienteActualizacionDTO convertirClienteActualizacionDTO(ClienteModel cliente) {
        return ClienteActualizacionDTO.builder()
                .dni(cliente.getDni())
                .nombre(cliente.getNombre())
                .apellido(cliente.getApellido())
                .telefono(cliente.getTelefono())
                .direccion(cliente.getDireccion())
                .fechaNacimiento(cliente.getFechaNacimiento())
                .genero(cliente.getGenero())
                .idUsuarioCreador(cliente.getUsuario() != null ? cliente.getUsuario().getIdUsuario() : null)
                .build();
    }
}
