package com.zybo.Parqueadero.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Controlador para la página de inicio de la API.
 * @author Juan Rozo
 */
@RestController
public class HomeController {

    @GetMapping("/")
    public Map<String, Object> home() {
        Map<String, Object> info = new HashMap<>();
        info.put("aplicacion", "Sistema de Gestion de Parqueadero");
        info.put("autor", "Juan Rozo");
        info.put("version", "1.0.0");

        Map<String, String> endpoints = new HashMap<>();
        endpoints.put("usuarios", "POST/GET/PUT/DELETE /usuarios");
        endpoints.put("vehiculos", "POST/GET/PUT/DELETE /vehiculos");
        endpoints.put("ingreso", "POST /estancias/ingreso");
        endpoints.put("salida", "POST /estancias/{id}/salida");
        endpoints.put("eventos", "POST /eventos/dispatch");

        info.put("endpoints", endpoints);
        return info;
    }
}
