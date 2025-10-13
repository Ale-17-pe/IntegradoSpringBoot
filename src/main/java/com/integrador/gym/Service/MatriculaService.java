package com.integrador.gym.Service;

import com.integrador.gym.Dto.CompraPlanDTO;
import com.integrador.gym.Dto.MembresiaDto;

public interface MatriculaService {
    MembresiaDto matricularCliente(CompraPlanDTO compraDto);

}
