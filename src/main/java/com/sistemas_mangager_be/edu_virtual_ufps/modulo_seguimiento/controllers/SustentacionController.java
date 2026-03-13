package com.sistemas_mangager_be.edu_virtual_ufps.modulo_seguimiento.controllers;

import com.sistemas_mangager_be.edu_virtual_ufps.modulo_seguimiento.dtos.CriterioEvaluacionDto;
import com.sistemas_mangager_be.edu_virtual_ufps.modulo_seguimiento.dtos.SustentacionDto;
import com.sistemas_mangager_be.edu_virtual_ufps.modulo_seguimiento.dtos.SustentacionEvaluadorDto;
import com.sistemas_mangager_be.edu_virtual_ufps.modulo_seguimiento.entities.enums.TipoSustentacion;
import com.sistemas_mangager_be.edu_virtual_ufps.modulo_seguimiento.services.SustentacionService;
import com.sistemas_mangager_be.edu_virtual_ufps.shared.responses.HttpResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
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
@RequestMapping("/api/sustentaciones")
@Tag(name = "Gestión de Sustentaciones", description = "API para la administración de sustentaciones." +
        " Permite crear, actualizar, eliminar y consultar sustentaciones de proyectos académicos. " +
        " Además, permite asignar evaluadores ,criterios de evaluación y asignar sus respectivas evaluaciones.")
public class SustentacionController {

    private final SustentacionService sustentacionService;

    public SustentacionController(SustentacionService sustentacionService) {
        this.sustentacionService = sustentacionService;
    }

