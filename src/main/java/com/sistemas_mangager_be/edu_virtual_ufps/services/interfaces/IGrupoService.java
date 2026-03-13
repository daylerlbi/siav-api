package com.sistemas_mangager_be.edu_virtual_ufps.services.interfaces;

import com.sistemas_mangager_be.edu_virtual_ufps.exceptions.*;
import com.sistemas_mangager_be.edu_virtual_ufps.shared.DTOs.GrupoDTO;
import com.sistemas_mangager_be.edu_virtual_ufps.shared.requests.GrupoRequest;
import com.sistemas_mangager_be.edu_virtual_ufps.shared.responses.EstudianteGrupoResponse;
import com.sistemas_mangager_be.edu_virtual_ufps.shared.responses.GrupoCohorteDocenteResponse;
import com.sistemas_mangager_be.edu_virtual_ufps.shared.responses.GrupoCohorteResponse;
import com.sistemas_mangager_be.edu_virtual_ufps.shared.responses.GrupoResponse;

import java.util.List;

public interface IGrupoService {
    GrupoDTO crearGrupo(GrupoDTO grupoDTO)
            throws MateriaNotFoundException, CohorteNotFoundException, UserNotFoundException,
            RoleNotFoundException;

    GrupoDTO actualizarGrupo(GrupoDTO grupoDTO, Integer id)
            throws MateriaNotFoundException, CohorteNotFoundException,
            UserNotFoundException, RoleNotFoundException, GrupoNotFoundException;

    GrupoResponse listarGrupo(Integer id) throws GrupoNotFoundException;

    List<GrupoResponse> listarGrupos();

    List<GrupoResponse> listarGruposPorMateria(Integer materiaId) throws MateriaNotFoundException;

    void desactivarGrupo(Integer id) throws GrupoNotFoundException;

    void activarGrupo(Integer id) throws GrupoNotFoundException;

    GrupoCohorteDocenteResponse vincularCohorteDocente(GrupoRequest grupoRequest)
            throws CohorteNotFoundException, GrupoNotFoundException, UserNotFoundException;

    void vincularGrupoMoodle(Long id, String moodleId) throws GrupoNotFoundException, GrupoExistException;

    void actualizarVinculacionCohorteDocente(Long vinculacionId, GrupoRequest grupoRequest)
            throws CohorteNotFoundException, GrupoNotFoundException, UserNotFoundException,
            VinculacionNotFoundException;

    GrupoCohorteDocenteResponse listarGrupoCohorteDocente(Long id) throws VinculacionNotFoundException;

    List<GrupoCohorteDocenteResponse> listarGrupoCohorteDocentes();

    List<GrupoCohorteDocenteResponse> listarGruposPorCohorte(Integer cohorteId) throws CohorteNotFoundException;

    List<GrupoCohorteDocenteResponse> listarGruposPorGrupo(Integer grupoId) throws GrupoNotFoundException;

    List<GrupoCohorteDocenteResponse> listarGruposPorDocente(Integer docenteId) throws UserNotFoundException;

    List<GrupoCohorteResponse> listarGruposPorCohorteGrupo(Integer cohorteGrupoId) throws CohorteNotFoundException;

    EstudianteGrupoResponse listarEstudiantesPorGrupoCohorte(Long grupoCohorteId);

    List<GrupoCohorteDocenteResponse> listarGruposPorPrograma(Integer programaId) throws ProgramaNotFoundException;

    List<GrupoCohorteDocenteResponse> listarGruposCohortePorMateria(Integer materiaId) throws MateriaNotFoundException;
}
