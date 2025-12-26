package com.zybo.Parqueadero.service;

import com.zybo.Parqueadero.dto.EstanciaDTO;
import com.zybo.Parqueadero.dto.IngresoDTO;
import com.zybo.Parqueadero.entity.Estancia;
import com.zybo.Parqueadero.entity.EventoOutbox;
import com.zybo.Parqueadero.entity.Vehiculo;
import com.zybo.Parqueadero.exception.ConflictException;
import com.zybo.Parqueadero.exception.ResourceNotFoundException;
import com.zybo.Parqueadero.repository.EstanciaRepository;
import com.zybo.Parqueadero.repository.EventoOutboxRepository;
import com.zybo.Parqueadero.repository.VehiculoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * Servicio para gestionar estancias (ingresos y salidas).
 * Implementa manejo de concurrencia con locks pesimistas.
 * @author Juan Rozo
 */
@Service
public class EstanciaService {

    // Tarifa: 100 pesos por minuto de estadía
    private static final int TARIFA_POR_MINUTO = 100;

    @Autowired
    private EstanciaRepository estanciaRepository;

    @Autowired
    private VehiculoRepository vehiculoRepository;

    @Autowired
    private EventoOutboxRepository eventoOutboxRepository;

    /**
     * Registra el ingreso de un vehículo al parqueadero.
     * Usa lock pesimista para evitar doble ingreso concurrente.
     */
    @Transactional
    public EstanciaDTO registrarIngreso(IngresoDTO dto) {
        // Verificar que el vehículo existe
        Vehiculo vehiculo = vehiculoRepository.findById(dto.getVehiculoId())
                .orElseThrow(() -> new ResourceNotFoundException("Vehiculo no encontrado con id: " + dto.getVehiculoId()));

        // Lock pesimista: evita que dos hilos creen estancia simultáneamente
        estanciaRepository.findEstanciaAbiertaByVehiculoIdWithLock(dto.getVehiculoId())
                .ifPresent(e -> {
                    throw new ConflictException("El vehiculo ya tiene una estancia abierta");
                });

        // Crear nueva estancia
        Estancia estancia = new Estancia();
        estancia.setVehiculo(vehiculo);

        estancia = estanciaRepository.save(estancia);
        return toDTO(estancia);
    }

    /**
     * Registra la salida y calcula el cobro.
     * Usa lock pesimista para evitar doble cierre concurrente.
     * Redondeo: si hay segundos adicionales, se cobra el minuto completo.
     */
    @Transactional
    public EstanciaDTO registrarSalida(Long estanciaId) {
        // Lock pesimista: evita que dos hilos cierren la misma estancia
        Estancia estancia = estanciaRepository.findByIdWithLock(estanciaId)
                .orElseThrow(() -> new ResourceNotFoundException("Estancia no encontrada con id: " + estanciaId));

        // Validar que no esté ya cerrada
        if ("CERRADA".equals(estancia.getEstado())) {
            throw new ConflictException("La estancia ya esta cerrada");
        }

        // Calcular tiempo de estadía
        LocalDateTime horaSalida = LocalDateTime.now();
        Duration duracion = Duration.between(estancia.getHoraIngreso(), horaSalida);
        long minutos = duracion.toMinutes();

        // Redondeo hacia arriba: si hay segundos extra, se cobra minuto completo
        if (duracion.toSecondsPart() > 0) {
            minutos++;
        }

        // Calcular cobro: minutos * 100 pesos
        int valorCobrado = (int) minutos * TARIFA_POR_MINUTO;

        // Actualizar estancia
        estancia.setHoraSalida(horaSalida);
        estancia.setMinutos((int) minutos);
        estancia.setValorCobrado(valorCobrado);
        estancia.setEstado("CERRADA");

        estancia = estanciaRepository.save(estancia);

        // Crear evento para cola de mensajería (patrón Outbox)
        crearEventoSalida(estancia);

        return toDTO(estancia);
    }

    /**
     * Crea evento en tabla outbox para notificar la salida.
     */
    private void crearEventoSalida(Estancia estancia) {
        String payload = String.format(
                "{\"estanciaId\":%d,\"vehiculoId\":%d,\"valorCobrado\":%d,\"horaSalida\":\"%s\"}",
                estancia.getId(),
                estancia.getVehiculo().getId(),
                estancia.getValorCobrado(),
                estancia.getHoraSalida().toString()
        );

        EventoOutbox evento = new EventoOutbox();
        evento.setTipoEvento("SALIDA_REGISTRADA");
        evento.setPayload(payload);

        eventoOutboxRepository.save(evento);
    }

    private EstanciaDTO toDTO(Estancia estancia) {
        return new EstanciaDTO(
                estancia.getId(),
                estancia.getVehiculo().getId(),
                estancia.getHoraIngreso(),
                estancia.getHoraSalida(),
                estancia.getMinutos(),
                estancia.getValorCobrado(),
                estancia.getEstado()
        );
    }
}