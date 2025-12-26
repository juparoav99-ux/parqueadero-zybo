package com.zybo.Parqueadero.service;

import com.zybo.Parqueadero.dto.VehiculoDTO;
import com.zybo.Parqueadero.entity.Usuario;
import com.zybo.Parqueadero.entity.Vehiculo;
import com.zybo.Parqueadero.exception.ConflictException;
import com.zybo.Parqueadero.exception.ResourceNotFoundException;
import com.zybo.Parqueadero.repository.UsuarioRepository;
import com.zybo.Parqueadero.repository.VehiculoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class VehiculoService {

    @Autowired
    private VehiculoRepository vehiculoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Transactional
    public VehiculoDTO crear(VehiculoDTO dto) {
        if (vehiculoRepository.existsByPlaca(dto.getPlaca())) {
            throw new ConflictException("Ya existe un vehículo con esa placa");
        }

        Usuario usuario = usuarioRepository.findById(dto.getUsuarioId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + dto.getUsuarioId()));

        Vehiculo vehiculo = new Vehiculo();
        vehiculo.setPlaca(dto.getPlaca());
        vehiculo.setUsuario(usuario);

        vehiculo = vehiculoRepository.save(vehiculo);
        return toDTO(vehiculo);
    }

    public VehiculoDTO obtenerPorId(Long id) {
        Vehiculo vehiculo = vehiculoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehículo no encontrado con id: " + id));
        return toDTO(vehiculo);
    }

    @Transactional
    public VehiculoDTO actualizar(Long id, VehiculoDTO dto) {
        Vehiculo vehiculo = vehiculoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehículo no encontrado con id: " + id));

        if (!vehiculo.getPlaca().equals(dto.getPlaca())
                && vehiculoRepository.existsByPlaca(dto.getPlaca())) {
            throw new ConflictException("Ya existe un vehículo con esa placa");
        }

        Usuario usuario = usuarioRepository.findById(dto.getUsuarioId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + dto.getUsuarioId()));

        vehiculo.setPlaca(dto.getPlaca());
        vehiculo.setUsuario(usuario);

        vehiculo = vehiculoRepository.save(vehiculo);
        return toDTO(vehiculo);
    }

    @Transactional
    public void eliminar(Long id) {
        if (!vehiculoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Vehículo no encontrado con id: " + id);
        }
        vehiculoRepository.deleteById(id);
    }

    private VehiculoDTO toDTO(Vehiculo vehiculo) {
        return new VehiculoDTO(
                vehiculo.getId(),
                vehiculo.getPlaca(),
                vehiculo.getUsuario().getId()
        );
    }
}