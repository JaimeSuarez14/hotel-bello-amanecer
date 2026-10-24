package pe.edu.hba.api.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import pe.edu.hba.api.domain.Reserva;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;
import tools.jackson.databind.ObjectMapper;

@Component
public class EventoPublisher {

  private final SqsClient sqsClient;
  private final ObjectMapper mapper;
  private final String queueName;
  private volatile String queueUrl;

  public EventoPublisher(SqsClient sqsClient,
      ObjectMapper mapper,
      @Value("${aws.sqs.queue-reservas}") String queueName) {
    this.sqsClient = sqsClient;
    this.mapper = mapper;
    this.queueName = queueName;
  }

  private String resolveQueueUrl() {
    if (queueUrl == null) {
      queueUrl = sqsClient.getQueueUrl(b -> b.queueName(queueName)).queueUrl();
    }
    return queueUrl;
  }

  public void publicarReservaCreada(Reserva r) {
    try {
      var payload = Map.of(
          "eventType", "RESERVA_CREADA",
          "reservaId", r.getId(),
          "codigo", r.getCodigo(),
          "habitacionId", r.getHabitacion().getId(),
          "huespedEmail", r.getHuespedEmail(),
          "fechaInicio", r.getFechaInicio().toString(),
          "fechaFin", r.getFechaFin().toString(),
          "total", r.getTotal(),
          "timestamp", java.time.OffsetDateTime.now().toString());
      String body = mapper.writeValueAsString(payload);
      sqsClient.sendMessage(SendMessageRequest.builder()
          .queueUrl(resolveQueueUrl())
          .messageBody(body)
          .build());
    } catch (Exception e) {
      throw new IllegalStateException("No se pudo publicar RESERVA_CREADA", e);
    }
  }
}
