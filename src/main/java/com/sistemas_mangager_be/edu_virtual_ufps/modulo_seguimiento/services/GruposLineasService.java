package com.sistemas_mangager_be.edu_virtual_ufps.modulo_seguimiento.services;

import com.sistemas_mangager_be.edu_virtual_ufps.modulo_seguimiento.dtos.GrupoInvestigacionDto;
import com.sistemas_mangager_be.edu_virtual_ufps.modulo_seguimiento.dtos.LineaInvestigacionDto;
import com.sistemas_mangager_be.edu_virtual_ufps.modulo_seguimiento.entities.GrupoInvestigacion;
import com.sistemas_mangager_be.edu_virtual_ufps.modulo_seguimiento.entities.LineaInvestigacion;
import com.sistemas_mangager_be.edu_virtual_ufps.modulo_seguimiento.mappers.GrupoInvestigacionMapper;
import com.sistemas_mangager_be.edu_virtual_ufps.modulo_seguimiento.mappers.LineaInvestigacionMapper;
import com.sistemas_mangager_be.edu_virtual_ufps.modulo_seguimiento.repositories.GrupoInvestigacionRepository;
import com.sistemas_mangager_be.edu_virtual_ufps.modulo_seguimiento.repositories.LineaInvestigacionRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GruposLineasService {
    private final GrupoInvestigacionRepository grupoInvestigacionRepository;
    private final GrupoInvestigacionMapper grupoInvestigacionMapper;
    private final LineaInvestigacionRepository lineaInvestigacionRepository;
    private final LineaInvestigacionMapper lineaInvestigacionMapper;

    @Autowired
    public GruposLineasService(GrupoInvestigacionRepository grupoInvestigacionRepository,
                               GrupoInvestigacionMapper grupoInvestigacionMapper,
                               LineaInvestigacionRepository lineaInvestigacionRepository,
                               LineaInvestigacionMapper lineaInvestigacionMapper) {
        this.grupoInvestigacionRepository = grupoInvestigacionRepository;
        this.grupoInvestigacionMapper = grupoInvestigacionMapper;
        this.lineaInvestigacionRepository = lineaInvestigacionRepository;
        this.lineaInvestigacionMapper = lineaInvestigacionMapper;
    }

    @Transactional
    @PreAuthorize("hasAuthority('ROLE_SUPERADMIN') or hasAuthority('ROLE_ADMIN')")
    public GrupoInvestigacionDto crearGrupo(GrupoInvestigacionDto dto) {
        GrupoInvestigacion entity = grupoInvestigacionMapper.toEntity(dto);
        return grupoInvestigacionMapper.toDto(grupoInvestigacionRepository.save(entity));
    }

    @Transactional
    @PreAuthorize("hasAuthority('ROLE_SUPERADMIN') or hasAuthority('ROLE_ADMIN')")
    public GrupoInvestigacionDto actualizarGrupo(Integer id, GrupoInvestigacionDto dto) {
        GrupoInvestigacion entity = grupoInvestigacionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Grupo de investigación no encontrado con id: " + id));
        grupoInvestigacionMapper.partialUpdate(dto, entity);
        return grupoInvestigacionMapper.toDto(grupoInvestigacionRepository.save(entity));
    }

    @Transactional
    @PreAuthorize("hasAuthority('ROLE_SUPERADMIN') or hasAuthority('ROLE_ADMIN')")
    public void eliminarGrupo(Integer id) {
        grupoInvestigacionRepository.deleteById(id);
    }

    @Transactional
    @PreAuthorize("hasAuthority('ROLE_SUPERADMIN') or hasAuthority('ROLE_ADMIN')")
    public LineaInvestigacionDto crearLinea(LineaInvestigacionDto dto) {
        LineaInvestigacion entity = lineaInvestigacionMapper.toEntity(dto);
        return lineaInvestigacionMapper.toDto(lineaInvestigacionRepository.save(entity));
    }

    @Transactional
    @PreAuthorize("hasAuthority('ROLE_SUPERADMIN') or hasAuthority('ROLE_ADMIN')")
    public LineaInvestigacionDto actualizarLinea(Integer id, LineaInvestigacionDto dto) {
        LineaInvestigacion entity = lineaInvestigacionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Línea de investigación no encontrada con id: " + id));

        lineaInvestigacionMapper.partialUpdate(dto, entity);

        if (dto.getGrupoInvestigacion() != null && dto.getGrupoInvestigacion().getId() != null) {
            GrupoInvestigacion grupo = grupoInvestigacionRepository.findById(dto.getGrupoInvestigacion().getId())
                    .orElseThrow(() -> new EntityNotFoundException("Grupo no encontrado con ID: " + dto.getGrupoInvestigacion().getId()));
            entity.setGrupoInvestigacion(grupo);
        }

        return lineaInvestigacionMapper.toDto(lineaInvestigacionRepository.save(entity));
    }

    @Transactional
    @PreAuthorize("hasAuthority('ROLE_SUPERADMIN') or hasAuthority('ROLE_ADMIN')")
    public void eliminarLinea(Integer id) {
        lineaInvestigacionRepository.deleteById(id);
    }
}
