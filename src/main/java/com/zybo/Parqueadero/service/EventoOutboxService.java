package com.zybo.Parqueadero.service;

import com.zybo.Parqueadero.dto.EventoOutboxDTO;
import com.zybo.Parqueadero.entity.EventoOutbox;
import com.zybo.Parqueadero.repository.EventoOutboxRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventoOutboxService {

    private static final Logger logger = LoggerFactory.getLogger(EventoOutboxService.class);

    @Autowired
    private EventoOutboxRepository eventoOutboxRepository;

    @Transactional
    public List<EventoOutboxDTO> despacharEventos() {
        List<EventoOutbox> eventosPendientes = eventoOutboxRepository.findByEstado("PENDIENTE");

        return eventosPendientes.stream()
                .map(evento -> {
                    // Marcar como enviado
                    evento.setEstado("ENVIADO");
                    eventoOutboxRepository.save(evento);

                    // Log del evento
                    logger.info("Evento despachado: tipo={}, payload={}",
                            evento.getTipoEvento(), evento.getPayload());

                    return toDTO(evento);
                })
                .collect(Collectors.toList());
    }

    private EventoOutboxDTO toDTO(EventoOutbox evento) {
        return new EventoOutboxDTO(
                evento.getId(),
                evento.getTipoEvento(),
                evento.getPayload(),
                evento.getEstado(),
                evento.getCreadoEn()
        );
    }
}