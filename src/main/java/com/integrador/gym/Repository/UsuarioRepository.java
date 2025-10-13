package com.integrador.gym.Repository;

import com.integrador.gym.Model.UsuarioModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<UsuarioModel, Long> {
    boolean existsByDni(String dni);
    boolean existsByEmail(String email);
    boolean existsByEmailAndIdUsuarioNot(String email, Long id);
    boolean existsByDniAndIdUsuarioNot(String dni, Long id);
    Optional<UsuarioModel> findByDni(String dni);
    Optional<UsuarioModel> findByEmail(String email);
}
