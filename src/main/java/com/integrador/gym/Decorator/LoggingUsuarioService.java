package com.integrador.gym.Decorator;

import com.integrador.gym.Dto.Actualizacion.UsuarioActualizacionDTO;
import com.integrador.gym.Dto.Creacion.UsuarioCreacionDTO;
import com.integrador.gym.Dto.UsuarioDTO;
import com.integrador.gym.Model.UsuarioModel;
import com.integrador.gym.Service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Primary  @Service
public class LoggingUsuarioService implements UsuarioService {

    @Autowired
    private UsuarioService delegate;
    // Servicio real
    public LoggingUsuarioService(@Qualifier("usuarioServiceImpl") UsuarioService delegate) {
        this.delegate = delegate;
    }
    @Override
    public List<UsuarioDTO> listarTodos() {
        System.out.println("[LOG]: Listando todos los usuarios...");
        List<UsuarioDTO> usuarios = delegate.listarTodos();
        System.out.println("[LOG] Total encontrados: " + usuarios.size());
        return usuarios;
    }

    @Override
    public Optional<UsuarioModel> obtenerPorId(Long id) {
        System.out.println("LOG: Obteniendo usuario con ID " + id);
        Optional<UsuarioModel> usuario = delegate.obtenerPorId(id);
        System.out.println(usuario.isPresent()
                ? "[LOG] Usuario encontrado: " + usuario.get().getNombre()
                : "[LOG] Usuario no encontrado con ID " + id);
        return usuario;
    }

    @Override
    public UsuarioDTO crear(UsuarioCreacionDTO dto) {
        System.out.println("Creando nuevo usuario: " + dto.getNombre());
        UsuarioDTO nuevo = delegate.crear(dto);
        System.out.println("[LOG] Usuario creado con ID: " + nuevo.getIdUsuario());
        return nuevo;
    }

    @Override
    public UsuarioDTO actualizar(Long id, UsuarioActualizacionDTO dto) {
        System.out.println("LOG: Actualizando usuario con ID " + id);
        UsuarioDTO actualizado = delegate.actualizar(id, dto);
        System.out.println("[LOG] Usuario actualizado correctamente.");
        return actualizado;
    }

    @Override
    public void eliminar(Long id) {
        System.out.println("LOG: Eliminando usuario con ID " + id);
        delegate.eliminar(id);
        System.out.println("[LOG] Usuario eliminado correctamente.");
    }

    @Override
    public UsuarioActualizacionDTO obtenerDTOParaEdicion(Long id) {
        return null;
    }
}
