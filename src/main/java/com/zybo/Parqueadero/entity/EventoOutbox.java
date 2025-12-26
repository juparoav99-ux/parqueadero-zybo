package com.zybo.Parqueadero.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entidad para el patrón Outbox - simula cola de mensajería.
 * Almacena eventos pendientes de despachar a otros sistemas.
 * @author Juan Rozo
 */
@Entity
@Table(name = "evento_outbox")
public class EventoOutbox {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Tipo de evento (ej: SALIDA_REGISTRADA)
    @Column(name = "tipo_evento", length = 40)
    private String tipoEvento;

    // Payload JSON con datos del evento
    @Column(columnDefinition = "TEXT")
    private String payload;

    // Estado: PENDIENTE o ENVIADO
    @Column(length = 12)
    private String estado;

    @Column(name = "creado_en")
    private LocalDateTime creadoEn;

    @PrePersist
    protected void onCreate() {
        this.creadoEn = LocalDateTime.now();
        this.estado = "PENDIENTE";
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTipoEvento() { return tipoEvento; }
    public void setTipoEvento(String tipoEvento) { this.tipoEvento = tipoEvento; }

    public String getPayload() { return payload; }
    public void setPayload(String payload) { this.payload = payload; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public LocalDateTime getCreadoEn() { return creadoEn; }
    public void setCreadoEn(LocalDateTime creadoEn) { this.creadoEn = creadoEn; }
}