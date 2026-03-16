package com.sistemas_mangager_be.edu_virtual_ufps.oracle.repositories;

import com.sistemas_mangager_be.edu_virtual_ufps.oracle.entities.EstudianteOracle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface EstudianteOracleRepository extends JpaRepository<EstudianteOracle, String> {

}