package com.sistemas_mangager_be.edu_virtual_ufps.oracle.repositories;

import com.sistemas_mangager_be.edu_virtual_ufps.oracle.entities.Id.MateriasMatriculadasId;
import com.sistemas_mangager_be.edu_virtual_ufps.oracle.entities.MateriasMatriculadasOracle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface MateriasMatriculadasOracleRepository
        extends JpaRepository<MateriasMatriculadasOracle, MateriasMatriculadasId> {
}


