package com.integrador.gym.Controller.Config;

import com.integrador.gym.Model.Enum.EstadoUsuario;
import com.integrador.gym.Model.Enum.Roles;
import com.integrador.gym.Model.UsuarioModel;
import com.integrador.gym.Repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@Component
public class DataInitializer  implements CommandLineRunner {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Override
    @Transactional
    public void run(String... args) throws Exception {
        // 1. Datos del Administrador
        String adminDni = "12345679";
        String adminPasswordPlana = "Admin123!";

        // 2. Comprobar si ya existe
        Optional<UsuarioModel> opt = usuarioRepository.findByDni(adminDni);
        if (opt.isPresent()) {
            System.out.println("✅ Administrador ya existe en la base de datos: DNI " + adminDni);
            return;
        }

        // 3. Crear el Administrador
        UsuarioModel admin = UsuarioModel.builder()
                .nombre("Admin")
                .apellido("Sistema")
                .dni(adminDni)
                .email("admin@aresgym.com")
                .telefono("999999999")
                .fechaNacimiento(LocalDate.of(1990, 1, 1))
                // 🔥 Hashing seguro en tiempo de ejecución
                .password(passwordEncoder.encode(adminPasswordPlana))
                .estado(EstadoUsuario.ACTIVO)
                .roles(Roles.ADMIN) // Asume que tu Enum se llama Roles
                .build();

        usuarioRepository.save(admin);
        usuarioRepository.flush(); // 👈 fuerza el guardado inmediato en la BD
        System.out.println("Guardado en la BD ✅: " + admin.getEmail());


        System.out.println("🎉 Usuario administrador creado exitosamente:");
        System.out.println("   DNI: " + adminDni);
        System.out.println("   Contraseña (Plana): " + adminPasswordPlana);
        System.out.println("   *El hash de 60 caracteres ya está guardado en la BD.");
    }

}

