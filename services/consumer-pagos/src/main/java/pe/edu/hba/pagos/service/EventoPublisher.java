package main.java.pe.edu.hba.pagos.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import pe.edu.hba.pagos.domain.Reserva;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

import java.util.Map;

@Component
public class EventoPublisher {
  private final SqsClient sqsClient;
  private final ObjectMapper mapper;
  private final String queueName;
  private volatile String queueUrl;

  public EventoPublisher(SqsClient sqsClient, ObjectMapper mapper,
      @Value("${app.sqs.queue-salida}") String queueName) {
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

  public void publicarReservaConfirmada(Reserva r) {
    try {
      var payload = Map.of(
          "eventType", "RESERVA_CONFIRMADA",
          "reservaId", r.getId(),
          "codigo", r.getCodigo(),
          "huespedEmail", r.getHuespedEmail(),
          "huespedNombre", r.getHuespedNombre(),
          "total", r.getTotal(),
          "timestamp", java.time.OffsetDateTime.now().toString());
      String body = mapper.writeValueAsString(payload);
      sqsClient.sendMessage(SendMessageRequest.builder()
          .queueUrl(resolveQueueUrl())
          .messageBody(body)
          .build());
    } catch (Exception e) {
      throw new IllegalStateException("No se pudo publicar RESERVA_CONFIRMADA", e);
    }
  }
}