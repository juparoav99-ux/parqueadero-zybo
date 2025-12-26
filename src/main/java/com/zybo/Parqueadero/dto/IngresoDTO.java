package com.zybo.Parqueadero.dto;

import jakarta.validation.constraints.NotNull;

public class IngresoDTO {

    @NotNull(message = "El vehiculoId es obligatorio")
    private Long vehiculoId;

    // Constructores
    public IngresoDTO() {}

    public IngresoDTO(Long vehiculoId) {
        this.vehiculoId = vehiculoId;
    }

    // Getters y Setters
    public Long getVehiculoId() { return vehiculoId; }
    public void setVehiculoId(Long vehiculoId) { this.vehiculoId = vehiculoId; }
}