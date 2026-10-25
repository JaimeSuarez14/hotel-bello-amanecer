package pe.edu.hba.pagos.consumer;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.awspring.cloud.sqs.annotation.SqsListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.hba.pagos.domain.EstadoReserva;
import pe.edu.hba.pagos.domain.Reserva;
import pe.edu.hba.pagos.repository.ReservaRepository;
import pe.edu.hba.pagos.service.EventoPublisher;
import pe.edu.hba.pagos.service.SimuladorPago;

@Component
public class PagoConsumer {
    private static final Logger log = LoggerFactory.getLogger(PagoConsumer.class);

    private final ReservaRepository reservaRepo;
    private final SimuladorPago simulador;
    private final EventoPublisher publisher;
    private final ObjectMapper mapper;

    public PagoConsumer(ReservaRepository reservaRepo, SimuladorPago simulador,
                        EventoPublisher publisher, ObjectMapper mapper) {
        this.reservaRepo = reservaRepo;
        this.simulador = simulador;
        this.publisher = publisher;
        this.mapper = mapper;
    }

    @SqsListener("${app.sqs.queue-entrada}")
    @Transactional
    public void procesarReservaCreada(String mensajeJson) {
        log.info("Evento recibido en q-reservas: {}", mensajeJson);

        try {
            JsonNode root = mapper.readTree(mensajeJson);
            Long reservaId = root.get("reservaId").asLong();

            // 1. IDEMPOTENCIA: Buscar la reserva
            Reserva reserva = reservaRepo.findById(reservaId).orElse(null);
            if (reserva == null) {
                log.error("Reserva {} no encontrada. Se descarta el mensaje.", reservaId);
                return; // Confirmamos el mensaje para que no vuelva a la cola
            }

            // Si ya fue procesada, ignoramos (SQS puede entregar el mensaje 2 veces)
            if (reserva.getEstado() != EstadoReserva.PENDIENTE) {
                log.info("Reserva {} ya fue procesada previamente (Estado: {}). Se ignora.",
                        reservaId, reserva.getEstado());
                return;
            }

            // 2. PROCESAR PAGO SIMULADO
            boolean pagoExitoso = simulador.procesarPago(reserva.getId(), reserva.getTotal());

            // 3. ACTUALIZAR ESTADO
            if (pagoExitoso) {
                reserva.setEstado(EstadoReserva.CONFIRMADA);
                reservaRepo.save(reserva);
                log.info("Reserva {} actualizada a CONFIRMADA en BD", reservaId);

                // 4. PUBLICAR SIGUIENTE EVENTO
                publisher.publicarReservaConfirmada(reserva);
                log.info("Evento RESERVA_CONFIRMADA publicado en q-notificaciones");
            } else {
                reserva.setEstado(EstadoReserva.RECHAZADA);
                reservaRepo.save(reserva);
                log.warn("Reserva {} actualizada a RECHAZADA (pago fallido)", reservaId);
            }

        } catch (Exception e) {
            // Si lanzamos una excepción, @SqsListener NO borra el mensaje de la cola.
            // Volverá a intentarse (hasta el límite de reintentos y luego irá a la DLQ).
            log.error("Error procesando el mensaje. Se reintentará.", e);
            throw new RuntimeException("Fallo al procesar el pago", e);
        }
    }
}