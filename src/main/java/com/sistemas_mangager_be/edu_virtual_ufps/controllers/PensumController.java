package com.sistemas_mangager_be.edu_virtual_ufps.controllers;

import com.sistemas_mangager_be.edu_virtual_ufps.exceptions.PensumNotFoundException;
import com.sistemas_mangager_be.edu_virtual_ufps.exceptions.ProgramaNotFoundException;
import com.sistemas_mangager_be.edu_virtual_ufps.services.interfaces.IPensumService;
import com.sistemas_mangager_be.edu_virtual_ufps.shared.DTOs.PensumDTO;
import com.sistemas_mangager_be.edu_virtual_ufps.shared.requests.MoodleRequest;
import com.sistemas_mangager_be.edu_virtual_ufps.shared.responses.HttpResponse;
import com.sistemas_mangager_be.edu_virtual_ufps.shared.responses.PensumSemestreResponse;
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
@RequestMapping("/api/pensums")
@RequiredArgsConstructor
@Tag(name = "Gestión de Pensums Académicos", description = "API para la administración integral de pensums académicos de SIAV UFPS. "
        +
        "Gestiona la estructura curricular por programas, creación automática de semestres, " +
        "sincronización con Moodle y administración de planes de estudio.")
public class PensumController {

    private final IPensumService pensumService;

