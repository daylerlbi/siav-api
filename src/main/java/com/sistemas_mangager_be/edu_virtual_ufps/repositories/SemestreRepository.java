package com.sistemas_mangager_be.edu_virtual_ufps.repositories;

import com.sistemas_mangager_be.edu_virtual_ufps.entities.Semestre;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface SemestreRepository extends JpaRepository<Semestre, Integer> {

    Optional<Semestre> findByNumero(Integer numero);
}
