package pe.edu.hba.api.domain;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Entity 
@Table (name = "reserva")
public class Reserva {

    @Id 
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @Column (nullable = false, unique = true, length = 20)
    private String codigo;

    @Column(name = "huesped_nombre", nullable = false, length = 120)
    private String huespedNombre;

    @Column(name = "huesped_email", nullable = false, length = 120)
    private String huespedEmail;

    @Column(name = "huesped_documento", nullable = false, length = 20)
    private String huespedDocumento;

    @ManyToOne (fetch = FetchType.LAZY, optional = false)
    @JoinColumn (name = "habitacion_id", nullable = false)
    private Habitacion habitacion;

    @Column(name = "fecha_inicio", nullable = false)
    private LocalDate fechaInicio;

    @Column(name = "fecha_fin", nullable = false)
    private LocalDate fechaFin;

    @Column(name = "num_huespedes", nullable = false)
    private Integer numHuespedes;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal total;

    @Enumerated (EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoReserva estado;

    @Column(name = "creada_en", nullable = false, updatable = false)
    private OffsetDateTime creadaEn;

    @Column(name = "actualizada_en", nullable = false)
    private OffsetDateTime actualizadaEn;

    @PrePersist 
    void prePersist() {
        var now = OffsetDateTime.now();
        if (creadaEn == null) creadaEn = now;
        actualizadaEn = now;
    }

    @PreUpdate 
    void preUpdate() {
        actualizadaEn = OffsetDateTime.now();
    }

    // getters y setters (omito por brevedad, genera con IDE o Lombok)
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }
    public String getHuespedNombre() { return huespedNombre; }
    public void setHuespedNombre(String huespedNombre) { this.huespedNombre = huespedNombre; }
    public String getHuespedEmail() { return huespedEmail; }
    public void setHuespedEmail(String huespedEmail) { this.huespedEmail = huespedEmail; }
    public String getHuespedDocumento() { return huespedDocumento; }
    public void setHuespedDocumento(String huespedDocumento) { this.huespedDocumento = huespedDocumento; }
    public Habitacion getHabitacion() { return habitacion; }
    public void setHabitacion(Habitacion habitacion) { this.habitacion = habitacion; }
    public LocalDate getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDate fechaInicio) { this.fechaInicio = fechaInicio; }
    public LocalDate getFechaFin() { return fechaFin; }
    public void setFechaFin(LocalDate fechaFin) { this.fechaFin = fechaFin; }
    public Integer getNumHuespedes() { return numHuespedes; }
    public void setNumHuespedes(Integer numHuespedes) { this.numHuespedes = numHuespedes; }
    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }
    public EstadoReserva getEstado() { return estado; }
    public void setEstado(EstadoReserva estado) { this.estado = estado; }
    public OffsetDateTime getCreadaEn() { return creadaEn; }
    public OffsetDateTime getActualizadaEn() { return actualizadaEn; }
}