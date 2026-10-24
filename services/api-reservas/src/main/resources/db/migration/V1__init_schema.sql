CREATE TABLE habitacion (
    id BIGSERIAL PRIMARY KEY,
    numero VARCHAR(10) NOT NULL UNIQUE,
    tipo VARCHAR(20) NOT NULL,
    capacidad INTEGER NOT NULL,
    precio_por_noche NUMERIC(10,2) NOT NULL,
    activa BOOLEAN NOT NULL DEFAULT TRUE,
    creada_en TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE reserva (
    id BIGSERIAL PRIMARY KEY,
    codigo VARCHAR(20) NOT NULL UNIQUE,
    huesped_nombre VARCHAR(120) NOT NULL,
    huesped_email VARCHAR(120) NOT NULL,
    huesped_documento VARCHAR(20) NOT NULL,
    habitacion_id BIGINT NOT NULL REFERENCES habitacion(id),
    fecha_inicio DATE NOT NULL,
    fecha_fin DATE NOT NULL,
    num_huespedes INTEGER NOT NULL,
    total NUMERIC(10,2) NOT NULL,
    estado VARCHAR(20) NOT NULL,
    creada_en TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    actualizada_en TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    CONSTRAINT ck_reserva_fechas CHECK (fecha_fin > fecha_inicio),
    CONSTRAINT ck_reserva_estado CHECK (estado IN ('PENDIENTE','CONFIRMADA','RECHAZADA','CANCELADA'))
);

CREATE INDEX idx_reserva_habitacion_fechas
    ON reserva (habitacion_id, fecha_inicio, fecha_fin);

CREATE INDEX idx_reserva_estado ON reserva (estado);