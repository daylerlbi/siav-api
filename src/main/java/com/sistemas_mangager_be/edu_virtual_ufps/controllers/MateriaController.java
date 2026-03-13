package com.sistemas_mangager_be.edu_virtual_ufps.controllers;

import com.sistemas_mangager_be.edu_virtual_ufps.exceptions.MateriaExistsException;
import com.sistemas_mangager_be.edu_virtual_ufps.exceptions.MateriaNotFoundException;
import com.sistemas_mangager_be.edu_virtual_ufps.exceptions.PensumNotFoundException;
import com.sistemas_mangager_be.edu_virtual_ufps.exceptions.SemestrePensumNotFoundException;
import com.sistemas_mangager_be.edu_virtual_ufps.services.interfaces.IMateriaService;
import com.sistemas_mangager_be.edu_virtual_ufps.shared.DTOs.MateriaDTO;
import com.sistemas_mangager_be.edu_virtual_ufps.shared.requests.MateriaSemestreRequest;
import com.sistemas_mangager_be.edu_virtual_ufps.shared.requests.MoodleRequest;
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
@RequestMapping("/api/materias")
@RequiredArgsConstructor
@Tag(name = "Gestión de Materias Académicas", description = "API para la administración integral de materias de SIAV UFPS. "
        +
        "Gestiona el catálogo de asignaturas por pensum, validación de códigos únicos, " +
        "asignación por semestres y sincronización con plataforma Moodle.")
public class MateriaController {

    private final IMateriaService materiaService;

