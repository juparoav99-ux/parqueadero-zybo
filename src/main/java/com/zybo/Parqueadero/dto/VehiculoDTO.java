package com.zybo.Parqueadero.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class VehiculoDTO {

    private Long id;

    @NotBlank(message = "La placa es obligatoria")
    @Size(max = 12, message = "La placa no puede exceder 12 caracteres")
    private String placa;

    @NotNull(message = "El usuarioId es obligatorio")
    private Long usuarioId;

    // Constructores
    public VehiculoDTO() {}

    public VehiculoDTO(Long id, String placa, Long usuarioId) {
        this.id = id;
        this.placa = placa;
        this.usuarioId = usuarioId;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getPlaca() { return placa; }
    public void setPlaca(String placa) { this.placa = placa; }

    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }
}