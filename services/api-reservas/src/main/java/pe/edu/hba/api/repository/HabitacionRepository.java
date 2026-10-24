package pe.edu.hba.api.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import pe.edu.hba.api.domain.Habitacion;
import pe.edu.hba.api.domain.TipoHabitacion;

public interface HabitacionRepository extends JpaRepository<Habitacion, Long> {

  @Query("""
      SELECT h FROM Habitacion h
      WHERE h.activa = true
        AND h.capacidad >= :numHuespedes
        AND (:tipo IS NULL OR h.tipo = :tipo)
        AND NOT EXISTS (
          SELECT r FROM Reserva r
          WHERE r.habitacion = h
            AND r.estado IN (pe.edu.hba.api.domain.EstadoReserva.PENDIENTE,
                             pe.edu.hba.api.domain.EstadoReserva.CONFIRMADA)
            AND r.fechaInicio < :fechaFin
            AND r.fechaFin   > :fechaInicio
        )
      ORDER BY h.numero
      """)
  List<Habitacion> buscarDisponibles(
      @Param("fechaInicio") LocalDate fechaInicio,
      @Param("fechaFin") LocalDate fechaFin,
      @Param("numHuespedes") Integer numHuespedes,
      @Param("tipo") TipoHabitacion tipo);
}