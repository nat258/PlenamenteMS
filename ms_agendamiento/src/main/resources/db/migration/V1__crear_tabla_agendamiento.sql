CREATE TABLE reserva_hora (
    id INT AUTO_INCREMENT PRIMARY KEY,
    fecha_hora DATETIME NOT NULL,
    estado VARCHAR(50) NOT NULL,
    paciente_id INT NOT NULL,
    psicologo_id INT NOT NULL
);

CREATE TABLE boleta (
    id INT AUTO_INCREMENT PRIMARY KEY,
    tipo_pago VARCHAR(25) NOT NULL,
    monto INT NOT NULL,
    reserva_hora_id INT NOT NULL,
    CONSTRAINT fk_boleta_reserva FOREIGN KEY (reserva_hora_id) REFERENCES reserva_hora(id),
    CONSTRAINT uk_boleta_reserva UNIQUE (reserva_hora_id)
);