package com.sistemas_mangager_be.edu_virtual_ufps.controllers;

import com.sistemas_mangager_be.edu_virtual_ufps.exceptions.CohorteNotFoundException;
import com.sistemas_mangager_be.edu_virtual_ufps.services.interfaces.ICohorteService;
import com.sistemas_mangager_be.edu_virtual_ufps.shared.DTOs.CohorteDTO;
import com.sistemas_mangager_be.edu_virtual_ufps.shared.DTOs.CohortePorCarreraDTO;
import com.sistemas_mangager_be.edu_virtual_ufps.shared.responses.CohorteResponse;
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
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cohortes")
@RequiredArgsConstructor
@Tag(name = "Gestión de Cohortes", description = "API para la administración completa de cohortes académicas de SIAV UFPS. "
        +
        "Permite crear, consultar, actualizar y listar cohortes con sus respectivos grupos asociados.")
public class CohorteController {

    private final ICohorteService cohorteService;

    @Operation(summary = "Crear nueva cohorte académica", description = "Registra una nueva cohorte en el sistema y crea automáticamente dos grupos: 'Grupo A' y 'Grupo B'. "
            +
            "La cohorte queda disponible para asignación de estudiantes y gestión académica.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cohorte creada exitosamente con grupos automáticos", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class), examples = @ExampleObject(value = """
                    {
                        "httpStatusCode": 200,
                        "httpStatus": "OK",
                        "reason": "OK",
                        "message": "Cohorte creada con éxito"
                    }
                    """))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o cohorte duplicada", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class)))
    })
    @PostMapping("/crear")
    public ResponseEntity<HttpResponse> crearCohorte(
            @Parameter(description = "Datos de la nueva cohorte a crear", required = true, schema = @Schema(implementation = CohorteDTO.class), example = """
                    {
                        "nombre": "Cohorte 2024-1 Maestría TIC",
                        "descripcion": "Primera cohorte 2024 para Maestría en TIC"
                    }
                    """) @RequestBody CohorteDTO cohorteDTO) {

        cohorteService.crearCohorte(cohorteDTO);
        return new ResponseEntity<>(
                new HttpResponse(HttpStatus.OK.value(), HttpStatus.OK, HttpStatus.OK.getReasonPhrase(),
                        " Cohorte creada con exito"),
                HttpStatus.OK);
    }

    @Operation(summary = "Consultar cohorte por ID", description = "Obtiene información completa de una cohorte específica incluyendo datos básicos y grupos asociados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cohorte encontrada exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CohorteResponse.class), examples = @ExampleObject(value = """
                    {
                        "id": 1,
                        "nombre": "Cohorte 2024-1 Maestría TIC",
                        "fechaCreacion": "2024-01-15T08:00:00",
                        "cohortesGrupos": [
                            {
                                "id": 1,
                                "nombre": "Cohorte 2024-1 Maestría TIC Grupo A"
                            },
                            {
                                "id": 2,
                                "nombre": "Cohorte 2024-1 Maestría TIC Grupo B"
                            }
                        ]
                    }
                    """))),
            @ApiResponse(responseCode = "404", description = "Cohorte no encontrada", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class))),
            @ApiResponse(responseCode = "400", description = "ID inválido", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<CohorteResponse> listarCohorte(
            @Parameter(description = "ID único de la cohorte a consultar", required = true, example = "1", schema = @Schema(type = "integer", minimum = "1")) @PathVariable Integer id)
            throws CohorteNotFoundException {

        CohorteResponse cohorte = cohorteService.listarCohorte(id);
        return new ResponseEntity<>(cohorte, HttpStatus.OK);
    }

    @Operation(summary = "Listar todas las cohortes", description = "Obtiene listado completo de todas las cohortes registradas con información básica y grupos asociados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de cohortes obtenida exitosamente", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = CohorteResponse.class)), examples = @ExampleObject(value = """
                    [
                        {
                            "id": 1,
                            "nombre": "Cohorte 2024-1 Maestría TIC",
                            "fechaCreacion": "2024-01-15T08:00:00",
                            "cohortesGrupos": [
                                {
                                    "id": 1,
                                    "nombre": "Cohorte 2024-1 Maestría TIC Grupo A"
                                }
                            ]
                        }
                    ]
                    """))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class)))
    })
    @GetMapping("/listar")
    public ResponseEntity<List<CohorteResponse>> listarCohortes() {
        List<CohorteResponse> cohortes = cohorteService.listarCohortes();
        return new ResponseEntity<>(cohortes, HttpStatus.OK);
    }

    @Operation(summary = "Actualizar cohorte existente", description = "Modifica datos de una cohorte. Si cambia el nombre, actualiza automáticamente nombres de grupos asociados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cohorte actualizada exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class), examples = @ExampleObject(value = """
                    {
                        "httpStatusCode": 200,
                        "httpStatus": "OK",
                        "reason": "OK",
                        "message": "Cohorte actualizada con éxito"
                    }
                    """))),
            @ApiResponse(responseCode = "404", description = "Cohorte no encontrada", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o ID incorrecto", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<HttpResponse> actualizarCohorte(
            @Parameter(description = "ID de la cohorte a actualizar", required = true, example = "1", schema = @Schema(type = "integer", minimum = "1")) @PathVariable Integer id,

            @Parameter(description = "Nuevos datos para la cohorte", required = true, schema = @Schema(implementation = CohorteDTO.class), example = """
                    {
                        "nombre": "Cohorte 2024-1 Maestría TIC Actualizada",
                        "descripcion": "Descripción actualizada"
                    }
                    """) @RequestBody CohorteDTO cohorteDTO)
            throws CohorteNotFoundException {

        cohorteService.actualizarCohorte(cohorteDTO, id);
        return new ResponseEntity<>(
                new HttpResponse(HttpStatus.OK.value(), HttpStatus.OK, HttpStatus.OK.getReasonPhrase(),
                        " Cohorte actualizada con exito"),
                HttpStatus.OK);
    }

    @Operation(summary = "Listar cohortes por programa académico", description = "Obtiene vista estructurada de cohortes organizadas por programa académico con grupos asociados. "
            +
            "Ideal para reportes y análisis por carrera.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista por programa académico obtenida exitosamente", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = CohortePorCarreraDTO.class)), examples = @ExampleObject(value = """
                    [
                        {
                            "programa": {
                                "id": 1,
                                "nombre": "Maestría en TIC aplicadas a la Educación",
                                "codigo": "MTIC"
                            },
                            "cohortes": [
                                {
                                    "id": 1,
                                    "nombre": "Cohorte 2024-1 Maestría TIC",
                                    "fechaCreacion": "2024-01-15T08:00:00",
                                    "cohortesGrupos": [
                                        {
                                            "id": 1,
                                            "nombre": "Cohorte 2024-1 Maestría TIC Grupo A"
                                        }
                                    ]
                                }
                            ]
                        }
                    ]
                    """))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class)))
    })
    @GetMapping("/por-carrera")
    public ResponseEntity<List<CohortePorCarreraDTO>> listarCohortesPorCarreraConGrupos() {
        List<CohortePorCarreraDTO> resultado = cohorteService.listarCohortesPorCarreraConGrupos();
        return ResponseEntity.ok(resultado);
    }
}
