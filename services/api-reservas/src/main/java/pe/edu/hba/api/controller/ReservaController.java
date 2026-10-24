package pe.edu.hba.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import pe.edu.hba.api.dto.ReservaCreateRequest;
import pe.edu.hba.api.dto.ReservaResponse;
import pe.edu.hba.api.service.ReservaService;

@RestController 
@RequestMapping ("/api/reservas")
public class ReservaController {

    private final ReservaService service;

    public ReservaController(ReservaService service) {
        this.service = service;
    }

    @PostMapping 
    public ResponseEntity<ReservaResponse> crear(@Valid @RequestBody ReservaCreateRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(req));
    }

    @GetMapping ("/{id}")
    public ReservaResponse obtener(@PathVariable Long id) {
        return service.obtener(id);
    }
}