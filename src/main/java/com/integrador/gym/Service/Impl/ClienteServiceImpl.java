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
    public ClienteDTO crear(com.integrador.gym.Dto.Creacion.ClienteCreacionDTO dto) {
        throw new UnsupportedOperationException("La creación de clientes debe hacerse a través del registro de usuario.");
    }

    @Override
    @Transactional
    public ClienteDTO actualizar(String dni, ClienteActualizacionDTO dto) {
        UsuarioModel usuarioAsociado = usuarioRepository.findByDni(dni)
                .orElseThrow(() -> new UsuarioNoAutorizado("Usuario no encontrado con DNI: " + dni));

        ClienteModel clienteExistente = clienteRepository.findByDni(dni)
                .orElseThrow(() -> new ClienteNoEncontrado(dni));
        if (!dni.equals(dto.getDni())) {
            validarUnicidadDni(dto.getDni(), usuarioAsociado.getIdUsuario());
        }
        validarEdadMinima(dto.getFechaNacimiento());
        if (!usuarioAsociado.getIdUsuario().equals(dto.getIdUsuarioCreador())) {
            throw new UsuarioNoAutorizado("No tienes permiso para cambiar el ID de usuario asociado.");
        }

        clienteExistente.setDni(dto.getDni());
        clienteExistente.setNombre(dto.getNombre());
        clienteExistente.setApellido(dto.getApellido());
        clienteExistente.setTelefono(dto.getTelefono());
        clienteExistente.setDireccion(dto.getDireccion());
        clienteExistente.setFechaNacimiento(dto.getFechaNacimiento());
        clienteExistente.setGenero(dto.getGenero());

        usuarioAsociado.setDni(dto.getDni());
        usuarioAsociado.setNombre(dto.getNombre());
        usuarioAsociado.setApellido(dto.getApellido());
        usuarioAsociado.setTelefono(dto.getTelefono());
        usuarioAsociado.setFechaNacimiento(dto.getFechaNacimiento());

        usuarioRepository.save(usuarioAsociado);
        ClienteModel guardado = clienteRepository.save(clienteExistente);
        return toDTO(guardado, usuarioAsociado);
    }


@Override
    public void eliminar(String dni) {
        ClienteModel cliente = obtenerPorIdOrThrow(dni);
        UsuarioModel usuarioAsociado = cliente.getUsuario();
        if (usuarioAsociado != null) {
            usuarioAsociado.setEstado(EstadoUsuario.INACTIVO);
            usuarioRepository.save(usuarioAsociado);
        }
        clienteRepository.delete(cliente);
    }

    @Override
    public boolean existePorDni(String dni) {
        return clienteRepository.existsByDni(dni);
    }

    @Override
    @Transactional(readOnly = true) // 💡 Marcado como solo lectura
    public Optional<ClienteModel> obtenerClientePorDni(String dni) {

        // Asumimos que el UsuarioModel tiene una relación @OneToOne con ClienteModel.

        return clienteRepository.findByDni(dni);
    }

    private void validarEdadMinima(LocalDate fechaNacimiento) {
        int edad = Period.between(fechaNacimiento, LocalDate.now()).getYears();
        if (edad < 16) {
            throw new IllegalArgumentException("El cliente debe tener al menos 16 años.");
        }
    }
    private void validarUnicidadDni(String dni, Long idUsuarioExcluido) {
        // Usamos el repositorio de Usuario porque garantiza la unicidad para la cuenta.
        if (usuarioRepository.existsByDniAndIdUsuarioNot(dni, idUsuarioExcluido)) {
            throw new ClienteDniDuplicado(dni);
        }
    }

    private ClienteModel obtenerPorIdOrThrow(String dni) {
        return clienteRepository.findByDni(dni)
                .orElseThrow(() -> new ClienteNoEncontrado(dni));
    }

    private void validarUnicidad(String dni, Long idCliente) {
        Optional<ClienteModel> existente = clienteRepository.findByDni(dni);
        if (existente.isPresent() && !existente.get().getIdCliente().equals(idCliente)) {
            throw new ClienteDniDuplicado(dni);
        }
    }

    private ClienteDTO toDTO(ClienteModel cliente, UsuarioModel usuarioAsociado) {
        ClienteDTO dto = new ClienteDTO();
        dto.setIdCliente(cliente.getIdCliente());
        dto.setDni(cliente.getDni());
        dto.setNombre(cliente.getNombre());
        dto.setApellido(cliente.getApellido());
        dto.setTelefono(cliente.getTelefono());
        dto.setDireccion(cliente.getDireccion());
        dto.setFechaNacimiento(cliente.getFechaNacimiento());
        dto.setGenero(cliente.getGenero());
        dto.setIdUsuarioCreador(usuarioAsociado.getIdUsuario());
        dto.setNombreUsuarioCreador(usuarioAsociado.getNombre() + " " + usuarioAsociado.getApellido());
        return dto;

    }
}