    @Operation(summary = "Crear nueva materia académica", description = "Registra una materia en el sistema validando unicidad del código y correcta asignación "
            +
            "al semestre del pensum. Valida que el número de semestre esté dentro del rango " +
            "permitido por el pensum (1 a cantidadSemestres) y que exista la relación SemestrePensum.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Materia creada exitosamente con validaciones completadas", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MateriaDTO.class), examples = @ExampleObject(value = """
                    {
                        "id": 1,
                        "nombre": "Metodología de la Investigación",
                        "codigo": "MIN",
                        "creditos": 3,
                        "semestre": "1",
                        "pensumId": 1,
                        "semestrePensumId": 1,
                        "moodleId": null
                    }
                    """))),
            @ApiResponse(responseCode = "400", description = "Código de materia duplicado o semestre fuera del rango del pensum", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class), examples = @ExampleObject(value = """
                    {
                        "httpStatusCode": 400,
                        "httpStatus": "BAD_REQUEST",
                        "reason": "Bad Request",
                        "message": "la materia con el codigo MIN ya esta en registrada en el sistema"
                    }
                    """))),
            @ApiResponse(responseCode = "404", description = "Pensum no encontrado o relación semestre-pensum inexistente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class), examples = @ExampleObject(value = """
                    {
                        "httpStatusCode": 404,
                        "httpStatus": "NOT_FOUND",
                        "reason": "Not Found",
                        "message": "el pensum con el id 1 no fue encontrado"
                    }
                    """)))
    })
    @PostMapping("/crear")
    public ResponseEntity<MateriaDTO> crearMateria(
            @Parameter(description = "Datos de la materia incluyendo código único, pensum y semestre", required = true, schema = @Schema(implementation = MateriaDTO.class), example = """
                    {
                        "nombre": "Metodología de la Investigación",
                        "codigo": "MIN",
                        "creditos": 3,
                        "semestre": "1",
                        "pensumId": 1
                    }
                    """) @RequestBody MateriaDTO materiaDTO)
            throws PensumNotFoundException, MateriaExistsException,
            SemestrePensumNotFoundException {

        MateriaDTO materia = materiaService.crearMateria(materiaDTO);
        return new ResponseEntity<>(materia, HttpStatus.CREATED);
    }

    @Operation(summary = "Actualizar materia académica", description = "Modifica una materia existente con validaciones de código único (solo si cambia), "
            +
            "verificación de rango de semestre en el pensum y actualización de relaciones. " +
            "Permite cambiar pensum, semestre y todos los demás campos.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Materia actualizada exitosamente con validaciones completadas", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class), examples = @ExampleObject(value = """
                    {
                        "httpStatusCode": 200,
                        "httpStatus": "OK",
                        "reason": "OK",
                        "message": "Materia actualizada con exito"
                    }
                    """))),
            @ApiResponse(responseCode = "400", description = "Código duplicado o semestre inválido para el pensum", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class), examples = @ExampleObject(value = """
                    {
                        "httpStatusCode": 400,
                        "httpStatus": "BAD_REQUEST",
                        "reason": "Bad Request",
                        "message": "el semestre 5 no está en el rango del pensum (1-4)"
                    }
                    """))),
            @ApiResponse(responseCode = "404", description = "Materia, pensum o relación semestre-pensum no encontrados", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<HttpResponse> actualizarMateria(
            @Parameter(description = "ID único de la materia a actualizar", required = true, example = "1", schema = @Schema(type = "integer", minimum = "1")) @PathVariable Integer id,

            @Parameter(description = "Datos actualizados de la materia con validaciones automáticas", required = true, schema = @Schema(implementation = MateriaDTO.class), example = """
                    {
                        "nombre": "Metodología de la Investigación Actualizada",
                        "codigo": "MINA",
                        "creditos": 4,
                        "semestre": "2",
                        "pensumId": 1
                    }
                    """) @RequestBody MateriaDTO materiaDTO)
            throws MateriaExistsException, PensumNotFoundException,
            MateriaNotFoundException, SemestrePensumNotFoundException {

        materiaService.actualizarMateria(id, materiaDTO);
        return new ResponseEntity<>(
                new HttpResponse(HttpStatus.OK.value(), HttpStatus.OK, HttpStatus.OK.getReasonPhrase(),
                        "Materia actualizada con exito"),
                HttpStatus.OK);
    }

    @Operation(summary = "Consultar materia específica", description = "Obtiene información completa de una materia incluyendo datos del pensum asociado "
            +
            "y relación con semestre-pensum.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Información de la materia obtenida exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MateriaDTO.class), examples = @ExampleObject(value = """
                    {
                        "id": 1,
                        "nombre": "Metodología de la Investigación",
                        "codigo": "MIN",
                        "creditos": 3,
                        "semestre": "1",
                        "pensumId": 1,
                        "semestrePensumId": 1,
                        "moodleId": "COURSE_123"
                    }
                    """))),
            @ApiResponse(responseCode = "404", description = "Materia no encontrada", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class), examples = @ExampleObject(value = """
                    {
                        "httpStatusCode": 404,
                        "httpStatus": "NOT_FOUND",
                        "reason": "Not Found",
                        "message": "la materia con el id 1 no fue encontrada"
                    }
                    """)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<MateriaDTO> listarMateria(
            @Parameter(description = "ID único de la materia a consultar", required = true, example = "1", schema = @Schema(type = "integer", minimum = "1")) @PathVariable Integer id)
            throws MateriaNotFoundException {

        MateriaDTO materia = materiaService.listarMateria(id);
        return new ResponseEntity<>(materia, HttpStatus.OK);
    }

    @Operation(summary = "Listar todas las materias", description = "Obtiene el catálogo completo de materias académicas registradas en el sistema "
            +
            "con información básica de cada asignatura y su pensum asociado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista completa de materias obtenida exitosamente", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = MateriaDTO.class))))
    })
    @GetMapping("/listar")
    public ResponseEntity<List<MateriaDTO>> listarMaterias() {
        List<MateriaDTO> materias = materiaService.listarMaterias();
        return new ResponseEntity<>(materias, HttpStatus.OK);
    }

    @Operation(summary = "Consultar materias por pensum", description = "Filtra todas las materias asociadas a un pensum específico ordenadas por semestre. "
            +
            "Útil para obtener la estructura curricular completa de un plan de estudios.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de materias por pensum obtenida exitosamente", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = MateriaDTO.class)))),
            @ApiResponse(responseCode = "404", description = "Pensum no encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class), examples = @ExampleObject(value = """
                    {
                        "httpStatusCode": 404,
                        "httpStatus": "NOT_FOUND",
                        "reason": "Not Found",
                        "message": "el pensum con el id 1 no fue encontrado"
                    }
                    """)))
    })
    @GetMapping("/pensum/{pensumId}")
    public ResponseEntity<List<MateriaDTO>> listarMateriasPorPensum(
            @Parameter(description = "ID del pensum para filtrar materias", required = true, example = "1", schema = @Schema(type = "integer", minimum = "1")) @PathVariable Integer pensumId)
            throws PensumNotFoundException {

        List<MateriaDTO> materias = materiaService.listarMateriasPorPensum(pensumId);
        return new ResponseEntity<>(materias, HttpStatus.OK);
    }

    @Operation(summary = "Consultar materias por pensum y semestre específico", description = "Filtra materias de un pensum específico que pertenecen a un semestre determinado. "
            +
            "Útil para obtener la carga académica de un semestre particular dentro de un plan de estudios.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de materias por pensum y semestre obtenida exitosamente", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = MateriaDTO.class)), examples = @ExampleObject(value = """
                    [
                        {
                            "id": 1,
                            "nombre": "Metodología de la Investigación",
                            "codigo": "MIN",
                            "creditos": 3,
                            "semestre": "1",
                            "pensumId": 1,
                            "semestrePensumId": 1,
                            "moodleId": "COURSE_123"
                        },
                        {
                            "id": 2,
                            "nombre": "Fundamentos de TIC",
                            "codigo": "FTIC",
                            "creditos": 4,
                            "semestre": "1",
                            "pensumId": 1,
                            "semestrePensumId": 1,
                            "moodleId": "COURSE_124"
                        }
                    ]
                    """))),
            @ApiResponse(responseCode = "404", description = "Pensum no encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class)))
    })
    @GetMapping("/semestre")
    public ResponseEntity<List<MateriaDTO>> listarMateriasPorPensumPorSemestre(
            @Parameter(description = "Filtros para buscar materias por pensum y semestre específico", required = true, schema = @Schema(implementation = MateriaSemestreRequest.class), example = """
                    {
                        "pensumId": 1,
                        "semestre": "1"
                    }
                    """) @RequestBody MateriaSemestreRequest materiaSemestreRequest)
            throws PensumNotFoundException {

        List<MateriaDTO> materias = materiaService.listarMateriasPorPensumPorSemestre(materiaSemestreRequest);
        return new ResponseEntity<>(materias, HttpStatus.OK);
    }

    @Operation(summary = "Vincular materia con categoría de Moodle", description = "Establece la conexión entre una materia del sistema y su categoría o curso "
            +
            "correspondiente en Moodle. Valida que el MoodleID no esté ya en uso por otra materia.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vinculación con Moodle realizada exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class), examples = @ExampleObject(value = """
                    {
                        "httpStatusCode": 200,
                        "httpStatus": "OK",
                        "reason": "OK",
                        "message": "Vinculacion con moodle realizada con exito"
                    }
                    """))),
            @ApiResponse(responseCode = "400", description = "MoodleID ya está en uso por otra materia", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class), examples = @ExampleObject(value = """
                    {
                        "httpStatusCode": 400,
                        "httpStatus": "BAD_REQUEST",
                        "reason": "Bad Request",
                        "message": "la materia con el moodle id COURSE_123 ya esta en registrada en el sistema"
                    }
                    """))),
            @ApiResponse(responseCode = "404", description = "Materia no encontrada", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class), examples = @ExampleObject(value = """
                    {
                        "httpStatusCode": 404,
                        "httpStatus": "NOT_FOUND",
                        "reason": "Not Found",
                        "message": "la materia con el id 1 no fue encontrada"
                    }
                    """)))
    })
    @PostMapping("/moodle")
    public ResponseEntity<HttpResponse> vincularMoodle(
            @Parameter(description = "Información para vincular materia con categoría o curso de Moodle", required = true, schema = @Schema(implementation = MoodleRequest.class), example = """
                    {
                        "backendId": 1,
                        "moodleId": "COURSE_123"
                    }
                    """) @RequestBody MoodleRequest moodleRequest)
            throws MateriaExistsException, MateriaNotFoundException {

        materiaService.vincularMoodleId(moodleRequest);
        return new ResponseEntity<>(
                new HttpResponse(HttpStatus.OK.value(), HttpStatus.OK, HttpStatus.OK.getReasonPhrase(),
                        "Vinculacion con moodle realizada con exito"),
                HttpStatus.OK);
    }
}
