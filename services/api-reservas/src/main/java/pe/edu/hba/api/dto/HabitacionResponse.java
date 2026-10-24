package pe.edu.hba.api.dto;

import java.math.BigDecimal;

import pe.edu.hba.api.domain.Habitacion;

public record HabitacionResponse(
    Long id,
    String numero,
    String tipo,
    Integer capacidad,
    BigDecimal precioPorNoche) {
  public static HabitacionResponse from(Habitacion h) {
    return new HabitacionResponse(
        h.getId(), h.getNumero(), h.getTipo().name(),
        h.getCapacidad(), h.getPrecioPorNoche());
  }
}