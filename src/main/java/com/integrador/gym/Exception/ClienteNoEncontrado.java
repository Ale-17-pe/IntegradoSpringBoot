package com.integrador.gym.Exception;

public class ClienteNoEncontrado extends RuntimeException {
    public ClienteNoEncontrado(String dni) {
        super("Cliente no encontrado con DNI: " + dni);
    }
}
