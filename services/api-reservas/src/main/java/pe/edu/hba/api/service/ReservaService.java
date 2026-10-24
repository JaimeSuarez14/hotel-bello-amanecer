package pe.edu.hba.api.service;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pe.edu.hba.api.domain.EstadoReserva;
import pe.edu.hba.api.domain.Habitacion;
import pe.edu.hba.api.domain.Reserva;
import pe.edu.hba.api.dto.ReservaCreateRequest;
import pe.edu.hba.api.dto.ReservaResponse;
import pe.edu.hba.api.exception.RecursoNoEncontradoException;
import pe.edu.hba.api.exception.ReglaNegocioException;
import pe.edu.hba.api.repository.HabitacionRepository;
import pe.edu.hba.api.repository.ReservaRepository;

@Service 
public class ReservaService {

    private final ReservaRepository reservaRepo;
    private final HabitacionRepository habitacionRepo;
    private final EventoPublisher publisher;

    public ReservaService(ReservaRepository reservaRepo,
                          HabitacionRepository habitacionRepo,
                          EventoPublisher publisher) {
        this.reservaRepo = reservaRepo;
        this.habitacionRepo = habitacionRepo;
        this.publisher = publisher;
    }

    @Transactional 
    public ReservaResponse crear(ReservaCreateRequest req) {

        Habitacion hab = habitacionRepo.findById(req.habitacionId())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Habitación no encontrada: " + req.habitacionId()));

        if (!Boolean.TRUE.equals(hab.getActiva())) {
            throw new ReglaNegocioException("La habitación no está activa");
        }
        if (req.numHuespedes() > hab.getCapacidad()) {
            throw new ReglaNegocioException(
                    "La habitación admite hasta " + hab.getCapacidad() + " huéspedes");
        }

        boolean solapa = reservaRepo
                .existsByHabitacionIdAndEstadoInAndFechaInicioLessThanAndFechaFinGreaterThan(
                        hab.getId(),
                        List.of(EstadoReserva.PENDIENTE, EstadoReserva.CONFIRMADA),
                        req.fechaFin(),
                        req.fechaInicio()
                );
        if (solapa) {
            throw new ReglaNegocioException("La habitación ya tiene una reserva en ese rango");
        }

        long noches = ChronoUnit.DAYS.between(req.fechaInicio(), req.fechaFin());
        BigDecimal total = hab.getPrecioPorNoche().multiply(BigDecimal.valueOf(noches));

        Reserva r = new Reserva();
        r.setCodigo(generarCodigo());
        r.setHuespedNombre(req.huespedNombre());
        r.setHuespedEmail(req.huespedEmail());
        r.setHuespedDocumento(req.huespedDocumento());
        r.setHabitacion(hab);
        r.setFechaInicio(req.fechaInicio());
        r.setFechaFin(req.fechaFin());
        r.setNumHuespedes(req.numHuespedes());
        r.setTotal(total);
        r.setEstado(EstadoReserva.PENDIENTE);

        r = reservaRepo.save(r);

        publisher.publicarReservaCreada(r);

        return ReservaResponse.from(r);
    }

    @Transactional(readOnly = true)
    public ReservaResponse obtener(Long id) {
        return reservaRepo.findById(id)
                .map(ReservaResponse::from)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Reserva no encontrada: " + id));
    }

    private String generarCodigo() {
        return "HBA-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}