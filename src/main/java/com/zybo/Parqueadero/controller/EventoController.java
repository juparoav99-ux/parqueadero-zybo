package com.zybo.Parqueadero.controller;

import com.zybo.Parqueadero.dto.EventoOutboxDTO;
import com.zybo.Parqueadero.service.EventoOutboxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/eventos")
public class EventoController {

    @Autowired
    private EventoOutboxService eventoOutboxService;

    @PostMapping("/dispatch")
    public ResponseEntity<List<EventoOutboxDTO>> despacharEventos() {
        List<EventoOutboxDTO> eventos = eventoOutboxService.despacharEventos();
        return ResponseEntity.ok(eventos);
    }
}