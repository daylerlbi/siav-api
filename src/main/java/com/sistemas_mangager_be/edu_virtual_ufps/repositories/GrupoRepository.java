package com.sistemas_mangager_be.edu_virtual_ufps.repositories;

import com.sistemas_mangager_be.edu_virtual_ufps.entities.Grupo;
import com.sistemas_mangager_be.edu_virtual_ufps.entities.Materia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface GrupoRepository extends JpaRepository<Grupo, Integer> {

    List<Grupo> findByMateriaId(Materia materiaId);

    long countByMateriaId(Materia materiaId);

    List<Grupo> findByMateriaIdIn(List<Materia> materias);


}
