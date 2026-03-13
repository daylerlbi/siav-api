package com.sistemas_mangager_be.edu_virtual_ufps.repositories;

import com.sistemas_mangager_be.edu_virtual_ufps.entities.Pensum;
import com.sistemas_mangager_be.edu_virtual_ufps.entities.Programa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PensumRepository extends JpaRepository<Pensum, Integer> {

    List<Pensum> findByProgramaId(Programa programaId);

}
