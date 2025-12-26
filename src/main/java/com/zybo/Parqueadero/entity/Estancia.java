package com.zybo.Parqueadero.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entidad que representa una estancia (ingreso/salida) de un vehículo.
 * Maneja el control de tiempo y cobro por estadía.
 * @author Juan Rozo
 */
@Entity
@Table(name = "estancia")
public class Estancia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Vehículo asociado a esta estancia
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehiculo_id", nullable = false)
    private Vehiculo vehiculo;

    // Hora de ingreso al parqueadero
    @Column(name = "hora_ingreso", nullable = false)
    private LocalDateTime horaIngreso;

    // Hora de salida (null mientras está ABIERTA)
    @Column(name = "hora_salida")
    private LocalDateTime horaSalida;

    // Estado: ABIERTA o CERRADA
    @Column(length = 10)
    private String estado;

    // Minutos de estadía calculados al cerrar
    private Integer minutos;

    // Valor cobrado = minutos * 100
    @Column(name = "valor_cobrado")
    private Integer valorCobrado;

    // Campo para control de concurrencia con optimistic locking
    @Version
    private Long version;

    @PrePersist
    protected void onCreate() {
        this.horaIngreso = LocalDateTime.now();
        this.estado = "ABIERTA";
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