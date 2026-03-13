package com.sistemas_mangager_be.edu_virtual_ufps.repositories;

import com.sistemas_mangager_be.edu_virtual_ufps.entities.CambioEstadoMatricula;
import com.sistemas_mangager_be.edu_virtual_ufps.entities.Estudiante;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CambioEstadoMatriculaRepository extends JpaRepository<CambioEstadoMatricula, Long> {

    List<CambioEstadoMatricula> findByMatriculaId_EstudianteIdAndSemestre(Estudiante estudianteId, String semestre);

}
