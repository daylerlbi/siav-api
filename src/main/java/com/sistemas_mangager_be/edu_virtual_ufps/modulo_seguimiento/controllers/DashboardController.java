package com.sistemas_mangager_be.edu_virtual_ufps.modulo_seguimiento.controllers;

import com.sistemas_mangager_be.edu_virtual_ufps.modulo_seguimiento.dtos.DashboardDto;
import com.sistemas_mangager_be.edu_virtual_ufps.modulo_seguimiento.services.DashboardService;
import com.sistemas_mangager_be.edu_virtual_ufps.shared.responses.HttpResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
@Tag(name = "Dashboard", description = "API para visualizar el panel de control con informacion relevante sobre el proyecto" +
        " académico del usuario. Incluye actividades proximas a entregar o atrasadas.")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @Operation(
            summary = "Obtener información del dashboard",
            description = "Devuelve estado actual del proyecto del estudiante, incluyento actividades proximas a entregar o atrasadas."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Datos del dashboard obtenidos correctamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = DashboardDto.class))),
            @ApiResponse(responseCode = "500", description = "Error del servidor",
                    content = @Content(schema = @Schema(implementation = HttpResponse.class)))
    })
    @GetMapping()
    public ResponseEntity<DashboardDto> getDashboard() {
        return ResponseEntity.ok(dashboardService.obtenerDashboard());
    }
}
