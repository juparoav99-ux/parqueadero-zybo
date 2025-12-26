package com.zybo.Parqueadero.repository;

import com.zybo.Parqueadero.entity.EventoOutbox;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventoOutboxRepository extends JpaRepository<EventoOutbox, Long> {

    List<EventoOutbox> findByEstado(String estado);
}