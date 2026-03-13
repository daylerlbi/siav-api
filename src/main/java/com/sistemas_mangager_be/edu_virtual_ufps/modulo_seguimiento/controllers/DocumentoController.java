package com.sistemas_mangager_be.edu_virtual_ufps.modulo_seguimiento.controllers;

import com.sistemas_mangager_be.edu_virtual_ufps.modulo_seguimiento.dtos.DocumentoDto;
import com.sistemas_mangager_be.edu_virtual_ufps.modulo_seguimiento.dtos.DocumentoUploadDto;
import com.sistemas_mangager_be.edu_virtual_ufps.modulo_seguimiento.dtos.RetroalimentacionDto;
import com.sistemas_mangager_be.edu_virtual_ufps.modulo_seguimiento.entities.enums.TipoDocumento;
import com.sistemas_mangager_be.edu_virtual_ufps.modulo_seguimiento.services.DocumentoService;
import com.sistemas_mangager_be.edu_virtual_ufps.shared.responses.HttpResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/documentos")
@Tag(name = "Gestión de documentos de proyectos", description = "API para la administración de documentos de los proyectos." +
        " Incluye operaciones para subir, listar, eliminar documentos y gestionar retroalimentaciones.")
public class DocumentoController {

    private final DocumentoService documentoService;

    public DocumentoController(DocumentoService documentoService) {
        this.documentoService = documentoService;
    }

