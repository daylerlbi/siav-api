package com.sistemas_mangager_be.edu_virtual_ufps.repositories;

import com.sistemas_mangager_be.edu_virtual_ufps.entities.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface EstudianteRepository extends JpaRepository<Estudiante, Integer> {

    List<Estudiante> findByPensumId(Pensum pensumId);

    List<Estudiante> findByCohorteId(CohorteGrupo cohorteId);

    List<Estudiante> findByProgramaId(Programa programaId);

    List<Estudiante> findByEstadoEstudianteId(EstadoEstudiante estadoEstudianteId);

    List<Estudiante> findByEstadoEstudianteIdAndProgramaId_EsPosgradoTrue(EstadoEstudiante estadoEstudianteId);

    List<Estudiante> findByEstadoEstudianteId_IdAndCohorteId(Integer estadoEstudianteId, CohorteGrupo cohorteId);

    Optional<Estudiante> findByCodigo(String codigo);

    boolean existsByEmail(String email);

    boolean existsByCodigo(String codigo);

    boolean existsByMoodleId(String moodleId);

    boolean existsByCedula(String cedula);

}
