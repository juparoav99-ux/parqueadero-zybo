package com.zybo.Parqueadero.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UsuarioDTO {

    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 120, message = "El nombre no puede exceder 120 caracteres")
    private String nombres;

    @NotBlank(message = "El documento es obligatorio")
    @Size(max = 30, message = "El documento no puede exceder 30 caracteres")
    private String documento;

    @NotBlank(message = "El teléfono es obligatorio")
    @Size(max = 30, message = "El teléfono no puede exceder 30 caracteres")
    private String telefono;

    // Constructores
    public UsuarioDTO() {}

    public UsuarioDTO(Long id, String nombres, String documento, String telefono) {
        this.id = id;
        this.nombres = nombres;
        this.documento = documento;
        this.telefono = telefono;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombres() { return nombres; }
    public void setNombres(String nombres) { this.nombres = nombres; }

    public String getDocumento() { return documento; }
    public void setDocumento(String documento) { this.documento = documento; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
}