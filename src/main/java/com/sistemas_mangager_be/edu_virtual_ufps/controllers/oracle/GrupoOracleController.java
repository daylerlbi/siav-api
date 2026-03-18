package com.sistemas_mangager_be.edu_virtual_ufps.controllers.oracle;

import com.sistemas_mangager_be.edu_virtual_ufps.oracle.entities.GrupoOracle;
import com.sistemas_mangager_be.edu_virtual_ufps.oracle.repositories.GrupoOracleRespository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Profile;
import java.util.List;


@RestController
@RequestMapping("/api/oracle/grupos")
@Tag(name = "Gestión de Grupos Oracle", description = "API para la consulta de información de grupos académicos desde la base de datos Oracle de UFPS. "
        + "Proporciona acceso a datos de grupos por carrera, materia, profesores asignados y capacidad de matrícula.")
public class GrupoOracleController {

    private final GrupoOracleRespository grupoOracleRespository;

    public GrupoOracleController(GrupoOracleRespository grupoOracleRespository) {
        this.grupoOracleRespository = grupoOracleRespository;
    }

    @Operation(summary = "Listar todos los grupos", description = "Obtiene el catálogo completo de grupos académicos registrados en la base de datos Oracle "
            + "incluyendo información de matrícula, profesores asignados y capacidad máxima.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de grupos obtenida exitosamente", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = GrupoOracle.class)), examples = @ExampleObject(value = """
                    [
                        {
                            "codCarrera": "170",
                            "codMateria": "0704",
                            "grupo": "A",
                            "ciclo": "0",
                            "numAlumMatriculados": 39,
                            "numMaxAlumnos": 50,
                            "codProfesor": "05782",
                            "notasProcesadas": "N",
                            "cedido": "N",
                            "seccional": "01",
                            "dirigido": "N"
                        },
                        {
                            "codCarrera": "170",
                            "codMateria": "0705",
                            "grupo": "C",
                            "ciclo": "0",
                            "numAlumMatriculados": 10,
                            "numMaxAlumnos": 10,
                            "codProfesor": "07620",
                            "notasProcesadas": "N",
                            "cedido": "N",
                            "seccional": "01",
                            "dirigido": "N"
                        }
                    ]
                    """)))
    })
    @GetMapping
    public List<GrupoOracle> obtenerTodos() {
        return grupoOracleRespository.findAll();
    }

    @Operation(summary = "Listar grupos por carrera", description = "Obtiene todos los grupos académicos de una carrera específica "
            + "incluyendo información de matrícula y profesores asignados por materia.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Consulta realizada exitosamente", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = GrupoOracle.class)), examples = {
                    @ExampleObject(name = "Grupos encontrados", value = """
                            [
                                {
                                    "codCarrera": "215",
                                    "codMateria": "0101",
                                    "grupo": "A",
                                    "ciclo": "0",
                                    "numAlumMatriculados": 19,
                                    "numMaxAlumnos": 40,
                                    "codProfesor": "05096",
                                    "notasProcesadas": "N",
                                    "cedido": "N",
                                    "seccional": "01",
                                    "dirigido": "N"
                                },
                                {
                                    "codCarrera": "215",
                                    "codMateria": "0102",
                                    "grupo": "A",
                                    "ciclo": "0",
                                    "numAlumMatriculados": 19,
                                    "numMaxAlumnos": 40,
                                    "codProfesor": "07356",
                                    "notasProcesadas": "N",
                                    "cedido": "N",
                                    "seccional": "01",
                                    "dirigido": "N"
                                }
                            ]
                            """),
                    @ExampleObject(name = "Sin grupos", value = "[]")
            }))
    })
    @GetMapping("/carrera")
    public List<GrupoOracle> obtenerGruposSistemas(
            @Parameter(description = "Código de la carrera para filtrar grupos", required = true, example = "215", schema = @Schema(type = "string")) @RequestParam String codCarrera) {
        return grupoOracleRespository.findByCodCarrera(codCarrera);
    }

    @Operation(summary = "Buscar grupo específico", description = "Busca un grupo específico utilizando código de carrera, código de materia y grupo. "
            + "Retorna información detallada del grupo si existe, o array vacío si no se encuentra.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Búsqueda realizada exitosamente", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = GrupoOracle.class)), examples = {
                    @ExampleObject(name = "Grupo encontrado", value = """
                            [
                                {
                                    "codCarrera": "215",
                                    "codMateria": "0101",
                                    "grupo": "A",
                                    "ciclo": "0",
                                    "numAlumMatriculados": 19,
                                    "numMaxAlumnos": 40,
                                    "codProfesor": "05096",
                                    "notasProcesadas": "N",
                                    "cedido": "N",
                                    "seccional": "01",
                                    "dirigido": "N"
                                }
                            ]
                            """),
                    @ExampleObject(name = "Grupo no encontrado", value = "[]")
            }))
    })
    @GetMapping("/grupo")
    public List<GrupoOracle> obtenerGrupo(
            @Parameter(description = "Código de la carrera", required = true, example = "215", schema = @Schema(type = "string")) @RequestParam String codCarrera,
            @Parameter(description = "Código de la materia", required = true, example = "0101", schema = @Schema(type = "string")) @RequestParam String codMateria,
            @Parameter(description = "Identificador del grupo", required = true, example = "A", schema = @Schema(type = "string")) @RequestParam String grupo) {
        return grupoOracleRespository.findByCodCarreraAndCodMateriaAndGrupo(codCarrera, codMateria, grupo);
    }

}
