CREATE TABLE region (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) UNIQUE NOT NULL
);

CREATE TABLE comuna (
    id_comuna INT AUTO_INCREMENT PRIMARY KEY,
    nombre_comuna VARCHAR(100) NOT NULL, 
    region_id INT,
    CONSTRAINT fk_comuna_region FOREIGN KEY (region_id) REFERENCES region(id) ON DELETE SET NULL
);

CREATE TABLE sucursal (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(150) NOT NULL,
    direccion VARCHAR(255) NOT NULL,
    comuna_id INT,
    CONSTRAINT fk_sucursal_comuna FOREIGN KEY (comuna_id) REFERENCES comuna(id_comuna) ON DELETE SET NULL
);

CREATE TABLE especialidad (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL
);

CREATE TABLE psicologo (
    id INT AUTO_INCREMENT PRIMARY KEY,
    rut BIGINT NOT NULL,
    dv_rut CHAR(1) NOT NULL,
    p_nombre VARCHAR(100) NOT NULL,  
    s_nombre VARCHAR(100),            
    p_apellido VARCHAR(100) NOT NULL,
    s_apellido VARCHAR(100) NOT NULL
);

CREATE TABLE psicologo_especialidad (
    id_psicologo INT NOT NULL,
    id_especialidad INT NOT NULL,
    PRIMARY KEY (id_psicologo, id_especialidad),
    CONSTRAINT fk_pe_psicologo FOREIGN KEY (id_psicologo) REFERENCES psicologo(id) ON DELETE CASCADE,
    CONSTRAINT fk_pe_especialidad FOREIGN KEY (id_especialidad) REFERENCES especialidad(id) ON DELETE CASCADE
);

CREATE TABLE psicologo_sucursal (
    id_psicologo INT NOT NULL,
    id_sucursal INT NOT NULL,
    PRIMARY KEY (id_psicologo, id_sucursal),
    CONSTRAINT fk_ps_psicologo FOREIGN KEY (id_psicologo) REFERENCES psicologo(id) ON DELETE CASCADE,
    CONSTRAINT fk_ps_sucursal FOREIGN KEY (id_sucursal) REFERENCES sucursal(id) ON DELETE CASCADE
);