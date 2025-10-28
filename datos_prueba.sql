-- ============================================================================
-- Archivo: datos_prueba.sql
-- Descripción: Datos de prueba para el sistema de Pacientes e Historias Clínicas
-- ============================================================================

USE pacienteHistoriaClinica;

-- Insertar pacientes de prueba
INSERT INTO paciente (eliminado, apellido, nombre, dni, fecha_nac) VALUES
(false, 'GARCÍA', 'JUAN CARLOS', '12345678', '1985-03-15'),
(false, 'RODRÍGUEZ', 'MARÍA FERNANDA', '23456789', '1990-07-22'),
(false, 'LÓPEZ', 'PEDRO LUIS', '34567890', '1978-11-08'),
(false, 'MARTÍNEZ', 'ANA SOFÍA', '45678901', '1995-05-30'),
(false, 'GONZÁLEZ', 'ROBERTO DANIEL', '56789012', '1982-09-14'),
(false, 'FERNÁNDEZ', 'LUCÍA BEATRIZ', '67890123', '1988-12-25'),
(false, 'PÉREZ', 'DIEGO MATÍAS', '78901234', '1992-02-18'),
(false, 'SÁNCHEZ', 'CAROLINA ISABEL', '89012345', '1987-06-07');

-- Insertar historias clínicas asociadas a los pacientes
INSERT INTO historiaClinica (eliminado, nro_historia, grupo_sangre, antecedentes, medicacionActual, observaciones, id_paciente) VALUES
(false, 'HC-2024-001', 'O+', 'Hipertensión arterial desde 2015. Alergia a la penicilina.', 'Enalapril 10mg - 1 vez al día', 'Paciente estable. Control cada 6 meses.', 1),
(false, 'HC-2024-002', 'A+', 'Diabetes tipo 2. Antecedentes familiares de enfermedad cardiovascular.', 'Metformina 850mg - 2 veces al día', 'Requiere control de glucemia mensual.', 2),
(false, 'HC-2024-003', 'B-', 'Asma bronquial. Rinitis alérgica estacional.', 'Salbutamol (uso según necesidad)', 'Evitar exposición a alérgenos. Control anual.', 3),
(false, 'HC-2024-004', 'AB+', 'Sin antecedentes patológicos relevantes.', NULL, 'Paciente sana. Control de rutina anual.', 4),
(false, 'HC-2024-005', 'O-', 'Hipotiroidismo. Cirugía de apendicectomía en 2010.', 'Levotiroxina 100mcg - 1 vez al día', 'TSH controlada. Próximo control en 3 meses.', 5);

-- Pacientes sin historia clínica (para probar la relación 1->1)
-- Los pacientes con ID 6, 7 y 8 no tienen historia clínica asociada

-- Insertar algunas historias clínicas adicionales sin asociar (para probar operaciones)
INSERT INTO historiaClinica (eliminado, nro_historia, grupo_sangre, antecedentes, medicacionActual, observaciones, id_paciente) VALUES
(false, 'HC-2024-006', 'A-', 'Sin antecedentes patológicos.', NULL, 'Historia clínica lista para asociar.', NULL),
(false, 'HC-2024-007', 'B+', 'Alergia a ácaros del polvo.', 'Loratadina 10mg según necesidad', 'Historia clínica lista para asociar.', NULL);

-- Verificar datos insertados
SELECT 'Pacientes insertados:' as Info;
SELECT id, apellido, nombre, dni, fecha_nac FROM paciente WHERE eliminado = false;

SELECT '' as '';
SELECT 'Historias Clínicas insertadas:' as Info;
SELECT id, nro_historia, grupo_sangre, id_paciente FROM historiaClinica WHERE eliminado = false;

SELECT '' as '';
SELECT 'Pacientes con Historia Clínica asociada:' as Info;
SELECT 
    p.id as paciente_id,
    p.apellido,
    p.nombre,
    hc.nro_historia,
    hc.grupo_sangre
FROM paciente p
INNER JOIN historiaClinica hc ON p.id = hc.id_paciente
WHERE p.eliminado = false AND hc.eliminado = false;

