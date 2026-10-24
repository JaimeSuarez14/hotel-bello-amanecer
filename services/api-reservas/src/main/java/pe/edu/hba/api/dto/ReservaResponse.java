package pe.edu.hba.api.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;

import pe.edu.hba.api.domain.Reserva;

public record ReservaResponse(
    Long id,
    String codigo,
    Long habitacionId,
    String habitacionNumero,
    String huespedNombre,
    String huespedEmail,
    LocalDate fechaInicio,
    LocalDate fechaFin,
    Integer numHuespedes,
    BigDecimal total,
    String estado,
    OffsetDateTime creadaEn) {
  public static ReservaResponse from(Reserva r) {
    return new ReservaResponse(
        r.getId(),
        r.getCodigo(),
        r.getHabitacion().getId(),
        r.getHabitacion().getNumero(),
        r.getHuespedNombre(),
        r.getHuespedEmail(),
        r.getFechaInicio(),
        r.getFechaFin(),
        r.getNumHuespedes(),
        r.getTotal(),
        r.getEstado().name(),
        r.getCreadaEn());
  }
}