    @Operation(
            summary = "Crear sustentación para un proyecto",
            description = "Registra una nueva sustentación con sus detalles como tipo, fecha, hora, lugar y proyecto asociado.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = SustentacionDto.class),
                            examples = @ExampleObject(
                                    name = "Ejemplo de sustentación",
                                    summary = "Sustentación de tipo TESIS en modalidad virtual",
                                    value = """
                {
                  "tipoSustentacion": "TESIS",
                  "fecha": "2025-05-15",
                  "hora": "10:30:00",
                  "horaFin": "11:30:00",
                  "lugar": "https://streamyard.com/syveiyp6wd",
                  "descripcion": "Sala 2: Zulia Segundo Encuentro  EDUTICLAB 2024-II",
                  "sustentacionExterna": false,
                  "idProyecto": 19
                }
                """
                            )
                    )
            )
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sustentación creada correctamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = SustentacionDto.class))),
            @ApiResponse(responseCode = "500", description = "Error del servidor",
                    content = @Content(schema = @Schema(implementation = HttpResponse.class)))
    })
    @PostMapping
    public ResponseEntity<SustentacionDto> crearSustentacion(@RequestBody SustentacionDto dto) {
        return ResponseEntity.ok(sustentacionService.crearSustentacion(dto));
    }

    @Operation(
            summary = "Obtener sustentación de un proyecto",
            description = "Retorna los datos de una sustentación específica según el proyecto y el tipo de sustentación."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sustentación encontrada",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = SustentacionDto.class))),
            @ApiResponse(responseCode = "500", description = "Error del servidor",
                    content = @Content(schema = @Schema(implementation = HttpResponse.class)))
    })
    @Parameters({
            @Parameter(name = "idProyecto", description = "ID del proyecto al que pertenece la sustentación", required = true, example = "19"),
            @Parameter(name = "tipoSustentacion", description = "Tipo de sustentación (ej: TESIS, ANTEPROYECTO)", required = true,
                    example = "TESIS",
                    schema = @Schema(implementation = TipoSustentacion.class)
            )
    })
    @GetMapping("/seleccionar")
    public ResponseEntity<SustentacionDto> obtenerSustentacion(@RequestParam Integer idProyecto,
                                                               @RequestParam TipoSustentacion tipoSustentacion) {
        return ResponseEntity.ok(sustentacionService.obtenerSustentacion(idProyecto, tipoSustentacion));
    }

    @Operation(
            summary = "Listar sustentaciones",
            description = "Retorna la lista de todas las sustentaciones registradas o filtradas por proyecto."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado de sustentaciones obtenido correctamente",
                    content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = SustentacionDto.class)))),
            @ApiResponse(responseCode = "500", description = "Error del servidor",
                    content = @Content(schema = @Schema(implementation = HttpResponse.class)))
    })
    @Parameter(name = "idProyecto", description = "ID del proyecto para filtrar las sustentaciones (opcional)", example = "19")
    @GetMapping
    public ResponseEntity<List<SustentacionDto>> listarSustentaciones(@RequestParam(required = false) Integer idProyecto) {
        return ResponseEntity.ok(sustentacionService.listarSustentaciones(idProyecto));
    }

    @Operation(
            summary = "Actualizar una sustentación",
            description = "Actualiza los datos de una sustentación existente, como la fecha, hora, lugar o si es externa.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = SustentacionDto.class),
                            examples = @ExampleObject(
                                    name = "Ejemplo de actualización",
                                    summary = "Datos actualizados de la sustentación",
                                    value = """
                {
                  "fecha": "2025-05-25",
                  "hora": "16:30:00",
                  "horaFin": "17:30:00",
                  "lugar": "Sala 2: Zulia Segundo Encuentro  EDUTICLAB 2024-II",
                  "descripcion": "https://streamyard.com/syveiyp6wd",
                  "sustentacionExterna": true
                }
                """
                            )
                    )
            )
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sustentación actualizada correctamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = SustentacionDto.class))),
            @ApiResponse(responseCode = "500", description = "Error del servidor",
                    content = @Content(schema = @Schema(implementation = HttpResponse.class)))
    })
    @Parameter(name = "id", description = "ID de la sustentación a actualizar", required = true, example = "15")
    @PutMapping("/{id}")
    public ResponseEntity<SustentacionDto> actualizarSustentacion(@PathVariable Integer id, @RequestBody SustentacionDto dto) {
        return ResponseEntity.ok(sustentacionService.actualizarSustentacion(id, dto));
    }

    @Operation(
            summary = "Eliminar una sustentación",
            description = "Elimina una sustentación existente a partir de su ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Sustentación eliminada correctamente"),
            @ApiResponse(responseCode = "500", description = "Error del servidor",
                    content = @Content(schema = @Schema(implementation = HttpResponse.class)))
    })
    @Parameter(name = "id", description = "ID de la sustentación a eliminar", required = true, example = "15")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarSustentacion(@PathVariable Integer id) {
        sustentacionService.eliminarSustentacion(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Asignar evaluador a sustentación",
            description = "Asocia un evaluador (usuario) a una sustentación específica, indicando si es jurado externo.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = SustentacionEvaluadorDto.class),
                            examples = @ExampleObject(
                                    name = "Ejemplo de asignación",
                                    summary = "Asignar jurado externo a sustentación",
                                    value = """
                {
                  "idSustentacion": 20,
                  "idUsuario": 3,
                  "juradoExterno": true
                }
                """
                            )
                    )
            )
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Evaluador asignado correctamente"),
            @ApiResponse(responseCode = "500", description = "Error del servidor",
                    content = @Content(schema = @Schema(implementation = HttpResponse.class)))
    })
    @PostMapping("/evaluador")
    public ResponseEntity<Void> asignarEvaluadorASustentacion(@RequestBody SustentacionEvaluadorDto dto) {
        sustentacionService.asignarEvaluadorASustentacion(dto);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Eliminar evaluador de una sustentación",
            description = "Desasigna un evaluador (jurado) previamente asociado a una sustentación específica."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Evaluador eliminado correctamente"),
            @ApiResponse(responseCode = "500", description = "Error del servidor",
                    content = @Content(schema = @Schema(implementation = HttpResponse.class)))
    })
    @Parameters({
            @Parameter(name = "idSustentacion", description = "ID de la sustentación", required = true, example = "20"),
            @Parameter(name = "idEvaluador", description = "ID del evaluador (usuario) que se desea eliminar", required = true, example = "3")
    })
    @DeleteMapping("/{idSustentacion}/evaluador/{idEvaluador}")
    public ResponseEntity<Void> eliminarEvaluadorDeSustentacion(@PathVariable Integer idSustentacion, @PathVariable Integer idEvaluador) {
        sustentacionService.eliminarEvaluadorDeSustentacion(idSustentacion, idEvaluador);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Evaluar sustentación",
            description = "Permite a un evaluador asignado calificar una sustentación con nota y observaciones.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = SustentacionEvaluadorDto.class),
                            examples = @ExampleObject(
                                    name = "Ejemplo de evaluación",
                                    summary = "Evaluación por parte de un evaluador",
                                    value = """
                {
                  "idSustentacion": 18,
                  "idUsuario": 2,
                  "observaciones": "Bien hecho",
                  "nota": 5.0
                }
                """
                            )
                    )
            )
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Evaluación registrada correctamente"),
            @ApiResponse(responseCode = "500", description = "Error del servidor",
                    content = @Content(schema = @Schema(implementation = HttpResponse.class)))
    })
    @PutMapping("/evaluar-sustentacion")
    public ResponseEntity<Void> evaluarSustentacion(@RequestBody SustentacionEvaluadorDto dto) {
        sustentacionService.evaluarSustentacion(dto);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Agregar criterio de evaluación",
            description = "Permite agregar un nuevo criterio de evaluación a una sustentación existente.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CriterioEvaluacionDto.class),
                            examples = @ExampleObject(
                                    name = "Ejemplo de Criterio",
                                    summary = "Criterio para evaluar entrega en PDF",
                                    value = """
                {
                  "descripcion": "PDF",
                  "idSustentacion": 13
                }
                """
                            )
                    )
            )
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Criterio agregado correctamente"),
            @ApiResponse(responseCode = "500", description = "Error del servidor",
                    content = @Content(schema = @Schema(implementation = HttpResponse.class)))
    })
    @PostMapping("/agregar-criterio")
    public ResponseEntity<Void> agregarCriterio(@RequestBody CriterioEvaluacionDto criterioEvaluacionDto) {
        sustentacionService.agregarCriterioEvaluacion(criterioEvaluacionDto);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Actualizar criterio de evaluación",
            description = "Actualiza la descripción de un criterio de evaluación ya existente.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CriterioEvaluacionDto.class),
                            examples = @ExampleObject(
                                    name = "Ejemplo de actualización",
                                    summary = "Actualizar descripción del criterio",
                                    value = """
                {
                  "descripcion": "PDF ANTEPROYECTO"
                }
                """
                            )
                    )
            )
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Criterio actualizado correctamente"),
            @ApiResponse(responseCode = "500", description = "Error del servidor",
                    content = @Content(schema = @Schema(implementation = HttpResponse.class)))
    })
    @Parameter(name = "idCriterio", description = "ID del criterio que se desea actualizar", required = true, example = "5")
    @PutMapping("/actualizar-criterio/{idCriterio}")
    public ResponseEntity<Void> actualizarCriterio(@PathVariable Integer idCriterio, @RequestBody CriterioEvaluacionDto criterioEvaluacionDto) {
        sustentacionService.actualizarCriterioEvaluacion(idCriterio, criterioEvaluacionDto);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Eliminar criterio de evaluación",
            description = "Elimina un criterio de evaluación asociado a una sustentación."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Criterio eliminado correctamente"),
            @ApiResponse(responseCode = "500", description = "Error del servidor",
                    content = @Content(schema = @Schema(implementation = HttpResponse.class)))
    })
    @Parameter(name = "idCriterio", description = "ID del criterio que se desea eliminar", required = true, example = "5")
    @DeleteMapping("/eliminar-criterio/{idCriterio}")
    public ResponseEntity<Void> eliminarCriterio(@PathVariable Integer idCriterio) {
        sustentacionService.eliminarCriterioEvaluacion(idCriterio);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Marcar sustentación como realizada",
            description = "Permite marcar una sustentación como realizada una vez que ha sido presentada."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sustentación marcada como realizada correctamente"),
            @ApiResponse(responseCode = "500", description = "Error del servidor",
                    content = @Content(schema = @Schema(implementation = HttpResponse.class)))
    })
    @Parameter(name = "idSustentacion", description = "ID de la sustentación que se desea marcar como realizada", required = true, example = "12")
    @PostMapping("/realizada/{idSustentacion}")
    public ResponseEntity<Void> marcarSustentacionComoRealizada(@PathVariable Integer idSustentacion) {
        sustentacionService.marcarSustentacionRealizada(idSustentacion);
        return ResponseEntity.ok().build();
    }

}