    @Operation(summary = "Crear nuevo pensum académico", description = "Registra un pensum para un programa específico con creación automática de semestres asociados. "
            +
            "Genera automáticamente las relaciones SemestrePrograma y SemestrePensum según la cantidad " +
            "de semestres especificada. Si los semestres del programa ya existen, no los duplica.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Pensum creado exitosamente con semestres asociados", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PensumDTO.class), examples = @ExampleObject(value = """
                    {
                        "id": 1,
                        "nombre": "Pensum 2024 Maestría TIC",
                        "cantidadSemestres": 4,
                        "programaId": 1,
                        "moodleId": null
                    }
                    """))),
            @ApiResponse(responseCode = "404", description = "Programa académico no encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class), examples = @ExampleObject(value = """
                    {
                        "httpStatusCode": 404,
                        "httpStatus": "NOT_FOUND",
                        "reason": "Not Found",
                        "message": "el programa con el id 1 no fue encontrado"
                    }
                    """))),
            @ApiResponse(responseCode = "500", description = "Error interno al crear semestres", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class), examples = @ExampleObject(value = """
                    {
                        "httpStatusCode": 500,
                        "httpStatus": "INTERNAL_SERVER_ERROR",
                        "reason": "Internal Server Error",
                        "message": "Semestre no configurado en la base de datos"
                    }
                    """)))
    })
    @PostMapping("/crear")
    public ResponseEntity<PensumDTO> crearPensum(
            @Parameter(description = "Datos del pensum incluyendo programa asociado y cantidad de semestres", required = true, schema = @Schema(implementation = PensumDTO.class), example = """
                    {
                        "nombre": "Pensum 2024 Maestría TIC",
                        "cantidadSemestres": 4,
                        "programaId": 1
                    }
                    """) @RequestBody PensumDTO pensumDTO)
            throws ProgramaNotFoundException {

        PensumDTO pensum = pensumService.crearPensum(pensumDTO);
        return new ResponseEntity<>(pensum, HttpStatus.CREATED);
    }

    @Operation(summary = "Actualizar pensum académico", description = "Modifica un pensum existente con sincronización automática de semestres. "
            +
            "Si cambia la cantidad de semestres, elimina los excedentes (empezando por los de mayor número) " +
            "o crea los faltantes automáticamente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pensum actualizado exitosamente con semestres sincronizados", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class), examples = @ExampleObject(value = """
                    {
                        "httpStatusCode": 200,
                        "httpStatus": "OK",
                        "reason": "OK",
                        "message": "Pensum actualizado con exito"
                    }
                    """))),
            @ApiResponse(responseCode = "404", description = "Pensum o programa no encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class), examples = @ExampleObject(value = """
                    {
                        "httpStatusCode": 404,
                        "httpStatus": "NOT_FOUND",
                        "reason": "Not Found",
                        "message": "el pensum con el id 1 no fue encontrado"
                    }
                    """)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<HttpResponse> actualizarPensum(
            @Parameter(description = "ID único del pensum a actualizar", required = true, example = "1", schema = @Schema(type = "integer", minimum = "1")) @PathVariable Integer id,

            @Parameter(description = "Datos actualizados del pensum. Al cambiar cantidadSemestres se sincroniza automáticamente", required = true, schema = @Schema(implementation = PensumDTO.class), example = """
                    {
                        "nombre": "Pensum 2024 Maestría TIC Actualizado",
                        "cantidadSemestres": 6,
                        "programaId": 1
                    }
                    """) @RequestBody PensumDTO pensumDTO)
            throws PensumNotFoundException, ProgramaNotFoundException {

        pensumService.actualizarPensum(pensumDTO, id);
        return new ResponseEntity<>(
                new HttpResponse(HttpStatus.OK.value(), HttpStatus.OK, HttpStatus.OK.getReasonPhrase(),
                        "Pensum actualizado con exito"),
                HttpStatus.OK);
    }

    @Operation(summary = "Consultar pensum específico con semestres", description = "Obtiene información completa de un pensum incluyendo todos sus semestres asociados "
            +
            "con datos del programa, nombres y IDs de Moodle.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Información completa del pensum obtenida exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PensumSemestreResponse.class), examples = @ExampleObject(value = """
                    {
                        "id": 1,
                        "nombre": "Pensum 2024 Maestría TIC",
                        "cantidadSemestres": 4,
                        "programaId": 1,
                        "programaNombre": "Maestría en TIC aplicadas a la Educación",
                        "semestres": [
                            {
                                "id": 1,
                                "nombre": "Primer Semestre",
                                "numero": 1,
                                "moodleId": "CAT_001"
                            },
                            {
                                "id": 2,
                                "nombre": "Segundo Semestre",
                                "numero": 2,
                                "moodleId": "CAT_002"
                            }
                        ]
                    }
                    """))),
            @ApiResponse(responseCode = "404", description = "Pensum no encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class)))
    })
    @GetMapping("/{id}")
    public PensumSemestreResponse listarPensum(
            @Parameter(description = "ID único del pensum a consultar", required = true, example = "1", schema = @Schema(type = "integer", minimum = "1")) @PathVariable Integer id)
            throws PensumNotFoundException {

        return pensumService.listarPensum(id);
    }

    @Operation(summary = "Listar todos los pensums", description = "Obtiene el catálogo completo de pensums académicos registrados en el sistema "
            +
            "con información detallada de programas asociados y estructura de semestres.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista completa de pensums obtenida exitosamente", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = PensumSemestreResponse.class))))
    })
    @GetMapping("/listar")
    public List<PensumSemestreResponse> listarPensums() {
        return pensumService.listarPensums();
    }

    @Operation(summary = "Consultar pensums por programa académico", description = "Filtra todos los pensums asociados a un programa específico (Maestría TIC, Analítica de Datos, etc.). "
            +
            "Útil para gestión curricular por programa académico.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de pensums por programa obtenida exitosamente", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = PensumSemestreResponse.class)))),
            @ApiResponse(responseCode = "404", description = "Programa académico no encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class), examples = @ExampleObject(value = """
                    {
                        "httpStatusCode": 404,
                        "httpStatus": "NOT_FOUND",
                        "reason": "Not Found",
                        "message": "el programa con el id 1 no fue encontrado"
                    }
                    """)))
    })
    @GetMapping("/programa/{id}")
    public List<PensumSemestreResponse> listarPensumsPorPrograma(
            @Parameter(description = "ID del programa académico para filtrar pensums", required = true, example = "1", schema = @Schema(type = "integer", minimum = "1")) @PathVariable Integer id)
            throws ProgramaNotFoundException {

        return pensumService.listarPensumsPorPrograma(id);
    }

    @Operation(summary = "Vincular pensum con categoría de Moodle", description = "Establece la conexión entre un pensum del sistema y su categoría correspondiente en Moodle. "
            +
            "Esta vinculación es necesaria para la sincronización de la estructura curricular.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vinculación con Moodle realizada exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class), examples = @ExampleObject(value = """
                    {
                        "httpStatusCode": 200,
                        "httpStatus": "OK",
                        "reason": "OK",
                        "message": "ID de Moodle vinculado al pensum correctamente"
                    }
                    """))),
            @ApiResponse(responseCode = "404", description = "Pensum no encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class), examples = @ExampleObject(value = """
                    {
                        "httpStatusCode": 404,
                        "httpStatus": "NOT_FOUND",
                        "reason": "Not Found",
                        "message": "el pensum con id 1 no fue encontrado"
                    }
                    """)))
    })
    @PostMapping("/moodle")
    public ResponseEntity<HttpResponse> vincularMoodleId(
            @Parameter(description = "Información para vincular pensum con categoría de Moodle", required = true, schema = @Schema(implementation = MoodleRequest.class), example = """
                    {
                        "backendId": 1,
                        "moodleId": "CAT_PENSUM_001"
                    }
                    """) @RequestBody MoodleRequest moodleRequest)
            throws PensumNotFoundException {

        pensumService.vincularMoodleId(moodleRequest);
        return new ResponseEntity<>(
                new HttpResponse(HttpStatus.OK.value(), HttpStatus.OK, HttpStatus.OK.getReasonPhrase(),
                        "ID de Moodle vinculado al pensum correctamente"),
                HttpStatus.OK);
    }

    @Operation(summary = "Vincular semestre de programa con categoría de Moodle", description = "Establece la conexión entre un semestre específico de un programa y su categoría "
            +
            "correspondiente en Moodle. Esta vinculación organiza la estructura jerárquica de categorías.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vinculación de semestre con Moodle realizada exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class), examples = @ExampleObject(value = """
                    {
                        "httpStatusCode": 200,
                        "httpStatus": "OK",
                        "reason": "OK",
                        "message": "ID de Moodle vinculado al semestre correctamente"
                    }
                    """))),
            @ApiResponse(responseCode = "404", description = "Semestre del programa no encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class), examples = @ExampleObject(value = """
                    {
                        "httpStatusCode": 404,
                        "httpStatus": "NOT_FOUND",
                        "reason": "Not Found",
                        "message": "el semestre del pensum con id 1 no fue encontrado"
                    }
                    """)))
    })
    @PostMapping("/semestre/moodle")
    public ResponseEntity<HttpResponse> vincularSemestreMoodleId(
            @Parameter(description = "Información para vincular semestre-programa con categoría de Moodle", required = true, schema = @Schema(implementation = MoodleRequest.class), example = """
                    {
                        "backendId": 1,
                        "moodleId": "CAT_SEM1_001"
                    }
                    """) @RequestBody MoodleRequest moodleRequest)
            throws ProgramaNotFoundException {

        pensumService.vincularSemestreMoodleId(moodleRequest);
        return new ResponseEntity<>(
                new HttpResponse(
                        HttpStatus.OK.value(),
                        HttpStatus.OK,
                        HttpStatus.OK.getReasonPhrase(),
                        "ID de Moodle vinculado al semestre correctamente"),
                HttpStatus.OK);
    }
}
