package com.sistemas_mangager_be.edu_virtual_ufps.oracle.repositories;

import com.sistemas_mangager_be.edu_virtual_ufps.oracle.entities.Id.MateriaOracleId;
import com.sistemas_mangager_be.edu_virtual_ufps.oracle.entities.MateriaOracle;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;
import java.util.Optional;


public interface MateriaOracleRepository extends JpaRepository<MateriaOracle, MateriaOracleId> {
}


