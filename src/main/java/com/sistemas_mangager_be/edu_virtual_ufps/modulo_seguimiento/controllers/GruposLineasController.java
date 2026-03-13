package com.sistemas_mangager_be.edu_virtual_ufps.modulo_seguimiento.controllers;

import com.sistemas_mangager_be.edu_virtual_ufps.modulo_seguimiento.dtos.GrupoInvestigacionDto;
import com.sistemas_mangager_be.edu_virtual_ufps.modulo_seguimiento.dtos.LineaInvestigacionDto;
import com.sistemas_mangager_be.edu_virtual_ufps.modulo_seguimiento.services.GruposLineasService;
import com.sistemas_mangager_be.edu_virtual_ufps.shared.responses.HttpResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/grupos-lineas")
@Tag(name = "Gestión de grupos y lineas de investigación", description = "API para la administración de grupos y lineas de investigación." +
        " Creacion, edición y eliminación de grupos y lineas de investigación.")
public class GruposLineasController {

    private final GruposLineasService gruposLineasService;

    public GruposLineasController(GruposLineasService gruposLineasService) {
        this.gruposLineasService = gruposLineasService;
    }

    @Operation(
            summary = "Crear grupo de investigación",
            description = "Registra un nuevo grupo de investigación asociado a un programa académico."
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Datos del grupo de investigación a crear.",
            required = true,
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = GrupoInvestigacionDto.class),
                    examples = @ExampleObject(
                            name = "Nuevo grupo",
                            summary = "Ejemplo de creación de grupo de investigación",
                            value = """
                    {
                      "nombre": "Grupo de Investigación en Inteligencia Artificial",
                      "programa": {
                        "id": 2
                      }
                    }
                    """
                    )
            )
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Grupo creado correctamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = GrupoInvestigacionDto.class))),
            @ApiResponse(responseCode = "500", description = "Error del servidor",
                    content = @Content(schema = @Schema(implementation = HttpResponse.class)))
    })
    @PostMapping("/grupo")
    public GrupoInvestigacionDto crearGrupo(@RequestBody GrupoInvestigacionDto dto) {
        return gruposLineasService.crearGrupo(dto);
    }

    @Operation(
            summary = "Actualizar grupo de investigación",
            description = "Actualiza el nombre del grupo de investigación identificado por su ID."
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Nuevo nombre del grupo de investigación.",
            required = true,
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = GrupoInvestigacionDto.class),
                    examples = @ExampleObject(
                            name = "Actualizar nombre del grupo",
                            summary = "Ejemplo para modificar el nombre de un grupo",
                            value = """
                    {
                      "nombre": "Grupo Sistemas Inteligentes"
                    }
                    """
                    )
            )
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Grupo actualizado correctamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = GrupoInvestigacionDto.class))),
            @ApiResponse(responseCode = "500", description = "Error del servidor",
                    content = @Content(schema = @Schema(implementation = HttpResponse.class)))
    })
    @PutMapping("/grupo/{id}")
    public GrupoInvestigacionDto actualizarGrupo(
            @Parameter(description = "ID del grupo a actualizar", example = "1", required = true) @PathVariable Integer id,
            @RequestBody GrupoInvestigacionDto dto) {
        return gruposLineasService.actualizarGrupo(id, dto);
    }

    @Operation(
            summary = "Eliminar grupo de investigación",
            description = "Elimina un grupo de investigación según su identificador único."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Grupo eliminado correctamente (sin contenido)"),
            @ApiResponse(responseCode = "500", description = "Error del servidor",
                    content = @Content(schema = @Schema(implementation = HttpResponse.class)))
    })
    @DeleteMapping("/grupo/{id}")
    public void eliminarGrupo(@Parameter(description = "ID del grupo a eliminar", example = "3", required = true) @PathVariable Integer id) {
        gruposLineasService.eliminarGrupo(id);
    }

    @Operation(
            summary = "Crear línea de investigación",
            description = "Registra una nueva línea de investigación asociada a un grupo de investigación."
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Datos de la línea de investigación a crear.",
            required = true,
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = LineaInvestigacionDto.class),
                    examples = @ExampleObject(
                            name = "Nueva línea",
                            summary = "Crear una línea de investigación para un grupo",
                            value = """
                    {
                      "nombre": "Aprendizaje Automático 1",
                      "grupoInvestigacion": {
                        "id": 3
                      }
                    }
                    """
                    )
            )
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Línea de investigación creada correctamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = LineaInvestigacionDto.class))),
            @ApiResponse(responseCode = "500", description = "Error del servidor",
                    content = @Content(schema = @Schema(implementation = HttpResponse.class)))
    })
    @PostMapping("/linea")
    public LineaInvestigacionDto crearLinea(@RequestBody LineaInvestigacionDto dto) {
        return gruposLineasService.crearLinea(dto);
    }

    @Operation(
            summary = "Actualizar línea de investigación",
            description = "Actualiza el nombre o el grupo asociado de una línea de investigación existente."
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Datos actualizados de la línea de investigación.",
            required = true,
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = LineaInvestigacionDto.class),
                    examples = @ExampleObject(
                            name = "Actualizar línea",
                            summary = "Modificar nombre o grupo asociado de la línea de investigación",
                            value = """
                    {
                      "nombre": "Aprendizaje Automático",
                      "grupoInvestigacion": {
                        "id": 3
                      }
                    }
                    """
                    )
            )
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Línea de investigación actualizada correctamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = LineaInvestigacionDto.class))),
            @ApiResponse(responseCode = "500", description = "Error del servidor",
                    content = @Content(schema = @Schema(implementation = HttpResponse.class)))
    })
    @PutMapping("/linea/{id}")
    public LineaInvestigacionDto actualizarLinea(
            @Parameter(description = "ID de la línea de investigación a actualizar", example = "4", required = true) @PathVariable Integer id,
            @RequestBody LineaInvestigacionDto dto) {
        return gruposLineasService.actualizarLinea(id, dto);
    }

    @Operation(
            summary = "Eliminar línea de investigación",
            description = "Elimina una línea de investigación identificada por su ID. Esta acción no se puede deshacer."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Línea eliminada correctamente (sin contenido)"),
            @ApiResponse(responseCode = "500", description = "Error del servidor",
                    content = @Content(schema = @Schema(implementation = HttpResponse.class)))
    })
    @DeleteMapping("/linea/{id}")
    public void eliminarLinea(@Parameter(description = "ID de la línea de investigación a eliminar", example = "5", required = true)
                                  @PathVariable Integer id) {
        gruposLineasService.eliminarLinea(id);
    }
}
