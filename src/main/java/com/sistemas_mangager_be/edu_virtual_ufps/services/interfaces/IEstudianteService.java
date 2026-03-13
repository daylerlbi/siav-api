package com.sistemas_mangager_be.edu_virtual_ufps.services.interfaces;

import com.sistemas_mangager_be.edu_virtual_ufps.exceptions.*;
import com.sistemas_mangager_be.edu_virtual_ufps.shared.DTOs.EstudianteDTO;
import com.sistemas_mangager_be.edu_virtual_ufps.shared.requests.MoodleRequest;
import com.sistemas_mangager_be.edu_virtual_ufps.shared.responses.EstudianteResponse;

import java.util.List;

public interface IEstudianteService {

    EstudianteDTO crearEstudiante(EstudianteDTO estudianteDTO)
            throws PensumNotFoundException, CohorteNotFoundException, EstadoEstudianteNotFoundException,
            RoleNotFoundException, UserExistException;

    void vincularMoodleId(MoodleRequest moodleRequest) throws EstudianteNotFoundException, UserExistException;

    EstudianteDTO actualizarEstudiante(Integer id, EstudianteDTO estudianteDTO)
            throws UserNotFoundException, PensumNotFoundException, CohorteNotFoundException,
            EstadoEstudianteNotFoundException, EstudianteNotFoundException, EmailExistException, UserExistException;

    EstudianteResponse listarEstudiante(Integer id) throws EstudianteNotFoundException;

    List<EstudianteResponse> listarEstudiantes();

    List<EstudianteResponse> listarEstudiantesPorPensum(Integer pensumId) throws PensumNotFoundException;

    List<EstudianteResponse> listarEstudiantesPorCohorte(Integer cohorteId) throws CohorteNotFoundException;

    List<EstudianteResponse> listarEstudiantesPorPrograma(Integer programaId) throws ProgramaNotFoundException;

    List<EstudianteResponse> listarEstudiantesPorEstado(Integer estadoEstudianteId) throws EstadoEstudianteNotFoundException;


    List<EstudianteResponse> listarEstudiantesPorGrupoCohorteConMatriculaEnCurso(Long grupoCohorteId)
            throws CohorteNotFoundException;
}
