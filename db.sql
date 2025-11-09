CREATE database pacienteHistoriaClinica;
use pacienteHistoriaClinica;

CREATE TABLE paciente (
id INT auto_increment primary key NOT NULL,
eliminado boolean NOT NULL default false,
apellido varchar(40) NOT NULL,
nombre varchar(40) NOT NULL,
dni varchar(15) NOT NULL unique,
fecha_nac date NOT NULL); 

CREATE TABLE historiaClinica (
id INT auto_increment primary key NOT NULL,
eliminado boolean NOT NULL default false,
nro_historia varchar(20) NOT NULL unique,
grupo_sangre varchar(10) NOT NULL,
antecedentes text NOT NULL,
observaciones text NOT NULL,
medicacionActual varchar(255),
id_paciente INT NOT NULL unique);


ALTER TABLE historiaClinica ADD CONSTRAINT fk_HC_paciente FOREIGN KEY (id_paciente) REFERENCES paciente (id), 
ADD CONSTRAINT chk_HC_eliminado CHECK (eliminado IN (0,1));
-- MySQL no permite usar funciones no determinísticas como CURDATE() en las restricciones CHECK. CURDATE() devuelve la fecha actual, que cambia constantemente, y MySQL requiere que las expresiones en CHECK sean determinísticas.
-- ALTER TABLE paciente ADD CONSTRAINT chk_paciente_eliminado CHECK (eliminado IN (0,1)), 
-- ADD CONSTRAINT chk_paciente_anio_nac CHECK (YEAR(fecha_nac) BETWEEN 1900 AND YEAR(CURDATE()));
ALTER TABLE paciente ADD CONSTRAINT chk_paciente_eliminado CHECK (eliminado IN (0,1)), 
ADD CONSTRAINT chk_paciente_anio_nac CHECK (YEAR(fecha_nac) >= 1900);