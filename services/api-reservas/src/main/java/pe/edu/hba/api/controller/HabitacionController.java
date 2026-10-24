package pe.edu.hba.api.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import pe.edu.hba.api.domain.TipoHabitacion;
import pe.edu.hba.api.dto.HabitacionResponse;
import pe.edu.hba.api.service.HabitacionService;

@RestController 
@RequestMapping ("/api/habitaciones")
public class HabitacionController {

    private final HabitacionService service;

    public HabitacionController(HabitacionService service) {
        this.service = service;
    }

    @GetMapping ("/disponibilidad")
    public List<HabitacionResponse> disponibilidad(
            @RequestParam @DateTimeFormat (iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin,
            @RequestParam Integer numHuespedes,
            @RequestParam(required = false) TipoHabitacion tipo) {
        return service.buscarDisponibles(fechaInicio, fechaFin, numHuespedes, tipo);
    }
}