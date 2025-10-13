package com.integrador.gym.Controller;

import com.integrador.gym.Dto.CompraPlanDTO;
import com.integrador.gym.Dto.MembresiaDto;
import com.integrador.gym.Service.MatriculaService;
import jakarta.validation.Valid;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/matriculas")
public class MatriculaController {

    @Autowired
    private MatriculaService matriculaService;

    /**
     * Endpoint para procesar la compra de un plan por parte de un cliente existente.
     * * @param compraDto DTO con el ID del cliente, ID del plan y detalles de pago.
     * @return ResponseEntity con los detalles de la Membresia recién creada.
     */
    @PostMapping("/comprar-plan")
    public ResponseEntity<MembresiaDto> comprarPlan(@Valid @RequestBody CompraPlanDTO compraDto) {

        // Asumiendo que el ID del cliente (idCliente) viene en el CompraPlanDTO,
        // podrías obtener el ID del usuario autenticado desde el contexto de seguridad
        // y asignarlo a 'idUsuarioGestor' si fuera necesario para auditoría.

        // Lógica de negocio principal: delegar la orquestación al servicio.
        MembresiaDto membresiaCreada = matriculaService.matricularCliente(compraDto);

        // Devolver una respuesta 201 Created con el DTO de la membresía.
        return new ResponseEntity<>(membresiaCreada, HttpStatus.CREATED);
    }

    // Aquí podrías añadir otros endpoints relacionados con la matrícula,
    // como consultar planes disponibles, etc.
}
