package com.sistemas_mangager_be.edu_virtual_ufps.services.interfaces;

import com.sistemas_mangager_be.edu_virtual_ufps.exceptions.EstudianteNotFoundException;
import com.sistemas_mangager_be.edu_virtual_ufps.exceptions.GrupoNotFoundException;
import com.sistemas_mangager_be.edu_virtual_ufps.exceptions.MateriaNotFoundException;
import com.sistemas_mangager_be.edu_virtual_ufps.exceptions.MatriculaException;
import com.sistemas_mangager_be.edu_virtual_ufps.shared.DTOs.MateriaDTO;
import com.sistemas_mangager_be.edu_virtual_ufps.shared.DTOs.MatriculaDTO;
import com.sistemas_mangager_be.edu_virtual_ufps.shared.responses.*;

import java.util.List;

public interface IMatriculaService {

    MatriculaDTO crearMatricula(MatriculaDTO matriculaDTO, String usuario)
            throws EstudianteNotFoundException, GrupoNotFoundException, MatriculaException;

    void anularMatricula(Long idMatricula, String usuario) throws MatriculaException;

    CorreoResponse enviarCorreo(Integer estudianteId, String usuario) throws EstudianteNotFoundException, MatriculaException;

    boolean verificarCorreoEnviado(Integer estudianteId) throws EstudianteNotFoundException;

    List<MatriculaResponse> listarMatriculasEnCursoPorEstudiante(Integer estudianteId) throws EstudianteNotFoundException;

    List<MateriaDTO> listarMateriasNoMatriculadasPorEstudiante(Integer estudianteId) throws EstudianteNotFoundException;

    List<PensumResponse> listarPensumPorEstudiante(Integer estudianteId) throws EstudianteNotFoundException;

    List<GrupoCohorteDocenteResponse> listarGrupoCohorteDocentePorMateria(String codigoMateria)
            throws MateriaNotFoundException;

    List<CambioEstadoMatriculaResponse> listarCambiosdeEstadoMatriculaPorEstudiante(Integer estudianteId) throws EstudianteNotFoundException, MatriculaException;

}
