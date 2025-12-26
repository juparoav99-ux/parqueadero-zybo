package com.zybo.Parqueadero.controller;

import com.zybo.Parqueadero.dto.EstanciaDTO;
import com.zybo.Parqueadero.dto.IngresoDTO;
import com.zybo.Parqueadero.service.EstanciaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/estancias")
public class EstanciaController {

    @Autowired
    private EstanciaService estanciaService;

    @PostMapping("/ingreso")
    public ResponseEntity<EstanciaDTO> registrarIngreso(@Valid @RequestBody IngresoDTO dto) {
        EstanciaDTO estancia = estanciaService.registrarIngreso(dto);
        return new ResponseEntity<>(estancia, HttpStatus.CREATED);
    }

    @PostMapping("/{id}/salida")
    public ResponseEntity<EstanciaDTO> registrarSalida(@PathVariable Long id) {
        EstanciaDTO estancia = estanciaService.registrarSalida(id);
        return ResponseEntity.ok(estancia);
    }
}