package com.integrador.gym.Service.Impl;

import com.integrador.gym.Dto.Actualizacion.UsuarioActualizacionDTO;
import com.integrador.gym.Dto.Creacion.UsuarioCreacionDTO;
import com.integrador.gym.Dto.UsuarioDTO;
import com.integrador.gym.Exception.DniYaRegistrado;
import com.integrador.gym.Exception.EmailYaRegistrado;
import com.integrador.gym.Exception.RolNoPermitido;
import com.integrador.gym.Exception.UsuarioNoEncontrado;
import com.integrador.gym.Factory.UsuarioFactory;
import com.integrador.gym.Model.ClienteModel;
import com.integrador.gym.Model.Enum.Roles;
import com.integrador.gym.Model.UsuarioModel;
import com.integrador.gym.Repository.ClienteRepository;
import com.integrador.gym.Repository.UsuarioRepository;
import com.integrador.gym.Service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;

@Service("usuarioServiceImpl")
@Transactional
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService, UserDetailsService {

    @Autowired
    private UsuarioFactory usuarioFactory;
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private final ClienteRepository clienteRepository; // Asumiendo que usas @RequiredArgsConstructor


    @Override
    public List<UsuarioDTO> listarTodos() {
        List<UsuarioModel> usuarios = usuarioRepository.findAll();

        return usuarios.stream()
                .map(this::toDTO)
                .collect(java.util.stream.Collectors.toList());
    }

    @Override
    public Optional<UsuarioModel> obtenerPorId(Long id) {
        return usuarioRepository.findById(id);
    }

    @Override
    public UsuarioDTO crear(UsuarioCreacionDTO dto) {
        validarEdadMinima(dto.getFechaNacimiento());
        validarUnicidad(dto.getEmail(), dto.getDni(), null); // ✅ Así evitas duplicar lógica

        if (dto.getRoles() != Roles.CLIENTE) {
            throw new RolNoPermitido("Solo se puede registrar el rol CLIENTE a través de este formulario.");
        }

        UsuarioModel usuario = usuarioFactory.crearDesdeDTO(dto);
        usuario.setPassword(passwordEncoder.encode(dto.getPassword()));
        UsuarioModel nuevoUsuario = usuarioRepository.save(usuario);

        ClienteModel nuevoCliente = ClienteModel.builder()
                .dni(nuevoUsuario.getDni())
                .nombre(nuevoUsuario.getNombre())
                .apellido(nuevoUsuario.getApellido())
                .telefono(dto.getTelefono())
                .direccion(dto.getDireccion())
                .fechaNacimiento(dto.getFechaNacimiento())
                .genero(dto.getGenero())
                .usuario(nuevoUsuario) // <--- ASOCIACIÓN CLAVE
                .build();

        clienteRepository.save(nuevoCliente);

        if (esEmpleado(usuario.getRoles())) {
            usuario.setFechaContratacion(LocalDate.now());
        }

        return toDTO(nuevoUsuario);
    }
    private UsuarioDTO toDTO(UsuarioModel usuario) {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setIdUsuario(usuario.getIdUsuario());
        dto.setDni(usuario.getDni());
        dto.setNombre(usuario.getNombre());
        dto.setApellido(usuario.getApellido());
        dto.setEmail(usuario.getEmail());
        dto.setTelefono(usuario.getTelefono());
        dto.setRoles(usuario.getRoles());
        dto.setFechaNacimiento(usuario.getFechaNacimiento());
        dto.setFechaContratacion(usuario.getFechaContratacion());
        dto.setEstado(usuario.getEstado());
        dto.setFechaCreacion(usuario.getFechaCreacion());

        return dto;
    }

    @Override
    public UsuarioDTO actualizar(Long id, UsuarioActualizacionDTO dto) {
        UsuarioModel existente = obtenerPorIdOrThrow(id);

        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            existente.setPassword(passwordEncoder.encode(dto.getPassword())); // 🔐 encriptar
        }

        // Regla de negocio: no se puede convertir CLIENT a empleado directamente
        if (existente.getRoles() == Roles.CLIENTE && esEmpleado(dto.getRoles())) {
            throw new RolNoPermitido(
                    "No se puede convertir un cliente en empleado directamente."
            );
        }
        validarEdadMinima(dto.getFechaNacimiento());
        validarUnicidad(dto.getEmail(), dto.getDni(), id);
        existente.setDni(dto.getDni());
        existente.setNombre(dto.getNombre());
        existente.setApellido(dto.getApellido());
        existente.setEmail(dto.getEmail());

        existente.setTelefono(dto.getTelefono());
        existente.setRoles(dto.getRoles());
        existente.setFechaNacimiento(dto.getFechaNacimiento());
        existente.setEstado(existente.getEstado()); // Mantener estado actual
        gestionarFechaContratacion(existente);

        UsuarioModel actualizado = usuarioRepository.save(existente);
        return toDTO(actualizado);
    }

    @Override
    public void eliminar(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new UsuarioNoEncontrado(id);
        }
        usuarioRepository.deleteById(id);
    }

    @Override
    public UsuarioActualizacionDTO obtenerDTOParaEdicion(Long id) {
        UsuarioModel usuario = obtenerPorIdOrThrow(id);

        UsuarioActualizacionDTO dto = new UsuarioActualizacionDTO();

        dto.setIdUsuario(usuario.getIdUsuario());
        dto.setDni(usuario.getDni());
        dto.setNombre(usuario.getNombre());
        dto.setApellido(usuario.getApellido());
        dto.setEmail(usuario.getEmail());
        dto.setTelefono(usuario.getTelefono());
        dto.setRoles(usuario.getRoles());
        dto.setFechaNacimiento(usuario.getFechaNacimiento());

        return dto;
    }

    private void validarEdadMinima(LocalDate fechaNacimiento) {
        int edad = Period.between(fechaNacimiento, LocalDate.now()).getYears();
        if (edad < 16) {
            throw new IllegalArgumentException("El usuario debe tener al menos 16 años.");
        }
    }

    private void validarUnicidad(String email, String dni, Long idExcluido) {
        if (idExcluido == null) {
            if (usuarioRepository.existsByEmail(email)) {
                throw new EmailYaRegistrado(email);
            }
            if (usuarioRepository.existsByDni(dni)) {
                throw new DniYaRegistrado(dni);
            }
        } else {
            if (usuarioRepository.existsByEmailAndIdUsuarioNot(email, idExcluido)) {
                throw new EmailYaRegistrado(email);
            }
            if (usuarioRepository.existsByDniAndIdUsuarioNot(dni, idExcluido)) {
                throw new DniYaRegistrado(dni);
            }
        }
    }

    private void gestionarFechaContratacion(UsuarioModel usuario) {
        if (esEmpleado(usuario.getRoles())) {
            if (usuario.getFechaContratacion() == null) {
                usuario.setFechaContratacion(LocalDate.now());
            }
        } else {
            usuario.setFechaContratacion(null); // Clientes no tienen fecha de contratación
        }
    }

    private boolean esEmpleado(Roles rol) {
        return rol == Roles.ADMIN || rol == Roles.RECEPCIONISTA;
    }

    private UsuarioModel obtenerPorIdOrThrow(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioNoEncontrado(id));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UsuarioModel usuario = usuarioRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con email: " + username));

        if (usuario.getEstado() != null && usuario.getEstado().name().equalsIgnoreCase("INACTIVO")) {
            throw new UsernameNotFoundException("Usuario inactivo: " + username);
        }

        return User.builder()
                .username(usuario.getEmail())
                .password(usuario.getPassword())
                .roles(usuario.getRoles().name()) // Enum → String
                .build();
    }

    /*
    @Override
    public boolean existePorEmail(String email) {
        return usuarioRepository.existsByEmail(email);
    }

    @Override
    public boolean existePorDni(String dni) {
        return usuarioRepository.existsByDni(dni);
    }

    private void validarUnicidad(String email, String dni, Long idExcluido) {
        if (existePorEmail(email)) {
            if (idExcluido == null ||
                    !usuarioRepository.findById(idExcluido).map(u -> u.getEmail().equals(email)).orElse(false)) {
                throw new RuntimeException("El email ya está registrado: " + email);
            }
        }

        if (existePorDni(dni)) {
            if (idExcluido == null ||
                    !usuarioRepository.findById(idExcluido).map(u -> u.getDni().equals(dni)).orElse(false)) {
                throw new RuntimeException("El DNI ya está registrado: " + dni);
            }
        }
    }

     */
}
