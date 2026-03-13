package com.sistemas_mangager_be.edu_virtual_ufps.services.interfaces;

import com.sistemas_mangager_be.edu_virtual_ufps.exceptions.ProgramaExistsException;
import com.sistemas_mangager_be.edu_virtual_ufps.exceptions.ProgramaNotFoundException;
import com.sistemas_mangager_be.edu_virtual_ufps.shared.DTOs.ProgramaDTO;
import com.sistemas_mangager_be.edu_virtual_ufps.shared.requests.MoodleRequest;
import com.sistemas_mangager_be.edu_virtual_ufps.shared.responses.SemestreProgramaResponse;

import java.util.List;

public interface IProgramaService {

    ProgramaDTO listarPrograma(Integer id) throws ProgramaNotFoundException;

    ProgramaDTO crearPrograma(ProgramaDTO programaDTO) throws ProgramaExistsException;

    ProgramaDTO actualizarPrograma(ProgramaDTO programaDTO, Integer id)
            throws ProgramaNotFoundException, ProgramaExistsException;

    List<ProgramaDTO> listarProgramas();

    void vincularHistoricoMoodleId(MoodleRequest moodleRequest)
            throws ProgramaNotFoundException, ProgramaExistsException;

    void vincularMoodleId(MoodleRequest moodleRequest) throws ProgramaNotFoundException, ProgramaExistsException;

    SemestreProgramaResponse listarSemestresPorPrograma(Integer programaId) throws ProgramaNotFoundException;

    ProgramaDTO listarProgramaPorCodigo(String codigo) throws ProgramaNotFoundException;
}
