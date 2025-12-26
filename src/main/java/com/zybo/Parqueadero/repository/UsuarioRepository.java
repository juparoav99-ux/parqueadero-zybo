package com.zybo.Parqueadero.repository;

import com.zybo.Parqueadero.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByDocumento(String documento);

    Optional<Usuario> findByTelefono(String telefono);

    boolean existsByDocumento(String documento);

    boolean existsByTelefono(String telefono);
}