package com.sistemas_mangager_be.edu_virtual_ufps.services.interfaces.divisist;

import com.sistemas_mangager_be.edu_virtual_ufps.exceptions.PensumNotFoundException;
import com.sistemas_mangager_be.edu_virtual_ufps.shared.DTOs.divisist.*;
import com.sistemas_mangager_be.edu_virtual_ufps.shared.responses.divisist.NotasDivisistResponse;

import java.util.List;

public interface IDivisistService {
    EstudianteDivisistDTO crearoActualizarEstudiante(EstudianteDivisistDTO estudianteDivisistDTO);

    ProfesorDivisistDTO crearOActualizarProfesor(ProfesorDivisistDTO profesorDivisistDTO);

    MateriaDivisistDTO crearOActualizarMateria(MateriaDivisistDTO materiaDivisistDTO)
            throws PensumNotFoundException;

    GrupoDivisistDTO crearOActualizarGrupo(GrupoDivisistDTO grupoDivisistDTO);

    MateriaMatriculadaDivisistDTO crearOActualizarMateriaMatriculada(
            MateriaMatriculadaDivisistDTO materiaMatriculadaDivisistDTO);

    NotaDivisistDTO crearOActualizarNota(NotaDivisistDTO notaDivisistDTO);

    NotaDivisistDTO actualizarNotaParcial(ActualizarNotaDivisistRequest request);

    // Método para buscar materia por código de carrera y código de materia
    MateriaDivisistDTO buscarMateriaPorCarreraYCodigo(String codCarrera, String codMateria);

    // Método para buscar grupo por código de carrera, código de materia y grupo
    GrupoDivisistDTO buscarGrupoPorCarreraMateriaYGrupo(String codCarrera, String codMateria, String grupo);

    // Método para buscar profesor por código
    ProfesorDivisistDTO buscarProfesorPorCodigo(String codProfesor);

    // Método para buscar estudiante por código
    EstudianteDivisistDTO buscarEstudiantePorCodigo(String codigo);

    NotaDivisistDTO buscarNotaPorAlumnoCarreraMateriaGrupo(String codAlumno, String codCarrera, String codMateria,
                                                           String grupo);

    List<NotasDivisistResponse> listarNotasPorEstudianteSemestreActual(String codAlumno);

    List<NotasDivisistResponse> listarNotasPorGrupoSemestreActual(String codCarrera, String codMateria,
                                                                  String grupo);

    List<NotasDivisistResponse> listarNotasPorSemestreActual();
}
