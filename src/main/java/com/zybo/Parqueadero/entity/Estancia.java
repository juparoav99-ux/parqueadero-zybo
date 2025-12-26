package com.zybo.Parqueadero.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "estancia")
public class Estancia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehiculo_id", nullable = false)
    private Vehiculo vehiculo;

    @Column(name = "hora_ingreso", nullable = false)
    private LocalDateTime horaIngreso;

    @Column(name = "hora_salida")
    private LocalDateTime horaSalida;

    @Column(length = 10)
    private String estado; // ABIERTA o CERRADA

    private Integer minutos;

    @Column(name = "valor_cobrado")
    private Integer valorCobrado;

    @Version
    private Long version; // Para control de concurrencia

    @PrePersist
    protected void onCreate() {
        horaIngreso = LocalDateTime.now();
        estado = "ABIERTA";
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Vehiculo getVehiculo() { return vehiculo; }
    public void setVehiculo(Vehiculo vehiculo) { this.vehiculo = vehiculo; }

    public LocalDateTime getHoraIngreso() { return horaIngreso; }
    public void setHoraIngreso(LocalDateTime horaIngreso) { this.horaIngreso = horaIngreso; }

    public LocalDateTime getHoraSalida() { return horaSalida; }
    public void setHoraSalida(LocalDateTime horaSalida) { this.horaSalida = horaSalida; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public Integer getMinutos() { return minutos; }
    public void setMinutos(Integer minutos) { this.minutos = minutos; }

    public Integer getValorCobrado() { return valorCobrado; }
    public void setValorCobrado(Integer valorCobrado) { this.valorCobrado = valorCobrado; }

    public Long getVersion() { return version; }
    public void setVersion(Long version) { this.version = version; }
}