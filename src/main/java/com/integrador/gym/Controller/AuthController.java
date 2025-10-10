package com.integrador.gym.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {

    @GetMapping("/login")
    public String mostrarLogin() {
        return "login";
    }



    @GetMapping("/registro")
    public String mostrarRegistro() {
        return "registro";
    }

    @GetMapping("/perfil")
    public String mostrarPerfil() {
        return "perfil";
    }

    @GetMapping("/")
    public String mostrarInicio() {
        return "index";
    }
}
