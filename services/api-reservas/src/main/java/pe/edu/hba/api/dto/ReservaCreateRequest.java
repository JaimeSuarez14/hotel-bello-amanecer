package pe.edu.hba.api.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ReservaCreateRequest(
        @NotBlank @Size (max = 120) String huespedNombre,
        @NotBlank @Email @Size(max = 120) String huespedEmail,
        @NotBlank @Size(max = 20) String huespedDocumento,
        @NotNull Long habitacionId,
        @NotNull @FutureOrPresent LocalDate fechaInicio,
        @NotNull @FutureOrPresent LocalDate fechaFin,
        @NotNull @Min (1) Integer numHuespedes
) {}
