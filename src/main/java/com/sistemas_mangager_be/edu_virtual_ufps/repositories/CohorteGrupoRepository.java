package com.sistemas_mangager_be.edu_virtual_ufps.repositories;

import com.sistemas_mangager_be.edu_virtual_ufps.entities.Cohorte;
import com.sistemas_mangager_be.edu_virtual_ufps.entities.CohorteGrupo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CohorteGrupoRepository extends JpaRepository<CohorteGrupo, Integer> {

    List<CohorteGrupo> findAllByCohorteId(Cohorte cohorte);

    void deleteAll(Iterable<? extends CohorteGrupo> entities);

    List<CohorteGrupo> findByCohorteId(Cohorte cohorteId);
}
