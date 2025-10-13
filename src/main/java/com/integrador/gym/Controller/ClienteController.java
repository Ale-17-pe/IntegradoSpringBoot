    package com.integrador.gym.Controller;

    import com.integrador.gym.Dto.Actualizacion.ClienteActualizacionDTO;
    import com.integrador.gym.Dto.Creacion.ClienteCreacionDTO;
    import com.integrador.gym.Dto.ClienteDTO;
    import com.integrador.gym.Dto.RegistrarClienteDTO;
    import com.integrador.gym.Exception.ClienteDniDuplicado;
    import com.integrador.gym.Exception.ClienteNoEncontrado;
    import com.integrador.gym.Exception.UsuarioNoAutorizado;
    import com.integrador.gym.Model.ClienteModel;
    import com.integrador.gym.Service.ClienteService;
    import com.integrador.gym.Service.Impl.RegistrarNuevoClienteImpl;
    import jakarta.validation.Valid;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.*;

    import java.util.HashMap;
    import java.util.List;
    import java.util.Map;

    @RestController
    @RequestMapping("/api/clientes")
    public class ClienteController {

        @Autowired
        private ClienteService clienteService;

        @Autowired
        private RegistrarNuevoClienteImpl registrarNuevoCliente;

        @GetMapping
        public List<ClienteModel> listar() {
            return clienteService.listarTodos();
        }

        @GetMapping("/{id}")
        public ResponseEntity<ClienteModel> obtener(@PathVariable Long id) {
            return clienteService.obtenerPorId(id)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        }

        @PostMapping
        public ResponseEntity<?> crear(@Valid @RequestBody ClienteCreacionDTO dto) {
            try {
                ClienteDTO nuevo = clienteService.crear(dto);
                return ResponseEntity.ok(nuevo);
            } catch (ClienteNoEncontrado | UsuarioNoAutorizado e) {
                return ResponseEntity.badRequest().body(errorResponse(e.getMessage()));
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().body(errorResponse("Validación: " + e.getMessage()));
            } catch (Exception e) {
                return ResponseEntity.internalServerError().body(errorResponse("Error interno: " + e.getMessage()));
            }
        }
        @PostMapping("/registro")
        public ResponseEntity<?> registrar(@Valid @RequestBody RegistrarClienteDTO dto) {
            try {
                registrarNuevoCliente.ejecutar(dto);
                return ResponseEntity.ok(Map.of("mensaje", "Cliente registrado exitosamente"));
            } catch (Exception e) {
                return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
            }
        }




        private Map<String, String> errorResponse(String mensaje) {
            Map<String, String> response = new HashMap<>();
            response.put("error", mensaje);
            return response;
        }
    }
