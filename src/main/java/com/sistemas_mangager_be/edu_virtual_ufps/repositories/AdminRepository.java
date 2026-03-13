package com.sistemas_mangager_be.edu_virtual_ufps.repositories;

import com.sistemas_mangager_be.edu_virtual_ufps.entities.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, Integer> {
    // Metodo para buscar un admin mediante su correo
    Optional<Admin> findByEmail(String email);

    // Metodo para verificar si el admin existe en la base de datos
    Boolean existsByEmail(String email);
}
