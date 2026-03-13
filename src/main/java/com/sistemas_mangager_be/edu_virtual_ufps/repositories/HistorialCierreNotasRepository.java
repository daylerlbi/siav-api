package com.sistemas_mangager_be.edu_virtual_ufps.repositories;

import com.sistemas_mangager_be.edu_virtual_ufps.entities.HistorialCierreNotas;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HistorialCierreNotasRepository extends JpaRepository<HistorialCierreNotas, Long> {
    List<HistorialCierreNotas> findFirstByGrupoCohorteIdOrderByFechaCierreDesc(Long grupoCohorteId);

    void deleteByGrupoCohorteId(Long grupoCohorteId);

    List<HistorialCierreNotas> findByGrupoCohorteId(Long grupoCohorteId);
}