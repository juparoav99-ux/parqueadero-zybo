package com.zybo.Parqueadero.service;

import com.zybo.Parqueadero.dto.UsuarioDTO;
import com.zybo.Parqueadero.entity.Usuario;
import com.zybo.Parqueadero.exception.ConflictException;
import com.zybo.Parqueadero.exception.ResourceNotFoundException;
import com.zybo.Parqueadero.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Transactional
    public UsuarioDTO crear(UsuarioDTO dto) {
        if (usuarioRepository.existsByDocumento(dto.getDocumento())) {
            throw new ConflictException("Ya existe un usuario con ese documento");
        }
        if (usuarioRepository.existsByTelefono(dto.getTelefono())) {
            throw new ConflictException("Ya existe un usuario con ese teléfono");
        }

        Usuario usuario = new Usuario();
        usuario.setNombres(dto.getNombres());
        usuario.setDocumento(dto.getDocumento());
        usuario.setTelefono(dto.getTelefono());

        usuario = usuarioRepository.save(usuario);
        return toDTO(usuario);
    }

    public UsuarioDTO obtenerPorId(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + id));
        return toDTO(usuario);
    }

    @Transactional
    public UsuarioDTO actualizar(Long id, UsuarioDTO dto) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + id));

        // Validar documento único si cambió
        if (!usuario.getDocumento().equals(dto.getDocumento())
                && usuarioRepository.existsByDocumento(dto.getDocumento())) {
            throw new ConflictException("Ya existe un usuario con ese documento");
        }

        // Validar teléfono único si cambió
        if (!usuario.getTelefono().equals(dto.getTelefono())
                && usuarioRepository.existsByTelefono(dto.getTelefono())) {
            throw new ConflictException("Ya existe un usuario con ese teléfono");
        }

        usuario.setNombres(dto.getNombres());
        usuario.setDocumento(dto.getDocumento());
        usuario.setTelefono(dto.getTelefono());

        usuario = usuarioRepository.save(usuario);
        return toDTO(usuario);
    }

    @Transactional
    public void eliminar(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new ResourceNotFoundException("Usuario no encontrado con id: " + id);
        }
        usuarioRepository.deleteById(id);
    }

    private UsuarioDTO toDTO(Usuario usuario) {
        return new UsuarioDTO(
                usuario.getId(),
                usuario.getNombres(),
                usuario.getDocumento(),
                usuario.getTelefono()
        );
    }
}