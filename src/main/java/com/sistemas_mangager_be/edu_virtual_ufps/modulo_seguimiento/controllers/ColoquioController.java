package com.sistemas_mangager_be.edu_virtual_ufps.modulo_seguimiento.controllers;

import com.sistemas_mangager_be.edu_virtual_ufps.modulo_seguimiento.dtos.ColoquioDto;
import com.sistemas_mangager_be.edu_virtual_ufps.modulo_seguimiento.services.ColoquioService;
import com.sistemas_mangager_be.edu_virtual_ufps.shared.responses.GrupoCohorteDocenteResponse;
import com.sistemas_mangager_be.edu_virtual_ufps.shared.responses.HttpResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/coloquios")
@Tag(name = "Gestión de coloquios(informes)", description = "API para la administración de coloquios." +
        " CRUD basico de coloquios, así como la obtención de coloquios por grupo de cohorte.")
public class ColoquioController {

    private final ColoquioService coloquioService;

    public ColoquioController(ColoquioService coloquioService) {
        this.coloquioService = coloquioService;
    }

    @Operation(
            summary = "Crear un nuevo coloquio",
            description = "Registra un nuevo coloquio académico con fecha, hora, lugar, descripción y grupo de cohorte asociado."
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Datos del coloquio a registrar.",
            required = true,
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ColoquioDto.class),
                    examples = @ExampleObject(
                            name = "Nuevo coloquio",
                            summary = "Coloquio con grupo de cohorte asignado",
                            value = """
                    {
                      "fecha": "2025-06-20",
                      "hora": "16:37:59",
                      "lugar": "meet.com",
                      "descripcion": "Primer informe",
                      "grupoCohorteId": 2
                    }
                    """
                    )
            )
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Coloquio creado correctamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ColoquioDto.class))),
            @ApiResponse(responseCode = "500", description = "Error del servidor",
                    content = @Content(schema = @Schema(implementation = HttpResponse.class)))
    })
    @PostMapping
    public ResponseEntity<ColoquioDto> crearColoquio(@RequestBody ColoquioDto coloquioDto) {
        return ResponseEntity.ok(coloquioService.crearColoquio(coloquioDto));
    }

    @Operation(
            summary = "Obtener coloquio por ID",
            description = "Devuelve los detalles de un coloquio específico a partir de su identificador único."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Coloquio encontrado exitosamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ColoquioDto.class))),
            @ApiResponse(responseCode = "500", description = "Error del servidor",
                    content = @Content(schema = @Schema(implementation = HttpResponse.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<ColoquioDto> obtenerColoquioPorId(
            @Parameter(description = "ID del coloquio a consultar", example = "5", required = true)
            @PathVariable Integer id) {
        return ResponseEntity.ok(coloquioService.obtenerColoquioPorId(id));
    }

    @Operation(
            summary = "Listar coloquios por grupo de cohorte",
            description = "Devuelve una lista de coloquios asociados al grupo de cohorte especificado por su ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Coloquios encontrados",
                    content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ColoquioDto.class)))),
            @ApiResponse(responseCode = "500", description = "Error del servidor",
                    content = @Content(schema = @Schema(implementation = HttpResponse.class)))
    })
    @GetMapping("/grupo-cohorte/{grupoCohorteId}")
    public ResponseEntity<List<ColoquioDto>> obtenerColoquiosPorGrupoCohorteId(
            @Parameter(description = "ID del grupo de cohorte", example = "2", required = true)
            @PathVariable Long grupoCohorteId) {
        return ResponseEntity.ok(coloquioService.obtenerColoquiosPorGrupoCohorteId(grupoCohorteId));
    }

    @Operation(
            summary = "Listar coloquios del estudiante autenticado",
            description = "Devuelve una lista de coloquios en los que participa el usuario autenticado como estudiante."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Coloquios encontrados correctamente",
                    content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ColoquioDto.class)))),
            @ApiResponse(responseCode = "500", description = "Error del servidor",
                    content = @Content(schema = @Schema(implementation = HttpResponse.class)))
    })
    @GetMapping("/estudiante")
    public ResponseEntity<List<ColoquioDto>> obtenerColoquiosPorUsuarioId() {
        return ResponseEntity.ok(coloquioService.obtenerColoquiosPorUsuarioId());
    }

    @Operation(
            summary = "Actualizar coloquio",
            description = "Actualiza los datos de un coloquio existente: fecha, hora, lugar y descripción."
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Datos del coloquio a actualizar",
            required = true,
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ColoquioDto.class),
                    examples = @ExampleObject(
                            name = "Actualizar coloquio",
                            summary = "Ejemplo de actualización de datos del coloquio",
                            value = """
                    {
                      "fecha": "2025-06-20",
                      "hora": "20:37:59",
                      "lugar": "meet.com",
                      "descripcion": "Segundo informe"
                    }
                    """
                    )
            )
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Coloquio actualizado correctamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ColoquioDto.class))),
            @ApiResponse(responseCode = "500", description = "Error del servidor",
                    content = @Content(schema = @Schema(implementation = HttpResponse.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<ColoquioDto> actualizarColoquio(
            @Parameter(description = "ID del coloquio a actualizar", example = "7", required = true)
            @PathVariable Integer id,
            @RequestBody ColoquioDto coloquioDto) {
        return ResponseEntity.ok(coloquioService.actualizarColoquio(id, coloquioDto));
    }

    @Operation(
            summary = "Listar estudiantes con entregas para un coloquio",
            description = "Retorna una lista de estudiantes y sus entregas asociadas al coloquio especificado por su ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Entregas encontradas correctamente",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(type = "object"))
                    )),
            @ApiResponse(responseCode = "500", description = "Error del servidor",
                    content = @Content(schema = @Schema(implementation = HttpResponse.class)))
    })
    @GetMapping("/entregas/{idColoquio}")
    public ResponseEntity<List<Map<String, Object>>> obtenerColoquiosConEntregasPorUsuarioId(
            @Parameter(description = "ID del coloquio", example = "3", required = true)
            @PathVariable Integer idColoquio) {
        return ResponseEntity.ok(coloquioService.estudiantesConEntregasPorColoquioId(idColoquio));
    }

    @Operation(
            summary = "Eliminar coloquio",
            description = "Elimina un coloquio específico identificado por su ID. Esta acción no se puede deshacer."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Coloquio eliminado correctamente (sin contenido)"),
            @ApiResponse(responseCode = "500", description = "Error del servidor",
                    content = @Content(schema = @Schema(implementation = HttpResponse.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarColoquio(
            @Parameter(description = "ID del coloquio a eliminar", example = "5", required = true)
            @PathVariable Integer id) {
        coloquioService.eliminarColoquio(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Listar grupos de cohorte del docente autenticado",
            description = "Devuelve una lista de grupos de cohorte en los que el usuario autenticado participa como docente."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Grupos de cohorte encontrados correctamente",
                    content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = GrupoCohorteDocenteResponse.class)))),
            @ApiResponse(responseCode = "500", description = "Error del servidor",
                    content = @Content(schema = @Schema(implementation = HttpResponse.class)))
    })
    @GetMapping("/docente")
    public ResponseEntity<List<GrupoCohorteDocenteResponse>> listarGruposPorDocente() {
        List<GrupoCohorteDocenteResponse> grupoCohorteDocenteResponse = coloquioService.listarGruposPorDocente();
        return ResponseEntity.ok(grupoCohorteDocenteResponse);
    }

}
