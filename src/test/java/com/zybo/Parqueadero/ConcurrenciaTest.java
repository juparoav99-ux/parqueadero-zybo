package com.zybo.Parqueadero;

import com.zybo.Parqueadero.dto.IngresoDTO;
import com.zybo.Parqueadero.entity.Usuario;
import com.zybo.Parqueadero.entity.Vehiculo;
import com.zybo.Parqueadero.repository.EstanciaRepository;
import com.zybo.Parqueadero.repository.EventoOutboxRepository;
import com.zybo.Parqueadero.repository.UsuarioRepository;
import com.zybo.Parqueadero.repository.VehiculoRepository;
import com.zybo.Parqueadero.service.EstanciaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class ConcurrenciaTest {

    @Autowired
    private EstanciaService estanciaService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private VehiculoRepository vehiculoRepository;

    @Autowired
    private EstanciaRepository estanciaRepository;

    @Autowired
    private EventoOutboxRepository eventoOutboxRepository;

    private Vehiculo vehiculo;

    @BeforeEach
    void setUp() {
        eventoOutboxRepository.deleteAll();
        estanciaRepository.deleteAll();
        vehiculoRepository.deleteAll();
        usuarioRepository.deleteAll();

        String uniqueId = String.valueOf(System.currentTimeMillis() % 100000);

        Usuario usuario = new Usuario();
        usuario.setNombres("Test User");
        usuario.setDocumento("DOC" + uniqueId);
        usuario.setTelefono("TEL" + uniqueId);
        usuario = usuarioRepository.save(usuario);

        vehiculo = new Vehiculo();
        vehiculo.setPlaca("T" + uniqueId);
        vehiculo.setUsuario(usuario);
        vehiculo = vehiculoRepository.save(vehiculo);
    }

    @Test
    void testDosIngresosSimultaneos_SoloUnoDebeTenerExito() throws InterruptedException {
        int numeroHilos = 2;
        ExecutorService executor = Executors.newFixedThreadPool(numeroHilos);
        CountDownLatch latch = new CountDownLatch(1);
        AtomicInteger exitos = new AtomicInteger(0);
        AtomicInteger fallos = new AtomicInteger(0);

        for (int i = 0; i < numeroHilos; i++) {
            executor.submit(() -> {
                try {
                    latch.await();
                    IngresoDTO dto = new IngresoDTO(vehiculo.getId());
                    estanciaService.registrarIngreso(dto);
                    exitos.incrementAndGet();
                } catch (Exception e) {
                    fallos.incrementAndGet();
                }
            });
        }

        latch.countDown();
        executor.shutdown();
        while (!executor.isTerminated()) {
            Thread.sleep(100);
        }

        assertEquals(1, exitos.get(), "Solo un ingreso debe tener exito");
        assertEquals(1, fallos.get(), "Un ingreso debe fallar por conflicto");
    }

    @Test
    void testDosSalidasSimultaneas_SoloUnaDebeTenerExito() throws InterruptedException {
        IngresoDTO ingresoDTO = new IngresoDTO(vehiculo.getId());
        var estanciaDTO = estanciaService.registrarIngreso(ingresoDTO);
        Long estanciaId = estanciaDTO.getId();

        int numeroHilos = 2;
        ExecutorService executor = Executors.newFixedThreadPool(numeroHilos);
        CountDownLatch latch = new CountDownLatch(1);
        AtomicInteger exitos = new AtomicInteger(0);
        AtomicInteger fallos = new AtomicInteger(0);

        for (int i = 0; i < numeroHilos; i++) {
            executor.submit(() -> {
                try {
                    latch.await();
                    estanciaService.registrarSalida(estanciaId);
                    exitos.incrementAndGet();
                } catch (Exception e) {
                    fallos.incrementAndGet();
                }
            });
        }

        latch.countDown();
        executor.shutdown();
        while (!executor.isTerminated()) {
            Thread.sleep(100);
        }

        assertEquals(1, exitos.get(), "Solo una salida debe tener exito");
        assertEquals(1, fallos.get(), "Una salida debe fallar por conflicto");
    }
}
