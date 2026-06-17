CREATE TABLE diagnosticos (
    id SERIAL PRIMARY KEY,
    paciente_id INT NOT NULL,
    diagnostico VARCHAR(255) NOT NULL,
    fecha_diagnostico DATE NOT NULL,
    FOREIGN KEY (paciente_id) REFERENCES pacientes(id)
);

CREATE TABLE historial_diagnosticos (
    id SERIAL PRIMARY KEY,
    paciente_id INT NOT NULL,
    diagnostico VARCHAR(255) NOT NULL,
    fecha_diagnostico DATE NOT NULL,
    FOREIGN KEY (paciente_id) REFERENCES pacientes(id)
);

CREATE TABLE pacientes (
    id SERIAL PRIMARY KEY,
    rut VARCHAR(9) NOT NULL UNIQUE,
    p_nombre VARCHAR(25) NOT NULL,
    s_nombre VARCHAR(25),
    p_apellido VARCHAR(25) NOT NULL,
    s_apellido VARCHAR(25),
    fecha_nacimiento DATE NOT NULL,
    genero VARCHAR(50) NOT NULL,
    direccion VARCHAR(150) NOT NULL,
    telefono VARCHAR(15) NOT NULL,
    correo VARCHAR(150) NOT NULL
);

CREATE TABLE prevision (
    id SERIAL PRIMARY KEY,
    paciente_id INT NOT NULL,
    tipo_prevision VARCHAR(50) NOT NULL,
    FOREIGN KEY (paciente_id) REFERENCES pacientes(id)
);

INSERT INTO pacientes (id, rut, p_nombre, s_nombre, p_apellido, s_apellido, fecha_nacimiento, genero, direccion, telefono, correo) VALUES
(1, '12345678-9', 'Juan', 'Carlos', 'Pérez', 'Gómez', '15-05-2000', 'Masculino', 'Cerrillos 123', '987654321', 'juan.perez@gmail.com');

INSERT INTO diagnosticos (id, paciente_id, diagnostico, fecha_diagnostico) VALUES
(1, 1, 'Diabetes Tipo 2', '15-01-2023');

INSERT INTO historial_diagnosticos (id, paciente_id, diagnostico, fecha_diagnostico) VALUES
(1, 1, 'Hipertensión', '15-01-2023');

INSERT INTO prevision (id, paciente_id, tipo_prevision) VALUES
(1, 1,'Fonasa');