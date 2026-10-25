package pe.edu.hba.pagos.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class SimuladorPago {
    private static final Logger log = LoggerFactory.getLogger(SimuladorPago.class);
    private final Random random = new Random();

    @Value("${app.pago.latencia-ms:2000}")
    private long latenciaMs;

    @Value("${app.pago.fallo-porcentaje:0}")
    private int falloPorcentaje;

    /**
     * Simula el procesamiento de un pago.
     * @return true si el pago es exitoso, false si es rechazado.
     */
    public boolean procesarPago(Long reservaId, java.math.BigDecimal total) {
        try {
            log.info("Iniciando pago para reserva {} por monto {}", reservaId, total);
            Thread.sleep(latenciaMs);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        }

        if (falloPorcentaje > 0 && random.nextInt(100) < falloPorcentaje) {
            log.warn("Pago RECHAZADO para reserva {} (simulación de fallo)", reservaId);
            return false;
        }

        log.info("Pago APROBADO para reserva {}", reservaId);
        return true;
    }
}