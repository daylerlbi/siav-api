package com.sistemas_mangager_be.edu_virtual_ufps.controllers;

import com.sistemas_mangager_be.edu_virtual_ufps.entities.TipoPrograma;
import com.sistemas_mangager_be.edu_virtual_ufps.exceptions.ProgramaExistsException;
import com.sistemas_mangager_be.edu_virtual_ufps.exceptions.ProgramaNotFoundException;
import com.sistemas_mangager_be.edu_virtual_ufps.repositories.TipoProgramaRepository;
import com.sistemas_mangager_be.edu_virtual_ufps.services.interfaces.IProgramaService;
import com.sistemas_mangager_be.edu_virtual_ufps.shared.DTOs.ProgramaDTO;
import com.sistemas_mangager_be.edu_virtual_ufps.shared.requests.MoodleRequest;
import com.sistemas_mangager_be.edu_virtual_ufps.shared.responses.HttpResponse;
import com.sistemas_mangager_be.edu_virtual_ufps.shared.responses.SemestreProgramaResponse;
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
@RequestMapping("/api/programas")
@RequiredArgsConstructor
@Tag(name = "Gestión de Programas Académicos", description = "API para la administración integral de programas académicos de SIAV UFPS. "
                + "Gestiona creación, actualización, consultas y vinculación con plataforma Moodle para programas de pregrado y posgrado.")
public class ProgramaController {

        private final IProgramaService programaService;
        private final TipoProgramaRepository tipoProgramaRepository;

