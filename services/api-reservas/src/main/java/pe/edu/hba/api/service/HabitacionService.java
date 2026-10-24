package pe.edu.hba.api.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import pe.edu.hba.api.domain.Habitacion;
import pe.edu.hba.api.domain.TipoHabitacion;
import pe.edu.hba.api.dto.HabitacionResponse;
import pe.edu.hba.api.repository.HabitacionRepository;

@Service
public class HabitacionService {

  private final HabitacionRepository repo;

  public HabitacionService(HabitacionRepository repo) {
    this.repo = repo;
  }

  public List<HabitacionResponse> buscarDisponibles(LocalDate fechaInicio,
      LocalDate fechaFin,
      Integer numHuespedes,
      TipoHabitacion tipo) {
    if (fechaInicio == null || fechaFin == null || !fechaFin.isAfter(fechaInicio)) {
      throw new IllegalArgumentException("fechaFin debe ser posterior a fechaInicio");
    }
    if (numHuespedes == null || numHuespedes < 1) {
      throw new IllegalArgumentException("numHuespedes debe ser >= 1");
    }
    List<Habitacion> lista = repo.buscarDisponibles(fechaInicio, fechaFin, numHuespedes, tipo);
    return lista.stream().map(HabitacionResponse::from).toList();
  }
}