    @Operation(
            summary = "Subir documento a un proyecto",
            description = "Permite subir un archivo a un proyecto específico, especificando el tipo de documento y una etiqueta opcional."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Documento subido correctamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = DocumentoDto.class))),
            @ApiResponse(responseCode = "500", description = "Error del servidor",
                    content = @Content(schema = @Schema(implementation = HttpResponse.class)))
    })
    @Parameters({
            @Parameter(name = "idProyecto", description = "ID del proyecto", example = "12", required = true),
            @Parameter(name = "archivo", description = "Archivo a subir (PDF, DOCX, etc.)", required = true,
                    content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE)),
            @Parameter(name = "tipoDocumento", description = "Tipo de documento (enum)", example = "TESIS", required = true),
            @Parameter(name = "tag", description = "Etiqueta adicional para identificar el documento", example = "Diapositivas")
    })
    @PostMapping("/{idProyecto}")
    public ResponseEntity<DocumentoDto> subirDocumento(
            @PathVariable Integer idProyecto,
            @RequestParam("archivo") MultipartFile archivo,
            @RequestParam("tipoDocumento") TipoDocumento tipoDocumento,
            @RequestParam("tag") String tag
    ) {
        return ResponseEntity.ok(documentoService.guardarDocumento(idProyecto, archivo, tipoDocumento, tag));
    }

    @Operation(
            summary = "Listar documentos por proyecto",
            description = "Devuelve todos los documentos asociados a un proyecto específico. Se puede filtrar opcionalmente por tipo de documento."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Documentos encontrados correctamente",
                    content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = DocumentoDto.class)))),
            @ApiResponse(responseCode = "500", description = "Error del servidor",
                    content = @Content(schema = @Schema(implementation = HttpResponse.class)))
    })
    @Parameters({
            @Parameter(name = "idProyecto", description = "ID del proyecto", example = "10", required = true),
            @Parameter(name = "tipoDocumento", description = "Tipo de documento a filtrar (opcional)", example = "TESIS")
    })
    @GetMapping("/proyecto/{idProyecto}")
    public ResponseEntity<List<DocumentoDto>> listarPorProyecto(@PathVariable Integer idProyecto,
                                                                @RequestParam(value = "tipoDocumento", required = false) TipoDocumento tipoDocumento) {
        return ResponseEntity.ok(documentoService.listarPorProyecto(idProyecto, tipoDocumento));
    }

    @Operation(
            summary = "Eliminar documento",
            description = "Elimina un documento específico del sistema usando su ID. Esta acción también elimina el archivo almacenado físicamente si aplica."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Documento eliminado correctamente (sin contenido)"),
            @ApiResponse(responseCode = "500", description = "Error del servidor",
                    content = @Content(schema = @Schema(implementation = HttpResponse.class)))
    })
    @Parameters({
            @Parameter(name = "id", description = "ID del documento a eliminar", example = "15", required = true)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarDocumento(@PathVariable Integer id) {
        documentoService.eliminarDocumento(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/sustentacion")
    public ResponseEntity<DocumentoDto> agregarDocumentoSustentacion(
            @RequestParam("archivo") MultipartFile archivo,
            @RequestParam TipoDocumento tipoDocumento,
            @RequestParam Integer idSustentacion,
            @RequestParam("tag") String tag) {
        return ResponseEntity.ok(documentoService.agregarDocumentoaSustentacion(archivo, tipoDocumento, idSustentacion, tag));
    }

    @GetMapping("/sustentacion/{idSustentacion}")
    public ResponseEntity<List<DocumentoDto>> listarDocumentosSustentacion(@PathVariable Integer idSustentacion) {
        return ResponseEntity.ok(documentoService.listarDocumentosPorSustentacion(idSustentacion));
    }

    @Operation(
            summary = "Subir documento para un coloquio",
            description = "Permite subir un archivo relacionado con un coloquio específico, indicando el tipo de documento y una etiqueta opcional."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Documento subido correctamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = DocumentoDto.class))),
            @ApiResponse(responseCode = "500", description = "Error del servidor",
                    content = @Content(schema = @Schema(implementation = HttpResponse.class)))
    })
    @Parameters({
            @Parameter(name = "archivo", description = "Archivo a subir (PDF, DOCX, etc.)", required = true,
                    content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE)),
            @Parameter(name = "tipoDocumento", description = "Tipo de documento (enum)", example = "COLOQUIO", required = true),
            @Parameter(name = "idColoquio", description = "ID del coloquio al que se asociará el documento", example = "8", required = true),
            @Parameter(name = "tag", description = "Etiqueta descriptiva del documento", example = "informe-final", required = true)
    })
    @PostMapping("/coloquio")
    public ResponseEntity<DocumentoDto> agregarDocumentoColoquio(
            @RequestParam("archivo") MultipartFile archivo,
            @RequestParam TipoDocumento tipoDocumento,
            @RequestParam Integer idColoquio,
            @RequestParam("tag") String tag) {
        return ResponseEntity.ok(documentoService.agregarDocumentoaColoquio(archivo, tipoDocumento, idColoquio, tag));
    }

    @Operation(
            summary = "Listar documentos de coloquio por estudiante",
            description = "Devuelve los documentos asociados a un coloquio específico. Si se especifica el ID del estudiante, filtra los documentos subidos por ese estudiante."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Documentos listados correctamente",
                    content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = DocumentoDto.class)))),
            @ApiResponse(responseCode = "500", description = "Error del servidor",
                    content = @Content(schema = @Schema(implementation = HttpResponse.class)))
    })
    @Parameters({
            @Parameter(name = "idColoquio", description = "ID del coloquio", required = true, example = "5"),
            @Parameter(name = "idEstudiante", description = "ID del estudiante para filtrar documentos (opcional)", example = "14")
    })
    @GetMapping("/coloquio/{idColoquio}")
    public ResponseEntity<List<DocumentoDto>> listarDocumentosColoquioEstudiante(@PathVariable Integer idColoquio, @RequestParam(required = false) Integer idEstudiante) {
        return ResponseEntity.ok(documentoService.listarDocumentosPorColoquioEstudiante(idColoquio, idEstudiante));
    }

    @Operation(
            summary = "Agregar retroalimentación a un documento",
            description = "Permite asociar una retroalimentación a un documento previamente subido.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = RetroalimentacionDto.class),
                            examples = @ExampleObject(
                                    name = "Ejemplo de retroalimentación",
                                    summary = "Retroalimentación sobre un documento",
                                    value = """
                {
                  "descripcion": "El archivo debe tener los logos institucionales.",
                  "documentoId": 33
                }
                """
                            )
                    )
            )
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retroalimentación agregada correctamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = RetroalimentacionDto.class))),
            @ApiResponse(responseCode = "500", description = "Error del servidor",
                    content = @Content(schema = @Schema(implementation = HttpResponse.class)))
    })
    @PostMapping("/retroalimentacion")
    public ResponseEntity<RetroalimentacionDto> agregarRetroalimentacionADocumentos(@RequestBody RetroalimentacionDto retroalimentacionDto) {
        return ResponseEntity.ok(documentoService.agregarRetroalimentacionADocumentos(retroalimentacionDto));
    }

    @Operation(
            summary = "Editar retroalimentación de un documento",
            description = "Permite modificar la descripción de una retroalimentación previamente registrada.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = RetroalimentacionDto.class),
                            examples = @ExampleObject(
                                    name = "Ejemplo de edición de retroalimentación",
                                    summary = "Actualización del texto de retroalimentación",
                                    value = """
                {
                  "id": 8,
                  "descripcion": "descripcion editada"
                }
                """
                            )
                    )
            )
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retroalimentación actualizada correctamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = RetroalimentacionDto.class))),
            @ApiResponse(responseCode = "500", description = "Error del servidor",
                    content = @Content(schema = @Schema(implementation = HttpResponse.class)))
    })
    @PutMapping("/retroalimentacion")
    public ResponseEntity<RetroalimentacionDto> editarRetroalimentacion(@RequestBody RetroalimentacionDto retroalimentacionDto) {
        return ResponseEntity.ok(documentoService.editarRetroalimentacion(retroalimentacionDto));
    }

    @Operation(
            summary = "Eliminar retroalimentación",
            description = "Elimina una retroalimentación existente a partir de su identificador único."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Retroalimentación eliminada correctamente"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(schema = @Schema(implementation = HttpResponse.class)))
    })
    @Parameter(name = "id", description = "ID de la retroalimentación a eliminar", required = true, example = "8")
    @DeleteMapping("/retroalimentacion/{id}")
    public ResponseEntity<Void> eliminarRetroalimentacion(@PathVariable Integer id) {
        documentoService.eliminarRetroalimentacion(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Importar documentos a un proyecto",
            description = "Permite subir múltiples archivos con su tipo de documento y tag correspondiente, asociados a un proyecto ya existente."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Documentos importados exitosamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = DocumentoDto.class))),
            @ApiResponse(responseCode = "500", description = "Error del servidor",
                    content = @Content(schema = @Schema(implementation = HttpResponse.class)))
    })
    @Parameters({
            @Parameter(
                    name = "idProyecto",
                    description = "ID del proyecto al que se asociarán los documentos",
                    required = true,
                    in = ParameterIn.PATH,
                    example = "12"
            ),
            @Parameter(
                    name = "archivos",
                    description = "Lista de archivos a importar",
                    required = true,
                    in = ParameterIn.QUERY,
                    content = @Content(mediaType = "multipart/form-data")
            ),
            @Parameter(
                    name = "tipoDocumentos",
                    description = "Lista de tipos de documento, en el mismo orden que los archivos",
                    required = true,
                    example = "[ANTEPROYECTO, TESIS]"
            ),
            @Parameter(
                    name = "tags",
                    description = "Lista de etiquetas asociadas a cada documento (mismo orden que los archivos)",
                    required = true,
                    example = "[\"Acta Revisión\", \"Informe Final\"]"
            )
    })
    @PostMapping("/importar-a-proyecto/{idProyecto}")
    public ResponseEntity<List<DocumentoDto>> importarDocumento(
            @PathVariable Integer idProyecto,
            @RequestParam("archivos") List<MultipartFile> archivos,
            @RequestParam("tipoDocumentos") List<TipoDocumento> tipoDocumentos,
            @RequestParam("tags") List<String> tags) {

        if (archivos.size() != tipoDocumentos.size() || archivos.size() != tags.size()) {
            throw new RuntimeException("El número de archivos, tipos de documento y tags debe coincidir");
        }

        List<DocumentoUploadDto> documentosUpload = new ArrayList<>();
        for (int i = 0; i < archivos.size(); i++) {
            DocumentoUploadDto dto = new DocumentoUploadDto();
            dto.setArchivo(archivos.get(i));
            dto.setTipoDocumento(tipoDocumentos.get(i));
            dto.setTag(tags.get(i));
            documentosUpload.add(dto);
        }

        List<DocumentoDto> resultado = documentoService.guardarDocumentos(idProyecto, documentosUpload);
        return ResponseEntity.ok(resultado);
    }
}
