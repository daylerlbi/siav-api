package com.sistemas_mangager_be.edu_virtual_ufps.repositories;

import com.sistemas_mangager_be.edu_virtual_ufps.entities.TipoPrograma;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TipoProgramaRepository extends JpaRepository<TipoPrograma, Integer> {
    TipoPrograma findByMoodleId(String moodleId);

}
