package com.zybo.Parqueadero.dto;

import java.time.LocalDateTime;

public class EstanciaDTO {

    private Long id;
    private Long vehiculoId;
    private LocalDateTime horaIngreso;
    private LocalDateTime horaSalida;
    private Integer minutos;
    private Integer valorCobrado;
    private String estado;

    // Constructores
    public EstanciaDTO() {}

    public EstanciaDTO(Long id, Long vehiculoId, LocalDateTime horaIngreso,
                       LocalDateTime horaSalida, Integer minutos,
                       Integer valorCobrado, String estado) {
        this.id = id;
        this.vehiculoId = vehiculoId;
        this.horaIngreso = horaIngreso;
        this.horaSalida = horaSalida;
        this.minutos = minutos;
        this.valorCobrado = valorCobrado;
        this.estado = estado;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getVehiculoId() { return vehiculoId; }
    public void setVehiculoId(Long vehiculoId) { this.vehiculoId = vehiculoId; }

    public LocalDateTime getHoraIngreso() { return horaIngreso; }
    public void setHoraIngreso(LocalDateTime horaIngreso) { this.horaIngreso = horaIngreso; }

    public LocalDateTime getHoraSalida() { return horaSalida; }
    public void setHoraSalida(LocalDateTime horaSalida) { this.horaSalida = horaSalida; }

    public Integer getMinutos() { return minutos; }
    public void setMinutos(Integer minutos) { this.minutos = minutos; }

    public Integer getValorCobrado() { return valorCobrado; }
    public void setValorCobrado(Integer valorCobrado) { this.valorCobrado = valorCobrado; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}