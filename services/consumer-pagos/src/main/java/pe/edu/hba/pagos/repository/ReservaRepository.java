package main.java.pe.edu.hba.pagos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.hba.pagos.domain.Reserva;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {
}