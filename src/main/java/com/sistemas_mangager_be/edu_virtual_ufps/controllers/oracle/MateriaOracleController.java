package com.sistemas_mangager_be.edu_virtual_ufps.controllers.oracle;

import com.sistemas_mangager_be.edu_virtual_ufps.oracle.entities.MateriaOracle;
import com.sistemas_mangager_be.edu_virtual_ufps.oracle.repositories.MateriaOracleRepository;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.Optional;

@Profile("prod")
@RestController
@RequestMapping("/api/oracle/materias")
@Tag(name = "Gestión de Materias Oracle", description = "API para la consulta de información de materias académicas desde la base de datos Oracle de UFPS. "
        + "Proporciona acceso a datos curriculares, créditos, semestres y información académica para integración con sistemas externos.")
public class MateriaOracleController {

    private final MateriaOracleRepository materiaOracleRepository;

    public MateriaOracleController(MateriaOracleRepository materiaOracleRepository) {
        this.materiaOracleRepository = materiaOracleRepository;
    }

    @Operation(summary = "Listar todas las materias", description = "Obtiene el catálogo completo de materias académicas registradas en la base de datos Oracle "
            + "incluyendo información curricular, créditos, horas teóricas y prácticas, semestre y estado de actividad.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de materias obtenida exitosamente", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = MateriaOracle.class)), examples = @ExampleObject(value = """
                    [
                        {
                            "codDpto": "44",
                            "codCarrera": "046",
                            "codMateria": "6207",
                            "nombre": "FORMULACION Y EVALUACION DE PR",
                            "ht": 3,
                            "hp": 0,
                            "creditos": 1,
                            "multiP": "N",
                            "semestre": "06",
                            "activa": "S",
                            "nbc": "14",
                            "hti": 0,
                            "hasa": 0,
                            "hasl": null,
                            "unicanota": "N",
                            "moduloAcu012": "N",
                            "tipoMateria": "TE",
                            "idMicro": null
                        },
                        {
                            "codDpto": "52",
                            "codCarrera": "046",
                            "codMateria": "6307",
                            "nombre": "COMPUTACION II",
                            "ht": 3,
                            "hp": 0,
                            "creditos": 1,
                            "multiP": "N",
                            "semestre": "6",
                            "activa": "S",
                            "nbc": "14",
                            "hti": 0,
                            "hasa": 0,
                            "hasl": 0,
                            "unicanota": "N",
                            "moduloAcu012": "N",
                            "tipoMateria": "TE",
                            "idMicro": null
                        }
                    ]
                    """)))
    })
    @GetMapping
    public List<MateriaOracle> obtenerTodos() {
        return materiaOracleRepository.findAll();
    }

    @Operation(summary = "Listar materias de Tecnología", description = "Obtiene todas las materias del programa de Tecnología (código de carrera 215) "
            + "incluyendo información curricular completa, créditos y distribución de horas.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de materias de Tecnología obtenida exitosamente", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = MateriaOracle.class)), examples = @ExampleObject(value = """
                    [
                        {
                            "codDpto": "52",
                            "codCarrera": "215",
                            "codMateria": "0101",
                            "nombre": "FUNDAMENTOS DE PROGRAMACION",
                            "ht": 48,
                            "hp": 0,
                            "creditos": 3,
                            "multiP": "N",
                            "semestre": "1",
                            "activa": "S",
                            "nbc": null,
                            "hti": 6,
                            "hasa": 0,
                            "hasl": 0,
                            "unicanota": "N",
                            "moduloAcu012": "N",
                            "tipoMateria": "TE",
                            "idMicro": null
                        },
                        {
                            "codDpto": "41",
                            "codCarrera": "215",
                            "codMateria": "0102",
                            "nombre": "INTRODUCCION A LA VIDA UNIVERSITARIA",
                            "ht": 16,
                            "hp": 0,
                            "creditos": 1,
                            "multiP": "N",
                            "semestre": "1",
                            "activa": "S",
                            "nbc": null,
                            "hti": 2,
                            "hasa": 0,
                            "hasl": 0,
                            "unicanota": "N",
                            "moduloAcu012": "N",
                            "tipoMateria": "TE",
                            "idMicro": null
                        }
                    ]
                    """)))
    })
    @GetMapping("/tecnologia")
    public List<MateriaOracle> obtenerMateriasTecnologia() {
        return materiaOracleRepository.findByCodCarrera("215");
    }

    @Operation(summary = "Buscar materia específica", description = "Busca una materia específica utilizando el código de carrera y código de materia. "
            + "Retorna información completa de la materia si existe, o respuesta vacía si no se encuentra.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Materia encontrada exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MateriaOracle.class), examples = @ExampleObject(value = """
                    {
                        "codDpto": "52",
                        "codCarrera": "215",
                        "codMateria": "0101",
                        "nombre": "FUNDAMENTOS DE PROGRAMACION",
                        "ht": 48,
                        "hp": 0,
                        "creditos": 3,
                        "multiP": "N",
                        "semestre": "1",
                        "activa": "S",
                        "nbc": null,
                        "hti": 6,
                        "hasa": 0,
                        "hasl": 0,
                        "unicanota": "N",
                        "moduloAcu012": "N",
                        "tipoMateria": "TE",
                        "idMicro": null
                    }
                    """))),
            @ApiResponse(responseCode = "404", description = "Materia no encontrada", content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/buscar")
    public ResponseEntity<MateriaOracle> obtenerMateriaPorCarreraYCodigo(
            @Parameter(description = "Código de la carrera", required = true, example = "215", schema = @Schema(type = "string")) @RequestParam String codCarrera,
            @Parameter(description = "Código de la materia", required = true, example = "0101", schema = @Schema(type = "string")) @RequestParam String codMateria) {

        Optional<MateriaOracle> materia = materiaOracleRepository.findByCodCarreraAndCodMateria(codCarrera, codMateria);

        if (materia.isPresent()) {
            return ResponseEntity.ok(materia.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
