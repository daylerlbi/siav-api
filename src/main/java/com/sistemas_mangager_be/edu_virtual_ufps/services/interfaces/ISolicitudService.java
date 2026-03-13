package com.sistemas_mangager_be.edu_virtual_ufps.services.interfaces;

import com.sistemas_mangager_be.edu_virtual_ufps.entities.Solicitud;
import com.sistemas_mangager_be.edu_virtual_ufps.exceptions.EstudianteNotFoundException;
import com.sistemas_mangager_be.edu_virtual_ufps.exceptions.SolicitudException;
import com.sistemas_mangager_be.edu_virtual_ufps.shared.DTOs.SolicitudDTO;
import com.sistemas_mangager_be.edu_virtual_ufps.shared.responses.SolicitudResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ISolicitudService {

    Solicitud crearSolicitud(SolicitudDTO solicitudDTO, Integer tipoSolicitudId)
            throws SolicitudException, EstudianteNotFoundException;

    Solicitud actualizarSolicitud(Long solicitudId, Integer tipoSolicitudId, SolicitudDTO solicitudDTO)
            throws SolicitudException, EstudianteNotFoundException;

    SolicitudResponse listarSolicitudPorId(Long id) throws SolicitudException;

    List<SolicitudResponse> listarSolicitudesPorTipo(Integer tipoSolicitudId) throws SolicitudException;

    void aprobarSolicitud(Long solicitudId, Integer tiposolicitudId, MultipartFile file, String usuario)
            throws SolicitudException, IOException;
}
