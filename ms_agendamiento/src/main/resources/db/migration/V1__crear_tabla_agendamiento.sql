CREATE TABLE boleta (
    id INT AUTO_INCREMENT PRIMARY KEY,
    tipo_pago VARCHAR(25) NOT NULL,
    monto INT NOT NULL
);

CREATE TABLE reserva_hora (
    id INT AUTO_INCREMENT PRIMARY KEY,
    fecha_hora DATETIME NOT NULL,
    estado VARCHAR(50) NOT NULL,
    paciente_id INT NOT NULL,
    psicologo_id INT NOT NULL,
    boleta_id INT NULL,
    UNIQUE (boleta_id),
    CONSTRAINT fk_reserva_boleta FOREIGN KEY (boleta_id) REFERENCES boleta(id)
);