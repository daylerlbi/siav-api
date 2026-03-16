package com.sistemas_mangager_be.edu_virtual_ufps.controllers.oracle;

import com.sistemas_mangager_be.edu_virtual_ufps.oracle.entities.MateriasMatriculadasOracle;
import com.sistemas_mangager_be.edu_virtual_ufps.oracle.repositories.MateriasMatriculadasOracleRepository;
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

import java.util.List;


@RestController
@RequestMapping("/api/oracle/materias-matriculadas")
@Tag(name = "Gestión de Materias Matriculadas Oracle", description = "API para la consulta de información de materias matriculadas desde la base de datos Oracle de UFPS. "
        + "Proporciona acceso a datos de matriculación por estudiante, carrera, materia y grupo para análisis académico.")
public class MateriasMatriculadasOracleController {

    private final MateriasMatriculadasOracleRepository materiasMatriculadasOracleRepository;

    public MateriasMatriculadasOracleController(
            MateriasMatriculadasOracleRepository materiasMatriculadasOracleRepository) {
        this.materiasMatriculadasOracleRepository = materiasMatriculadasOracleRepository;
    }

    @GetMapping
    public List<MateriasMatriculadasOracle> obtenerTodas() {
        return materiasMatriculadasOracleRepository.findAll();
    }

    @GetMapping("/sistemas")
    public List<MateriasMatriculadasOracle> obtenerPorCodCarMat() {
        return materiasMatriculadasOracleRepository.findByCodCarMat("115");
    }

    @GetMapping("/alumno-sistemas")
    public List<MateriasMatriculadasOracle> obtenerPorAlumno() {
        return materiasMatriculadasOracleRepository.findByCodAlumno("2042");
    }

    @GetMapping("/alumno-materia-sistemas")
    public List<MateriasMatriculadasOracle> obtenerPorAlumnoYMateria(
            @RequestParam String codAlumno,
            @RequestParam String codMateria) {
        return materiasMatriculadasOracleRepository.findByCodAlumnoAndCodMateria(codAlumno, codMateria);
    }

    @Operation(summary = "Filtrar materias matriculadas por carrera, materia y grupo", description = "Busca materias matriculadas específicas utilizando código de carrera, código de materia y grupo. "
            + "Útil para consultar estudiantes matriculados en una materia específica de un grupo determinado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Filtrado realizado exitosamente", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = MateriasMatriculadasOracle.class)), examples = {
                    @ExampleObject(name = "Materias encontradas", value = """
                            [
                                {
                                    "codAlumno": "0018",
                                    "codCarrera": "215",
                                    "codMateria": "0101",
                                    "grupo": "A",
                                    "codCarMat": "215",
                                    "estado": "M",
                                    "codMatMat": "0101",
                                    "seccional": "01"
                                },
                                {
                                    "codAlumno": "0027",
                                    "codCarrera": "215",
                                    "codMateria": "0101",
                                    "grupo": "A",
                                    "codCarMat": "215",
                                    "estado": "M",
                                    "codMatMat": "0101",
                                    "seccional": "01"
                                }
                            ]
                            """),
                    @ExampleObject(name = "Sin resultados", value = "[]")
            }))
    })
    @GetMapping("/filtrar")
    public List<MateriasMatriculadasOracle> filtrarPorCarreraMateriaYGrupo(
            @Parameter(description = "Código de carrera para matriculación", required = true, example = "215", schema = @Schema(type = "string")) @RequestParam String codCarMat,
            @Parameter(description = "Código de materia para matriculación", required = true, example = "0101", schema = @Schema(type = "string")) @RequestParam String codMatMat,
            @Parameter(description = "Grupo de la materia", required = true, example = "A", schema = @Schema(type = "string")) @RequestParam String grupo) {
        return materiasMatriculadasOracleRepository.findByCodCarMatAndCodMatMatAndGrupo(codCarMat, codMatMat, grupo);
    }

    @GetMapping("/alumno-carrera")
    public List<MateriasMatriculadasOracle> obtenerPorCarreraYAlumno(
            @RequestParam String codCarrera,
            @RequestParam String codAlumno) {
        return materiasMatriculadasOracleRepository.findByCodCarreraAndCodAlumno(codCarrera, codAlumno);
    }
}