        @Operation(summary = "Crear nuevo programa académico", description = "Registra un nuevo programa académico en el sistema validando unicidad de código. "
                        + "Asigna automáticamente el semestre actual y tipo de programa basado en el flag esPosgrado.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Programa creado exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProgramaDTO.class), examples = @ExampleObject(value = """
                                        {
                                            "id": 3,
                                            "nombre": "Ingenieria Electronica",
                                            "codigo": "115",
                                            "esPosgrado": true,
                                            "moodleId": null,
                                            "historicoMoodleId": null,
                                            "semestreActual": "2025-I"
                                        }
                                        """))),
                        @ApiResponse(responseCode = "409", description = "Código de programa ya existe", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class), examples = @ExampleObject(value = """
                                                            {
                                            "httpStatus": "CONFLICT",
                                            "reason": "Conflict",
                                            "message": "El código de programa 115 ya esta registrado en el sistema",
                                            "httpStatusCode": 409
                                        }
                                                            """))),
                        @ApiResponse(responseCode = "500", description = "Error en el formato JSON del request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class), examples = @ExampleObject(value = """
                                        {
                                            "httpStatus": "INTERNAL_SERVER_ERROR",
                                            "reason": "Internal Server Error",
                                            "message": "JSON PARSE ERROR: UNEXPECTED CHARACTER ('}' (CODE 125)): EXPECTED A VALUE",
                                            "httpStatusCode": 500
                                        }
                                        """)))
        })
        @PostMapping("/crear")
        public ResponseEntity<ProgramaDTO> crearPrograma(
                        @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Información del programa académico a crear. Todos los campos son obligatorios.", required = true, content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProgramaDTO.class), examples = @ExampleObject(name = "Ejemplo de creación de programa", value = """
                                        {
                                            "codigo": "115",
                                            "nombre": "Ingenieria Electronica",
                                            "esPosgrado": true
                                        }
                                        """))) @RequestBody ProgramaDTO programaDTO)
                        throws ProgramaExistsException {

                ProgramaDTO programa = programaService.crearPrograma(programaDTO);
                return new ResponseEntity<>(programa, HttpStatus.OK);
        }

        @Operation(summary = "Vincular programa con Moodle", description = "Establece la vinculación entre un programa existente y su ID de categoría en la plataforma Moodle actual. "
                        + "El backendId corresponde al ID del programa en el sistema y el moodleId es el ID de la categoría creada en Moodle.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Vinculación con Moodle realizada exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class), examples = @ExampleObject(value = """
                                        {
                                            "httpStatus": "OK",
                                            "reason": "OK",
                                            "message": "Vinculacion con moodle realizada con exito",
                                            "httpStatusCode": 200
                                        }
                                        """))),
                        @ApiResponse(responseCode = "404", description = "Programa no encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class), examples = @ExampleObject(value = """
                                        {
                                            "httpStatus": "NOT_FOUND",
                                            "reason": "Not Found",
                                            "message": "el programa con el id 3 no fue encontrado",
                                            "httpStatusCode": 404
                                        }
                                        """)))
        })
        @PostMapping("/moodle")
        public ResponseEntity<HttpResponse> vincularMoodle(
                        @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Información para vincular programa con plataforma Moodle. "
                                        +
                                        "backendId: ID del programa en el sistema, " +
                                        "moodleId: ID de la categoría creada en Moodle", required = true, content = @Content(mediaType = "application/json", schema = @Schema(implementation = MoodleRequest.class), examples = @ExampleObject(name = "Ejemplo de vinculación", value = """
                                                        {
                                                            "backendId": 1,
                                                            "moodleId": "200"
                                                        }
                                                        """))) @RequestBody MoodleRequest moodleRequest)
                        throws ProgramaNotFoundException, ProgramaExistsException {

                programaService.vincularMoodleId(moodleRequest);
                return new ResponseEntity<>(
                                new HttpResponse(HttpStatus.OK.value(), HttpStatus.OK, HttpStatus.OK.getReasonPhrase(),
                                                "Vinculacion con moodle realizada con exito"),
                                HttpStatus.OK);
        }

        @Operation(summary = "Vincular programa con Moodle histórico", description = "Establece la vinculación entre un programa y su ID de categoría histórica en Moodle para cursos anteriores. "
                        + "El backendId corresponde al ID del programa en el sistema y el moodleId es el ID de la categoría del histórico del programa creado en Moodle.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Vinculación histórica con Moodle realizada exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class), examples = @ExampleObject(value = """
                                        {
                                            "httpStatus": "OK",
                                            "reason": "OK",
                                            "message": "Vinculacion del historico con moodle realizada con exito",
                                            "httpStatusCode": 200
                                        }
                                        """))),
                        @ApiResponse(responseCode = "404", description = "Programa no encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class), examples = @ExampleObject(value = """
                                        {
                                            "httpStatus": "NOT_FOUND",
                                            "reason": "Not Found",
                                            "message": "el programa con el id 3 no fue encontrado",
                                            "httpStatusCode": 404
                                        }
                                        """)))
        })
        @PostMapping("/historico/moodle")
        public ResponseEntity<HttpResponse> vincularHistoricoMoodle(
                        @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Información para vincular programa con histórico Moodle. "
                                        +
                                        "backendId: ID del programa en el sistema, " +
                                        "moodleId: ID de la categoría del histórico del programa creado en Moodle", required = true, content = @Content(mediaType = "application/json", schema = @Schema(implementation = MoodleRequest.class), examples = @ExampleObject(name = "Ejemplo de vinculación histórica", value = """
                                                        {
                                                            "backendId": 1,
                                                            "moodleId": "5"
                                                        }
                                                        """))) @RequestBody MoodleRequest moodleRequest)
                        throws ProgramaNotFoundException, ProgramaExistsException {

                programaService.vincularHistoricoMoodleId(moodleRequest);
                return new ResponseEntity<>(
                                new HttpResponse(HttpStatus.OK.value(), HttpStatus.OK, HttpStatus.OK.getReasonPhrase(),
                                                "Vinculacion del historico con moodle realizada con exito"),
                                HttpStatus.OK);
        }

        @Operation(summary = "Listar todos los programas académicos", description = "Obtiene el catálogo completo de programas académicos registrados en el sistema "
                        + "incluyendo información de pregrado y posgrado.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Lista de programas obtenida exitosamente", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ProgramaDTO.class)), examples = @ExampleObject(value = """
                                        [
                                            {
                                                "id": 1,
                                                "nombre": "Tecnologia Ejemplo",
                                                "codigo": "123",
                                                "esPosgrado": false,
                                                "moodleId": "746",
                                                "historicoMoodleId": "747",
                                                "semestreActual": "2025-I"
                                            },
                                            {
                                                "id": 2,
                                                "nombre": "Maestria Ejemplo",
                                                "codigo": "234",
                                                "esPosgrado": true,
                                                "moodleId": "748",
                                                "historicoMoodleId": "749",
                                                "semestreActual": "2025-I"
                                            }
                                        ]
                                        """)))
        })
        @GetMapping("/listar")
        public ResponseEntity<List<ProgramaDTO>> listarProgramas() {
                return new ResponseEntity<>(programaService.listarProgramas(), HttpStatus.OK);
        }

        @Operation(summary = "Consultar programa específico", description = "Obtiene información completa de un programa académico específico incluyendo "
                        + "datos básicos, vinculaciones Moodle y semestre actual.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Información del programa obtenida exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProgramaDTO.class), examples = @ExampleObject(value = """
                                                            {
                                          "id": 1,
                                          "nombre": "Posgrado prueba",
                                          "codigo": "248",
                                          "esPosgrado": true,
                                          "moodleId": "206",
                                          "historicoMoodleId": "207",
                                          "semestreActual": null
                                        }
                                                            """))),
                        @ApiResponse(responseCode = "404", description = "Programa no encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class), examples = @ExampleObject(value = """
                                                            {
                                          "httpStatus": "NOT_FOUND",
                                          "reason": "Not Found",
                                          "message": "el programa con el id 3 no fue encontrado",
                                          "httpStatusCode": 404
                                        }
                                                            """)))
        })
        @GetMapping("/{id}")
        public ResponseEntity<ProgramaDTO> listarPrograma(
                        @Parameter(description = "ID único del programa a consultar", required = true, example = "1", schema = @Schema(type = "integer", minimum = "1")) @PathVariable Integer id)
                        throws ProgramaNotFoundException {

                ProgramaDTO programaDTO = programaService.listarPrograma(id);
                return new ResponseEntity<>(programaDTO, HttpStatus.OK);
        }

        @Operation(summary = "Consultar programa por código", description = "Busca un programa académico específico utilizando su código único. "
                        + "Útil para búsquedas rápidas y validaciones.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Programa encontrado exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProgramaDTO.class), examples = @ExampleObject(value = """
                                        {
                                            "id": 2,
                                            "nombre": "Maestria Ejemplo",
                                            "codigo": "234",
                                            "esPosgrado": true,
                                            "moodleId": "748",
                                            "historicoMoodleId": "749",
                                            "semestreActual": "2025-I"
                                        }
                                        """))),
                        @ApiResponse(responseCode = "404", description = "Programa con el código especificado no encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class), examples = @ExampleObject(value = """
                                        {
                                            "httpStatus": "NOT_FOUND",
                                            "reason": "Not Found",
                                            "message": "Programa con código 215 no encontrado",
                                            "httpStatusCode": 404
                                        }
                                        """)))
        })
        @GetMapping("/codigo/{codigo}")
        public ResponseEntity<ProgramaDTO> listarProgramaPorCodigo(
                        @Parameter(description = "Código único del programa a buscar", required = true, example = "234", schema = @Schema(type = "string")) @PathVariable String codigo)
                        throws ProgramaNotFoundException {

                ProgramaDTO programaDTO = programaService.listarProgramaPorCodigo(codigo);
                return new ResponseEntity<>(programaDTO, HttpStatus.OK);
        }

        @Operation(summary = "Consultar semestres de programa", description = "Obtiene todos los semestres asociados a un programa académico específico "
                        + "incluyendo información de vinculación con Moodle por semestre.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Semestres del programa obtenidos exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SemestreProgramaResponse.class), examples = @ExampleObject(value = """
                                                                {
                                            "id": 1,
                                            "nombre": "Tecnologia Ejemplo",
                                            "codigo": "123",
                                            "semestres": [
                                                {
                                                    "id": 1,
                                                    "nombre": "Semestre I",
                                                    "numero": 1,
                                                    "moodleId": "750"
                                                },
                                                {
                                                    "id": 2,
                                                    "nombre": "Semestre II",
                                                    "numero": 2,
                                                    "moodleId": "751"
                                                },
                                                {
                                                    "id": 3,
                                                    "nombre": "Semestre III",
                                                    "numero": 3,
                                                    "moodleId": "752"
                                                },
                                                {
                                                    "id": 4,
                                                    "nombre": "Semestre IV",
                                                    "numero": 4,
                                                    "moodleId": "753"
                                                },
                                                {
                                                    "id": 5,
                                                    "nombre": "Semestre V",
                                                    "numero": 5,
                                                    "moodleId": "754"
                                                },
                                                {
                                                    "id": 6,
                                                    "nombre": "Semestre VI",
                                                    "numero": 6,
                                                    "moodleId": "755"
                                                }
                                            ],
                                            "moodleId": "746",
                                            "esPosgrado": false
                                        }
                                                                                                    """))),
                        @ApiResponse(responseCode = "404", description = "Programa con el código especificado no encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class), examples = @ExampleObject(value = """
                                        {
                                            "httpStatus": "NOT_FOUND",
                                            "reason": "Not Found",
                                            "message": "el programa con el id 3 no fue encontrado",
                                            "httpStatusCode": 404
                                        }
                                        """)))
        })
        @GetMapping("/{id}/semestres")
        public ResponseEntity<SemestreProgramaResponse> listarSemestresPorPrograma(
                        @Parameter(description = "ID del programa para consultar sus semestres", required = true, example = "1", schema = @Schema(type = "integer", minimum = "1")) @PathVariable Integer id)
                        throws ProgramaNotFoundException {

                SemestreProgramaResponse semestreProgramaResponse = programaService.listarSemestresPorPrograma(id);
                return new ResponseEntity<>(semestreProgramaResponse, HttpStatus.OK);
        }

        @Operation(summary = "Actualizar programa académico", description = "Modifica la información de un programa existente validando unicidad de código "
                        + "solo cuando este cambie. Actualiza datos básicos del programa.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Programa actualizado exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class), examples = @ExampleObject(value = """
                                        {
                                            "httpStatus": "OK",
                                            "reason": "OK",
                                            "message": " Programa actualizado con exito",
                                            "httpStatusCode": 200
                                        }
                                        """))),
                        @ApiResponse(responseCode = "404", description = "Programa no encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class), examples = @ExampleObject(value = """
                                        {
                                            "httpStatus": "NOT_FOUND",
                                            "reason": "Not Found",
                                            "message": "el programa con el id 3 no fue encontrado",
                                            "httpStatusCode": 404
                                        }
                                        """)))
        })
        @PutMapping("/{id}")
        public ResponseEntity<HttpResponse> actualizarPrograma(
                        @Parameter(description = "ID único del programa a actualizar", required = true, example = "1", schema = @Schema(type = "integer", minimum = "1")) @PathVariable Integer id,

                        @Parameter(description = "Datos actualizados del programa", required = true, schema = @Schema(implementation = ProgramaDTO.class), example = """
                                        {
                                            "nombre": "Posgrado prueba",
                                            "codigo": "248",
                                            "esPosgrado": true,
                                            "moodleId": "206",
                                            "historicoMoodleId": "207",
                                            "semestreActual": "2025-I"
                                        }
                                        """) @RequestBody ProgramaDTO programaDTO)
                        throws ProgramaNotFoundException, ProgramaExistsException {

                programaService.actualizarPrograma(programaDTO, id);
                return new ResponseEntity<>(
                                new HttpResponse(HttpStatus.OK.value(), HttpStatus.OK, HttpStatus.OK.getReasonPhrase(),
                                                " Programa actualizado con exito"),
                                HttpStatus.OK);
        }

        @Operation(summary = "Listar tipos de programa", description = "Obtiene el catálogo de tipos de programa disponibles (Pregrado, Posgrado) "
                        + "para clasificación de programas académicos.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Lista de tipos de programa obtenida exitosamente", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = TipoPrograma.class)), examples = @ExampleObject(value = """
                                        [
                                            {
                                                "id": 1,
                                                "nombre": "Tecnologia",
                                                "moodleId": "307"
                                            },
                                            {
                                                "id": 2,
                                                "nombre": "Maestria",
                                                "moodleId": "306"
                                            }
                                        ]
                                        """)))
        })
        @GetMapping("/tipos-programa")
        public ResponseEntity<List<TipoPrograma>> listarTiposPrograma() {
                List<TipoPrograma> tiposPrograma = tipoProgramaRepository.findAll();
                return new ResponseEntity<>(tiposPrograma, HttpStatus.OK);
        }
}
