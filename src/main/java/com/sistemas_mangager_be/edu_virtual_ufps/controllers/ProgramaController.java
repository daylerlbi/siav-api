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
@Tag(name = "Gestión de Programas Académicos", description = "API para la administración integral de programas académicos de SIAV UFPS.")
public class ProgramaController {

    private final IProgramaService programaService;
    private final TipoProgramaRepository tipoProgramaRepository;

    @PostMapping("/crear")
    public ResponseEntity<ProgramaDTO> crearPrograma(@RequestBody ProgramaDTO programaDTO)
            throws ProgramaExistsException {
        ProgramaDTO programa = programaService.crearPrograma(programaDTO);
        return new ResponseEntity<>(programa, HttpStatus.OK);
    }

    @PostMapping("/moodle")
    public ResponseEntity<HttpResponse> vincularMoodle(@RequestBody MoodleRequest moodleRequest)
            throws ProgramaNotFoundException, ProgramaExistsException {
        programaService.vincularMoodleId(moodleRequest);
        return new ResponseEntity<>(
                new HttpResponse(HttpStatus.OK.value(), HttpStatus.OK, HttpStatus.OK.getReasonPhrase(),
                        "Vinculacion con moodle realizada con exito"),
                HttpStatus.OK);
    }

    @PostMapping("/historico/moodle")
    public ResponseEntity<HttpResponse> vincularHistoricoMoodle(@RequestBody MoodleRequest moodleRequest)
            throws ProgramaNotFoundException, ProgramaExistsException {
        programaService.vincularHistoricoMoodleId(moodleRequest);
        return new ResponseEntity<>(
                new HttpResponse(HttpStatus.OK.value(), HttpStatus.OK, HttpStatus.OK.getReasonPhrase(),
                        "Vinculacion del historico con moodle realizada con exito"),
                HttpStatus.OK);
    }

    @GetMapping("/listar")
    public ResponseEntity<List<ProgramaDTO>> listarProgramas() {
        return new ResponseEntity<>(programaService.listarProgramas(), HttpStatus.OK);
    }

    // ✅ NUEVO: listar programas por director
    @GetMapping("/listar/director/{directorId}")
    public ResponseEntity<List<ProgramaDTO>> listarProgramasPorDirector(
            @PathVariable Integer directorId) {
        return new ResponseEntity<>(programaService.listarProgramasPorDirector(directorId), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProgramaDTO> listarPrograma(@PathVariable Integer id)
            throws ProgramaNotFoundException {
        ProgramaDTO programaDTO = programaService.listarPrograma(id);
        return new ResponseEntity<>(programaDTO, HttpStatus.OK);
    }

    @GetMapping("/codigo/{codigo}")
    public ResponseEntity<ProgramaDTO> listarProgramaPorCodigo(@PathVariable String codigo)
            throws ProgramaNotFoundException {
        ProgramaDTO programaDTO = programaService.listarProgramaPorCodigo(codigo);
        return new ResponseEntity<>(programaDTO, HttpStatus.OK);
    }

    @GetMapping("/{id}/semestres")
    public ResponseEntity<SemestreProgramaResponse> listarSemestresPorPrograma(@PathVariable Integer id)
            throws ProgramaNotFoundException {
        SemestreProgramaResponse semestreProgramaResponse = programaService.listarSemestresPorPrograma(id);
        return new ResponseEntity<>(semestreProgramaResponse, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<HttpResponse> actualizarPrograma(@PathVariable Integer id,
            @RequestBody ProgramaDTO programaDTO)
            throws ProgramaNotFoundException, ProgramaExistsException {
        programaService.actualizarPrograma(programaDTO, id);
        return new ResponseEntity<>(
                new HttpResponse(HttpStatus.OK.value(), HttpStatus.OK, HttpStatus.OK.getReasonPhrase(),
                        " Programa actualizado con exito"),
                HttpStatus.OK);
    }

    @GetMapping("/tipos-programa")
    public ResponseEntity<List<TipoPrograma>> listarTiposPrograma() {
        List<TipoPrograma> tiposPrograma = tipoProgramaRepository.findAll();
        return new ResponseEntity<>(tiposPrograma, HttpStatus.OK);
    }
}