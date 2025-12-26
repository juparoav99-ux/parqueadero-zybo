package com.zybo.Parqueadero.dto;

import java.time.LocalDateTime;

public class EventoOutboxDTO {

    private Long id;
    private String tipoEvento;
    private String payload;
    private String estado;
    private LocalDateTime creadoEn;

    // Constructores
    public EventoOutboxDTO() {}

    public EventoOutboxDTO(Long id, String tipoEvento, String payload,
                           String estado, LocalDateTime creadoEn) {
        this.id = id;
        this.tipoEvento = tipoEvento;
        this.payload = payload;
        this.estado = estado;
        this.creadoEn = creadoEn;
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
