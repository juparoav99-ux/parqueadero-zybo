package com.zybo.Parqueadero.controller;

import com.zybo.Parqueadero.dto.VehiculoDTO;
import com.zybo.Parqueadero.service.VehiculoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/vehiculos")
public class VehiculoController {

    @Autowired
    private VehiculoService vehiculoService;

    @PostMapping
    public ResponseEntity<VehiculoDTO> crear(@Valid @RequestBody VehiculoDTO dto) {
        VehiculoDTO creado = vehiculoService.crear(dto);
        return new ResponseEntity<>(creado, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VehiculoDTO> obtenerPorId(@PathVariable Long id) {
        VehiculoDTO vehiculo = vehiculoService.obtenerPorId(id);
        return ResponseEntity.ok(vehiculo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<VehiculoDTO> actualizar(@PathVariable Long id, @Valid @RequestBody VehiculoDTO dto) {
        VehiculoDTO actualizado = vehiculoService.actualizar(id, dto);
        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        vehiculoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}