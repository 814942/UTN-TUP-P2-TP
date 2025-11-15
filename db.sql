CREATE database pacienteHistoriaClinica;
use pacienteHistoriaClinica;

CREATE TABLE paciente (
id INT auto_increment primary key NOT NULL,
eliminado boolean NOT NULL default false,
apellido varchar(40) NOT NULL,
nombre varchar(40) NOT NULL,
dni varchar(15) NOT NULL unique,
fecha_nac date NOT NULL,
CONSTRAINT chk_paciente_eliminado CHECK (eliminado IN (0,1))); 

CREATE TABLE historiaClinica (
id INT auto_increment primary key NOT NULL,
eliminado boolean NOT NULL default false,
nro_historia varchar(20) NOT NULL unique,
grupo_sangre varchar(10) NOT NULL,
antecedentes text NOT NULL,
observaciones text NOT NULL,
medicacionActual varchar(255),
id_paciente INT NOT NULL unique,
CONSTRAINT fk_HC_paciente FOREIGN KEY (id_paciente) REFERENCES paciente (id),
CONSTRAINT chk_HC_eliminado CHECK (eliminado IN (0,1)));

DELIMITER $$
CREATE TRIGGER trg_validar_fecha_nacimiento
BEFORE INSERT ON paciente
FOR EACH ROW
BEGIN
    IF NEW.fecha_nac > CURDATE() THEN
       SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'La fecha de nacimiento no puede ser una fecha futura.';
    END IF;
END$$
