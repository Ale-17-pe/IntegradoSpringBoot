package com.integrador.gym.Service.Impl;

import com.integrador.gym.Dto.Actualizacion.ClienteActualizacionDTO;
import com.integrador.gym.Dto.Creacion.ClienteCreacionDTO;
import com.integrador.gym.Dto.ClienteDTO;
import com.integrador.gym.Dto.UsuarioDTO;
import com.integrador.gym.Exception.ClienteDniDuplicado;
import com.integrador.gym.Exception.ClienteNoEncontrado;
import com.integrador.gym.Exception.UsuarioNoAutorizado;
import com.integrador.gym.Factory.ClienteFactory;
import com.integrador.gym.Model.ClienteModel;
import com.integrador.gym.Model.Enum.EstadoUsuario;
import com.integrador.gym.Model.Enum.Roles;
import com.integrador.gym.Model.UsuarioModel;
import com.integrador.gym.Repository.ClienteRepository;
import com.integrador.gym.Repository.UsuarioRepository;
import com.integrador.gym.Service.ClienteService;
import com.integrador.gym.Service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ClienteServiceImpl implements ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    @Qualifier("usuarioServiceImpl")
    private UsuarioService usuarioService;

    @Autowired
    private ClienteFactory clienteFactory;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public List<ClienteModel> listarTodos() {
        return clienteRepository.findAll();
    }

    @Override
    public Optional<ClienteModel> obtenerPorId(Long id) {
        return clienteRepository.findById(id);
    }

    @Override
    public ClienteDTO crear(ClienteCreacionDTO dto) {
        validarEdadMinima(dto.getFechaNacimiento());
        if (clienteRepository.existsByDni(dto.getDni())) {
            throw new ClienteDniDuplicado(dto.getDni());
        }

        // 1. Crear usuario
        UsuarioModel usuario = new UsuarioModel();
        usuario.setDni(dto.getDni());
        usuario.setNombre(dto.getNombre());
        usuario.setApellido(dto.getApellido());
        usuario.setEmail(dto.getEmail());
        usuario.setPassword(dto.getPassword());
        usuario.setTelefono(dto.getTelefono());
        usuario.setRoles(Roles.CLIENTE);
        usuario.setFechaNacimiento(dto.getFechaNacimiento());
        usuario.setEstado(EstadoUsuario.ACTIVO);

        // 2. Guardar usuario
        UsuarioModel guardadoUsuario = usuarioRepository.save(usuario);

        // 3. Crear cliente
        ClienteModel cliente = clienteFactory.crearDesdeDTO(dto, guardadoUsuario);
        ClienteModel guardadoCliente = clienteRepository.save(cliente);

        return toDTO(guardadoCliente, guardadoUsuario);
    }

    @Override
    public ClienteDTO actualizar(Long id, ClienteActualizacionDTO dto) {
        ClienteModel existente = obtenerPorIdOrThrow(id);

        validarEdadMinima(dto.getFechaNacimiento());
        validarUnicidad(dto.getDni(), id);

        UsuarioModel usuarioCreador = usuarioService.obtenerPorId(dto.getIdUsuarioCreador())
                .orElseThrow(() -> new UsuarioNoAutorizado("Usuario creador no válido."));

        existente.setDni(dto.getDni());
        existente.setNombre(dto.getNombre());
        existente.setApellido(dto.getApellido());
        existente.setTelefono(dto.getTelefono());
        existente.setDireccion(dto.getDireccion());
        existente.setFechaNacimiento(dto.getFechaNacimiento());
        existente.setGenero(dto.getGenero());
        existente.setUsuario(usuarioCreador);

        ClienteModel guardado = clienteRepository.save(existente);
        return toDTO(guardado, usuarioCreador);
    }

    @Override
    public void eliminar(Long id) {
        if (!clienteRepository.existsById(id)) {
            throw new ClienteNoEncontrado(id);
        }
        clienteRepository.deleteById(id);
    }

    @Override
    public boolean existePorDni(String dni) {
        return clienteRepository.existsByDni(dni);
    }

    private void validarEdadMinima(LocalDate fechaNacimiento) {
        int edad = Period.between(fechaNacimiento, LocalDate.now()).getYears();
        if (edad < 16) {
            throw new IllegalArgumentException("El cliente debe tener al menos 16 años.");
        }
    }
    private UsuarioModel validarUsuarioCreador(Long idUsuario) {
        Optional<UsuarioModel> usuarioOpt = usuarioService.obtenerPorId(idUsuario);
        if (usuarioOpt.isEmpty() || !usuarioOpt.get().isActivo()) {
            throw new UsuarioNoAutorizado("El usuario creador no existe o está inactivo.");
        }

        UsuarioModel usuario = usuarioOpt.get();
        if (usuario.getRoles() != Roles.ADMIN && usuario.getRoles() != Roles.RECEPCIONISTA) {
            throw new UsuarioNoAutorizado("Solo los roles ADMIN o RECEPCIONISTA pueden registrar clientes.");
        }
        return usuario;
    }

    private ClienteModel obtenerPorIdOrThrow(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new ClienteNoEncontrado(id));
    }

    private void validarUnicidad(String dni, Long idCliente) {
        Optional<ClienteModel> existente = clienteRepository.findByDni(dni);
        if (existente.isPresent() && !existente.get().getIdCliente().equals(idCliente)) {
            throw new ClienteDniDuplicado(dni);
        }
    }

    private ClienteDTO toDTO(ClienteModel cliente, UsuarioModel usuarioCreador) {
        ClienteDTO dto = new ClienteDTO();
        dto.setIdCliente(cliente.getIdCliente());
        dto.setDni(cliente.getDni());
        dto.setNombre(cliente.getNombre());
        dto.setApellido(cliente.getApellido());
        dto.setTelefono(cliente.getTelefono());
        dto.setDireccion(cliente.getDireccion());
        dto.setFechaNacimiento(cliente.getFechaNacimiento());
        dto.setGenero(cliente.getGenero());
        dto.setIdUsuarioCreador(usuarioCreador.getIdUsuario());
        dto.setNombreUsuarioCreador(usuarioCreador.getNombre() + " " + usuarioCreador.getApellido());
        return dto;
    }
}
