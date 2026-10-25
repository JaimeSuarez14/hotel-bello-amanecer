package main.java.pe.edu.hba.pagos.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "reserva")
public class Reserva {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 20)
    private String codigo;

    @Column(name = "huesped_email", nullable = false, length = 120)
    private String huespedEmail;

    @Column(name = "huesped_nombre", nullable = false, length = 120)
    private String huespedNombre;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal total;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoReserva estado;

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }
    public String getHuespedEmail() { return huespedEmail; }
    public void setHuespedEmail(String huespedEmail) { this.huespedEmail = huespedEmail; }
    public String getHuespedNombre() { return huespedNombre; }
    public void setHuespedNombre(String huespedNombre) { this.huespedNombre = huespedNombre; }
    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }
    public EstadoReserva getEstado() { return estado; }
    public void setEstado(EstadoReserva estado) { this.estado = estado; }
}