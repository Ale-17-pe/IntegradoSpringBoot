package com.integrador.gym.Service.Impl;

import com.integrador.gym.Dto.*;
import com.integrador.gym.Dto.Creacion.ClienteCreacionDTO;
import com.integrador.gym.Dto.Creacion.MembresiaCreacionDTO;
import com.integrador.gym.Dto.Creacion.PagoCreacionDTO;
import com.integrador.gym.Dto.Creacion.UsuarioCreacionDTO;
import com.integrador.gym.Model.ClienteModel;
import com.integrador.gym.Model.Enum.MetodoPago;
import com.integrador.gym.Model.Enum.Roles;
import com.integrador.gym.Model.MembresiaModel;
import com.integrador.gym.Model.PagoModel;
import com.integrador.gym.Model.UsuarioModel;
import com.integrador.gym.Service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
public class RegistrarNuevoClienteImpl implements RegistrarNuevoCliente {
    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private MembresiaService membresiaService;

    @Autowired
    private PagoService pagoService;


    @Override
    @Transactional
    public void ejecutar(RegistrarClienteDTO dto) {
        // 1. Crear usuario (cliente)
        UsuarioCreacionDTO usuarioDto = new UsuarioCreacionDTO(
                dto.getDni(),
                dto.getNombre(),
                dto.getApellido(),
                dto.getEmail(),
                dto.getPassword(),
                dto.getTelefono(),
                Roles.CLIENTE,
                dto.getFechaNacimiento()
        );

        UsuarioDTO usuario = usuarioService.crear(usuarioDto);

        // 2. Crear cliente
        ClienteCreacionDTO clienteDto = new ClienteCreacionDTO();
        clienteDto.setDni(dto.getDni());
        clienteDto.setNombre(dto.getNombre());
        clienteDto.setApellido(dto.getApellido());
        clienteDto.setTelefono(dto.getTelefono());
        clienteDto.setFechaNacimiento(dto.getFechaNacimiento());
        clienteDto.setIdUsuarioCreador(dto.getIdUsuarioCreador());

        ClienteDTO cliente = clienteService.crear(clienteDto);

        // 3. Crear membresía
        MembresiaCreacionDTO membresiaDto = new MembresiaCreacionDTO();
        membresiaDto.setIdCliente(cliente.getIdCliente());
        membresiaDto.setIdPlan(dto.getIdPlan()); // ¡Añade idPlan!
        membresiaDto.setFechaInicio(LocalDate.now());

        MembresiaDto membresia = membresiaService.crear(membresiaDto);

        // 4. Crear pago
        PagoCreacionDTO pagoDto = new PagoCreacionDTO();
        pagoDto.setIdMembresia(membresia.getIdMembresia());
        pagoDto.setMonto(dto.getMonto());
        pagoDto.setMetodoPago(MetodoPago.valueOf(dto.getMetodoPago()));
        pagoDto.setFechaPago(LocalDate.now().atStartOfDay());

        PagoDTO pago = pagoService.crear(pagoDto);
    }


}
