

CREATE TABLE comuna (
    id_comuna INT AUTO_INCREMENT,
    nombre_comuna VARCHAR(255) NOT NULL,
    region_id INT,
    CONSTRAINT PK_comuna PRIMARY KEY (id_comuna),
    CONSTRAINT FK_comuna_region FOREIGN KEY (region_id) 
        REFERENCES region(id_region) ON DELETE SET NULL ON UPDATE CASCADE
);

CREATE TABLE especialidad (
    id_especialidad INT AUTO_INCREMENT,
    nombre_especialidad VARCHAR(255) NOT NULL,
    CONSTRAINT PK_especialidad PRIMARY KEY (id_especialidad)
);

--Creacion tabla intermedia para relacion muchos a muchos entre psicologo y especialidad
CREATE TABLE psicologo_especialidad (
    psicologo_id INT NOT NULL,
    especialidad_id INT NOT NULL,
    PRIMARY KEY (psicologo_id, especialidad_id),
    CONSTRAINT FK_psicologo_especialidad_psicologo FOREIGN KEY (psicologo_id) 
        REFERENCES psicologo(id_psicologo) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT FK_psicologo_especialidad_especialidad FOREIGN KEY (especialidad_id) 
        REFERENCES especialidad(id_especialidad) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE psicologo (
    id_psicologo INT AUTO_INCREMENT,
    rut BIGINT NOT NULL,
    dv_rut VARCHAR(1) NOT NULL,
    p_nombre VARCHAR(255) NOT NULL,
    s_nombre VARCHAR(255),          -- No tiene @NotBlank, por lo tanto acepta NULL
    p_apellido VARCHAR(255) NOT NULL,
    s_apellido VARCHAR(255) NOT NULL,
    
    CONSTRAINT PK_psicologo PRIMARY KEY (id_psicologo)
);

CREATE TABLE region (
    id_region INT AUTO_INCREMENT,
    nombre_region VARCHAR(255) NOT NULL,
    CONSTRAINT PK_region PRIMARY KEY (id_region)

    CONSTRAINT PK_region PRIMARY KEY (id_region),
    CONSTRAINT UQ_region_nombre UNIQUE (nombre_region) 
);


CREATE TABLE sucursal (
    id_sucursal INT AUTO_INCREMENT,
    nombre_sucursal VARCHAR(255) NOT NULL,
    direccion VARCHAR(255) NOT NULL,
    comuna_id INT,
    CONSTRAINT PK_sucursal PRIMARY KEY (id_sucursal),
    CONSTRAINT FK_sucursal_comuna FOREIGN KEY (comuna_id) 
        REFERENCES comuna(id_comuna) ON DELETE SET NULL ON UPDATE CASCADE
);

CREATE TABLE psicologo_sucursal (
    psicologo_id INT NOT NULL,
    sucursal_id INT NOT NULL,
    
    PRIMARY KEY (psicologo_id, sucursal_id),
    
    CONSTRAINT FK_psicologo_sucursal_psicologo FOREIGN KEY (psicologo_id) 
        REFERENCES psicologo(id_psicologo) ON DELETE CASCADE ON UPDATE CASCADE,
        
    CONSTRAINT FK_psicologo_sucursal_sucursal FOREIGN KEY (sucursal_id) 
        REFERENCES sucursal(id_sucursal) ON DELETE CASCADE ON UPDATE CASCADE
);

--Insertar datos de ejemplo en las tablas comuna,especialidad, psicologo , region y sucursal.   


INSERT INTO comuna (nombre_comuna, region_id) VALUES 
('Santiago', 1),
('Providencia', 1),
('Las Condes', 1),
('Viña del Mar', 2),
('Valparaíso', 2);

INSERT INTO especialidad (nombre) VALUES 
('Psicología Clínica'),
('Psicología Infantil y Adolescencia'),
('Terapia Cognitivo-Conductual (TCC)'),
('Neuropsicología'),
('Terapia de Pareja y Familia'),
('Psicología Organizacional / Laboral');


INSERT INTO psicologo (rut, dv_rut, p_nombre, s_nombre, p_apellido, s_apellido) VALUES 
(12345678, '9', 'Pedro', 'Andrés', 'Pérez', 'Gómez'),
(18765432, 'K', 'María', NULL, 'González', 'López'), -- s_nombre es opcional
(15432198, '7', 'Nataly', 'Andrea', 'Muñoz', 'Silva');

INSERT INTO region (nombre_region) VALUES 
('Arica y Parinacota'),
('Tarapacá'),
('Antofagasta'),
('Atacama'),
('Coquimbo'),
('Valparaíso'),
('Región Metropolitana de Santiago'),
('Libertador General Bernardo O''Higgins'),
('Maule'),
('Ñuble'),
('Biobío'),
('La Araucanía'),
('Los Ríos'),
('Los Lagos'),
('Aysén del General Carlos Ibáñez del Campo'),
('Magallanes y de la Antártica Chilena');



INSERT INTO sucursal (nombre_sucursal, direccion, comuna_id) VALUES 
('Sucursal Central', 'Av. Providencia 1234, Oficina 501', 2),
('Sucursal Santiago Centro', 'Paseo Ahumada 85, Piso 3', 1),
('Sucursal Norte', 'Av. Américo Vespucio 990', 1);

