package com.sistemas_mangager_be.edu_virtual_ufps.modulo_seguimiento.controllers;

import com.sistemas_mangager_be.edu_virtual_ufps.modulo_seguimiento.dtos.*;
import com.sistemas_mangager_be.edu_virtual_ufps.modulo_seguimiento.entities.enums.TipoSustentacion;
import com.sistemas_mangager_be.edu_virtual_ufps.modulo_seguimiento.services.ProyectoService;
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

@RestController
@RequestMapping("/api/proyectos")
@Tag(name = "Gestión de proyectos", description = "API para la administración de proyectos académicos." +
        " CRUD basico de proyectos, asignación de usuarios, validación de fases y cálculo de definitivas.")
public class ProyectoController {

    private final ProyectoService proyectoService;

    public ProyectoController(ProyectoService proyectoService) {
        this.proyectoService = proyectoService;
    }

    @Operation(
            summary = "Crear nuevo proyecto del estudiante",
            description = "Registra un nuevo proyecto académico para el estudiante.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProyectoDto.class),
                            examples = @ExampleObject(
                                    name = "Ejemplo de Proyecto",
                                    summary = "Proyecto académico con metas ODS y objetivos específicos",
                                    value = """
                        {
                          "titulo": "Sistema de gestión de proyectos académicos",
                          "pregunta": "¿Cómo mejorar la eficiencia en la gestión de proyectos de investigación?",
                          "problema": "Falta de organización y seguimiento en los proyectos actuales.",
                          "objetivoGeneral": "Desarrollar una plataforma web para gestionar proyectos académicos.",
                          "estadoActual": 1,
                          "objetivosEspecificos": [
                            {
                              "numeroOrden": "1",
                              "descripcion": "Analizar los requerimientos del sistema."
                            },
                            {
                              "numeroOrden": "2",
                              "descripcion": "Diseñar la arquitectura del sistema."
                            }
                          ],
                          "metaODS": [
                            {
                              "id": 1
                            },
                            {
                              "id": 17
                            }
                          ]
                        }
                        """
                            )
                    )
            )
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Proyecto creado correctamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProyectoDto.class))),
            @ApiResponse(responseCode = "500", description = "Error del servidor",
                    content = @Content(schema = @Schema(implementation = HttpResponse.class)))
    })
    @PostMapping
    public ResponseEntity<ProyectoDto> crearProyecto(@RequestBody ProyectoDto dto) {
        return ResponseEntity.ok(proyectoService.crearProyecto(dto));
    }

    @Operation(
            summary = "Obtener proyecto del estudiante autenticado",
            description = "Devuelve el proyecto académico registrado por el estudiante actualmente autenticado."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Proyecto obtenido correctamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProyectoDto.class))),
            @ApiResponse(responseCode = "500", description = "Error del servidor",
                    content = @Content(schema = @Schema(implementation = HttpResponse.class)))
    })
    @GetMapping("/proyecto-estudiante")
    public ResponseEntity<ProyectoDto> obtenerProyectoEstudiante() {
        return ResponseEntity.ok(proyectoService.obtenerProyectoEstudiante());
    }

    @Operation(
            summary = "Obtener proyecto por ID",
            description = "Devuelve la información de un proyecto académico dado su identificador único."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Proyecto encontrado correctamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProyectoDto.class))),
            @ApiResponse(responseCode = "500", description = "Error del servidor",
                    content = @Content(schema = @Schema(implementation = HttpResponse.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<ProyectoDto> obtenerProyecto(@PathVariable Integer id) {
        return ResponseEntity.ok(proyectoService.obtenerProyecto(id));
    }

    @Operation(
            summary = "Listar proyectos con filtros opcionales",
            description = "Obtiene una lista de proyectos académicos, con opción de filtrar por línea de investigación, grupo de investigación o programa académico."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de proyectos obtenida correctamente",
                    content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ProyectoDto.class)))),
            @ApiResponse(responseCode = "500", description = "Error del servidor",
                    content = @Content(schema = @Schema(implementation = HttpResponse.class)))
    })
    @GetMapping
    public ResponseEntity<List<ProyectoDto>> listarProyectos(@RequestParam(required = false) Integer lineaId,
                                                             @RequestParam(required = false) Integer grupoId,
                                                             @RequestParam(required = false) Integer programaId) {
        return ResponseEntity.ok(proyectoService.listarProyectos(lineaId, grupoId, programaId));
    }

    @Operation(
            summary = "Actualizar proyecto",
            description = "Permite actualizar datos básicos o el progreso de los objetivos de un proyecto existente."
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Datos del proyecto a actualizar. Puede contener datos básicos o información de avance.",
            required = true,
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProyectoDto.class),
                    examples = {
                            @ExampleObject(
                                    name = "Actualizar información básica",
                                    summary = "Datos básicos como título, objetivos, línea de investigación",
                                    value = """
                        {
                          "titulo": "Sistema de gestión de proyectos académicos",
                          "pregunta": "¿Cómo mejorar la eficiencia en la gestión de proyectos de investigación? 11",
                          "problema": "Falta de organización y seguimiento en los proyectos actuales. 11",
                          "objetivoGeneral": "Desarrollar una plataforma web para gestionar proyectos académicos 11",
                          "objetivosEspecificos": [
                            {
                              "id": 1,
                              "numeroOrden": 1,
                              "descripcion": "Analizar los requerimientos del sistemas"
                            },
                            {
                              "id": 2,
                              "numeroOrden": 2,
                              "descripcion": "Diseñar la arquitectura del sistemas"
                            }
                          ],
                          "lineaInvestigacion": {
                            "id": 2
                          },
                          "metaODS": [
                            {
                                "id": 1
                            },
                            {
                                "id": 17
                            }
                          ],
                          "recomendacionDirectores": "Matias y Nelson"
                        }
                        """
                            ),
                            @ExampleObject(
                                    name = "Actualizar avance del proyecto",
                                    summary = "Estado actual y progreso de objetivos específicos",
                                    value = """
                        {
                          "estadoActual": 2,
                          "objetivosEspecificos": [
                            {
                              "id": 58,
                              "avanceReportado": 100,
                              "avanceReal": 10,
                              "evaluacion": {
                                "director": true,
                                "codirector": true
                              },
                              "fecha_inicio": "2025-04-02",
                              "fecha_fin": "2025-04-15"
                            },
                            {
                              "id": 59,
                              "avanceReportado": 100,
                              "avanceReal": 10,
                              "evaluacion": {
                                "director": true,
                                "codirector": true
                              },
                              "fecha_inicio": "2025-04-26",
                              "fecha_fin": "2025-04-30"
                            }
                          ]
                        }
                        """
                            )
                    }
            )
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Proyecto actualizado correctamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProyectoDto.class))),
            @ApiResponse(responseCode = "500", description = "Error del servidor",
                    content = @Content(schema = @Schema(implementation = HttpResponse.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<ProyectoDto> actualizarProyecto(@PathVariable Integer id, @RequestBody ProyectoDto dto) {
        return ResponseEntity.ok(proyectoService.actualizarProyecto(id, dto));
    }

    @Operation(
            summary = "Validar fase del proyecto",
            description = "Permite validar la fase actual de un proyecto. Según el rol del usuario autenticado (estudiante, docente o administrador), se aplican distintas reglas de validación."
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Datos de la validación de la fase, incluyendo el estado y un comentario.",
            required = true,
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ValidacionFaseDto.class),
                    examples = {
                            @ExampleObject(
                                    name = "Rechazo de fase",
                                    summary = "Ejemplo de rechazo de fase por un docente o administrador",
                                    value = """
                    {
                      "estadoRevision": "RECHAZADA",
                      "comentarioRevision": "El documento está incompleto."
                    }
                    """
                            ),
                            @ExampleObject(
                                    name = "Aprobación de fase",
                                    summary = "Ejemplo de aprobación de fase por un docente o administrador",
                                    value = """
                    {
                      "estadoRevision": "ACEPTADA",
                      "comentarioRevision": "La fase cumple con los requisitos."
                    }
                    """
                            )
                    }
            )
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Fase validada correctamente"),
            @ApiResponse(responseCode = "500", description = "Error del servidor",
                    content = @Content(schema = @Schema(implementation = HttpResponse.class)))
    })
    @PutMapping("/validar/{idProyecto}")
    public ResponseEntity<Void> validarFase(@PathVariable Integer idProyecto,
                                            @RequestBody ValidacionFaseDto validacionFaseDto) {
        proyectoService.validarFase(idProyecto, validacionFaseDto.getEstadoRevision(), validacionFaseDto.getComentarioRevision());
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Eliminar proyecto",
            description = "Elimina un proyecto académico según su identificador único."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Proyecto eliminado correctamente (sin contenido)"),
            @ApiResponse(responseCode = "500", description = "Error del servidor",
                    content = @Content(schema = @Schema(implementation = HttpResponse.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProyecto(@PathVariable Integer id) {
        proyectoService.eliminarProyecto(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Asignar usuario a un proyecto",
            description = "Asocia un usuario a un proyecto con un rol determinado (ej. director o codirector)."
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Datos del usuario a asignar, incluyendo el ID del usuario, el ID del proyecto y el rol.",
            required = true,
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = UsuarioProyectoDto.class),
                    examples = @ExampleObject(
                            name = "Asignar codirector",
                            summary = "Asociar un codirector al proyecto",
                            value = """
                    {
                      "idUsuario": 1,
                      "idProyecto": 18,
                      "rol": {
                        "id": 5
                      }
                    }
                    """
                    )
            )
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario asignado correctamente"),
            @ApiResponse(responseCode = "500", description = "Error del servidor",
                    content = @Content(schema = @Schema(implementation = HttpResponse.class)))
    })
    @PostMapping("/asignar-usuario")
    public ResponseEntity<Void> asignarUsuario(@RequestBody UsuarioProyectoDto dto) {
        proyectoService.asignarUsuarioAProyecto(dto);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Desasignar usuario de un proyecto",
            description = "Elimina la relación entre un usuario y un proyecto académico, eliminando su rol asignado en el proyecto."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Usuario desasignado correctamente (sin contenido)"),
            @ApiResponse(responseCode = "500", description = "Error del servidor",
                    content = @Content(schema = @Schema(implementation = HttpResponse.class)))
    })
    @DeleteMapping("/{idProyecto}/usuario/{idUsuario}")
    public ResponseEntity<Void> desasignarUsuario(@PathVariable Integer idProyecto, @PathVariable Integer idUsuario) {
        proyectoService.desasignarUsuarioDeProyecto(idUsuario, idProyecto);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Obtener definitiva del proyecto",
            description = "Calcula y asigna la nota definitiva del proyecto académico, según las notas dadas por los jurados en la sustentación de tipo TESIS."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Nota definitiva calculada correctamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = DefinitivaDto.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(schema = @Schema(implementation = HttpResponse.class)))
    })
    @GetMapping("/definitiva")
    public ResponseEntity<DefinitivaDto> obtenerDefinitiva(@Parameter(description = "ID del proyecto", example = "2", required = true) @RequestParam Integer idProyecto,
                                                           @Parameter(description = "Tipo de sustentación TESIS", example = "TESIS", required = true) @RequestParam TipoSustentacion tipoSustentacion) {
        return ResponseEntity.ok(proyectoService.calcularYAsignarDefinitiva(idProyecto, tipoSustentacion));
    }

    @GetMapping("/lineas-investigacion")
    public ResponseEntity<List<LineaInvestigacionDto>> obtenerLineasInvestigacion() {
        return ResponseEntity.ok(proyectoService.listarLineasInvestigacion());
    }

    @GetMapping("/grupos/lineas-investigacion")
    public ResponseEntity<List<GruposYLineasInvestigacionDto>> obtenerLineasInvestigacionPorGrupo() {
        return ResponseEntity.ok(proyectoService.listarGruposConLineas());
    }

    @Operation(
            summary = "Importar proyecto existente",
            description = "Permite importar un proyecto existente por parte de los usuarios con rol de admin o superadmin")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Datos basicos del proyecto a importar, incluyendo director y codirector",
            required = true,
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProyectoDto.class),
                    examples = @ExampleObject(
                            name = "Importar proyecto",
                            summary = "Datos de un proyecto académico para importar",
                            value = """
                    {
                      "titulo": "Sistema de gestión de proyectos académicos",
                      "pregunta": "¿Cómo mejorar la eficiencia en la gestión de proyectos de investigación?",
                      "problema": "Falta de organización y seguimiento en los proyectos actuales.",
                      "objetivoGeneral": "Desarrollar una plataforma web para gestionar proyectos académicos.",
                      "estadoActual": 5,
                      "objetivosEspecificos": [
                        {
                          "numeroOrden": "1",
                          "descripcion": "Analizar los requerimientos del sistema."
                        },
                        {
                          "numeroOrden": "2",
                          "descripcion": "Diseñar la arquitectura del sistema."
                        }
                      ],
                      "lineaInvestigacion": {
                        "id": 1
                      },
                      "usuariosAsignados": [
                        {
                          "idUsuario": 1,
                          "rol": {
                            "id": 1,
                            "nombre": "Estudiante"
                          }
                        },
                        {
                          "idUsuario": 2,
                          "rol": {
                            "id": 3,
                            "nombre": "Director"
                          }
                        },
                        {
                          "idUsuario": 3,
                          "rol": {
                            "id": 5,
                            "nombre": "Codirector"
                          }
                        }
                      ],
                      "metaODS": [
                        {
                          "id": 1,
                          "nombre": "FIN DE LA POBREZA"
                        },
                        {
                          "id": 17,
                          "nombre": "ALIANZAS PARA LOGRAR LOS OBJETIVOS"
                        }
                      ]
                    }
                    """
                    )
            )
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Proyecto importado correctamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProyectoDto.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(schema = @Schema(implementation = HttpResponse.class)))
    })
    @PostMapping("/importar")
    public ResponseEntity<ProyectoDto> importarProyectos(@RequestBody ProyectoDto proyecto) {
        return ResponseEntity.ok(proyectoService.importarProyecto(proyecto));
    }
}

