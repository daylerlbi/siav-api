package com.sistemas_mangager_be.edu_virtual_ufps.modulo_seguimiento.dtos;

import com.sistemas_mangager_be.edu_virtual_ufps.modulo_seguimiento.entities.enums.TipoDocumento;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * DTO for {@link com.sistemas_mangager_be.edu_virtual_ufps.modulo_seguimiento.entities.Documento}
 */
@Data
public class DocumentoDto implements Serializable {
    Integer id;
    String tipoArchivo;
    String tag;
    String nombre;
    String path;
    String peso;
    TipoDocumento tipoDocumento;
    Integer idProyecto;
    String url;
    List<RetroalimentacionDto> retroalimentacion;
}