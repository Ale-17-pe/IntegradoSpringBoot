package com.integrador.gym.Repository;

import com.integrador.gym.Model.ClienteModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<ClienteModel, Long> {
    boolean existsByDni(String dni);
    Optional<ClienteModel> findByDni(String dni);
}
