package com.zybo.Parqueadero.repository;

import com.zybo.Parqueadero.entity.Estancia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jakarta.persistence.LockModeType;
import java.util.Optional;

@Repository
public interface EstanciaRepository extends JpaRepository<Estancia, Long> {

    @Query("SELECT e FROM Estancia e WHERE e.vehiculo.id = :vehiculoId AND e.estado = 'ABIERTA'")
    Optional<Estancia> findEstanciaAbiertaByVehiculoId(@Param("vehiculoId") Long vehiculoId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT e FROM Estancia e WHERE e.vehiculo.id = :vehiculoId AND e.estado = 'ABIERTA'")
    Optional<Estancia> findEstanciaAbiertaByVehiculoIdWithLock(@Param("vehiculoId") Long vehiculoId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT e FROM Estancia e WHERE e.id = :id")
    Optional<Estancia> findByIdWithLock(@Param("id") Long id);
}