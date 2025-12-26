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

@Service
public class EstanciaService {

    private static final int TARIFA_POR_MINUTO = 100;

    @Autowired
    private EstanciaRepository estanciaRepository;

    @Autowired
    private VehiculoRepository vehiculoRepository;

    @Autowired
    private EventoOutboxRepository eventoOutboxRepository;

    @Transactional
    public EstanciaDTO registrarIngreso(IngresoDTO dto) {
        Vehiculo vehiculo = vehiculoRepository.findById(dto.getVehiculoId())
                .orElseThrow(() -> new ResourceNotFoundException("Vehiculo no encontrado con id: " + dto.getVehiculoId()));

        estanciaRepository.findEstanciaAbiertaByVehiculoIdWithLock(dto.getVehiculoId())
                .ifPresent(e -> {
                    throw new ConflictException("El vehiculo ya tiene una estancia abierta");
                });

        Estancia estancia = new Estancia();
        estancia.setVehiculo(vehiculo);

        estancia = estanciaRepository.save(estancia);
        return toDTO(estancia);
    }

    @Transactional
    public EstanciaDTO registrarSalida(Long estanciaId) {
        Estancia estancia = estanciaRepository.findByIdWithLock(estanciaId)
                .orElseThrow(() -> new ResourceNotFoundException("Estancia no encontrada con id: " + estanciaId));

        if ("CERRADA".equals(estancia.getEstado())) {
            throw new ConflictException("La estancia ya esta cerrada");
        }

        LocalDateTime horaSalida = LocalDateTime.now();
        Duration duracion = Duration.between(estancia.getHoraIngreso(), horaSalida);
        long minutos = duracion.toMinutes();

        if (duracion.toSecondsPart() > 0) {
            minutos++;
        }

        int valorCobrado = (int) minutos * TARIFA_POR_MINUTO;

        estancia.setHoraSalida(horaSalida);
        estancia.setMinutos((int) minutos);
        estancia.setValorCobrado(valorCobrado);
        estancia.setEstado("CERRADA");

        estancia = estanciaRepository.save(estancia);
        crearEventoSalida(estancia);

        return toDTO(estancia);
    }

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