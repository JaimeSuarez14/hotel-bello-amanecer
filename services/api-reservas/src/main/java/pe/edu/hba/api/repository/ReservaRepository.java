package pe.edu.hba.api.repository;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import pe.edu.hba.api.domain.EstadoReserva;
import pe.edu.hba.api.domain.Reserva;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {

  Optional<Reserva> findByCodigo(String codigo);

  boolean existsByHabitacionIdAndEstadoInAndFechaInicioLessThanAndFechaFinGreaterThan(
      Long habitacionId,
      Iterable<EstadoReserva> estados,
      LocalDate fechaFin,
      LocalDate fechaInicio);
}
