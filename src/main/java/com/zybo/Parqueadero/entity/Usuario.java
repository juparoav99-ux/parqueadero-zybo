package com.zybo.Parqueadero.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entidad que representa un usuario del parqueadero.
 * Un usuario puede tener múltiples vehículos asociados.
 * @author Juan Rozo
 */
@Entity
@Table(name = "usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String nombres;

    // Documento único para identificar al usuario
    @Column(nullable = false, unique = true, length = 30)
    private String documento;

    // Teléfono único para contacto
    @Column(nullable = false, unique = true, length = 30)
    private String telefono;

    @Column(name = "creado_en")
    private LocalDateTime creadoEn;

    // Se ejecuta antes de persistir para registrar fecha de creación
    @PrePersist
    protected void onCreate() {
        this.creadoEn = LocalDateTime.now();
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

    public LocalDateTime getCreadoEn() { return creadoEn; }
    public void setCreadoEn(LocalDateTime creadoEn) { this.creadoEn = creadoEn; }
}