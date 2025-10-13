    package com.integrador.gym.Controller;

    import com.integrador.gym.Model.ClienteModel;
    import com.integrador.gym.Service.ClienteService;
    import com.integrador.gym.Service.Impl.RegistrarNuevoClienteImpl;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.stereotype.Controller;
    import org.springframework.ui.Model;
    import org.springframework.web.bind.annotation.GetMapping;
    import org.springframework.security.core.Authentication;
    import org.springframework.web.bind.annotation.RequestParam;

    @Controller
    public class AuthController {
        @Autowired
        private RegistrarNuevoClienteImpl registrarNuevoCliente;

        @Autowired
        private ClienteService clienteService;


        @GetMapping("/login")
        public String loginPage(@RequestParam(required = false) String error,
                                @RequestParam(required = false) String logout,
                                Model model)  {
            if (error != null) {
                model.addAttribute("error", "DNI o contraseña incorrectos. Inténtalo nuevamente.");
            }
            if (logout != null) {
                model.addAttribute("mensaje", "Sesión cerrada exitosamente.");
            }
            return "login";
        }

        @GetMapping("/dashboard")
        public String mostrarDashboard(Authentication authentication, Model model) {

            // 1. Obtener la identidad (DNI) y el rol de la sesión de Spring Security
            String dniUsuario = authentication.getName();
            String rol = authentication.getAuthorities().iterator().next().getAuthority().replace("ROLE_", "");

            // 2. Intentar obtener el nombre completo del cliente (para el saludo personalizado)
            ClienteModel cliente = clienteService.obtenerClientePorDni(dniUsuario).orElse(null);

            String nombreUsuario;
            if (cliente != null) {
                nombreUsuario = cliente.getNombre();
            } else {
                // Fallback si no hay registro de cliente, usamos el DNI o un nombre genérico.
                nombreUsuario = "Usuario";
            }

            // 3. Pasar los datos esenciales a la vista
            model.addAttribute("nombre", nombreUsuario);
            model.addAttribute("rol", rol);

            // Dependiendo del rol, puedes redirigir a vistas diferentes.
            if (rol.equals("ADMIN")) {
                return "/admin/dashboard-admin";
            } else if (rol.equals("ENTRENADOR")) {
                return "dashboard-entrenador";
            } else {
                // El dashboard por defecto es el del CLIENTE
                return "dashboard";
            }
        }


        @GetMapping("/")
        public String mostrarInicio() {
            return "index";
        }


        @GetMapping("/ubicacion")
        public String mostrarInicioUbicacion() {
            return "ubicacion";
        }
    }
