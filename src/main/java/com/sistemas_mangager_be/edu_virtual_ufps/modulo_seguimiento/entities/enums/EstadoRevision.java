package com.sistemas_mangager_be.edu_virtual_ufps.modulo_seguimiento.entities.enums;

public enum EstadoRevision {
    SIN_REVISAR("Sin Revisar"),
    EN_REVISION("En Revisión"),
    ACEPTADA("Aceptada"),
    RECHAZADA("Rechazada");

    private final String descripcion;

    EstadoRevision(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
