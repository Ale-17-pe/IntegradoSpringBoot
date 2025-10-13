package com.integrador.gym.Controller;

import org.springframework.web.bind.annotation.GetMapping;

public class PaginasController {
    @GetMapping("/login")
    public String loginPage() { return "login"; }

    @GetMapping("/registro")
    public String mostrarRegistro() { return "registro"; }

    @GetMapping("/perfil")
    public String mostrarPerfil() { return "perfil"; }

    @GetMapping("/dashboard")
    public String dashboard() { return "dashboard"; }

    @GetMapping({"/", "/index"})
    public String inicio() { return "index"; }

    @GetMapping("/ubicacion")
    public String ubicacion() { return "ubicacion"; }

    @GetMapping("/ejercicios")
    public String ejercicios() { return "ejercicios"; }

    @GetMapping("/membresias")
    public String membresias() { return "membresias"; }

    @GetMapping("/mutar")
    public String planes() { return "planes"; }
}
