-- ============================================================================
-- Archivo: consultas_utiles.sql
-- Descripción: Consultas SQL útiles para verificar el funcionamiento del sistema
--              y para incluir en el informe del TFI
-- ============================================================================

USE pacienteHistoriaClinica;

-- ====================
-- CONSULTAS BÁSICAS
-- ====================

-- 1. Ver todos los pacientes activos (no eliminados)
SELECT 
    id,
    apellido,
    nombre,
    dni,
    fecha_nac,
    eliminado
FROM paciente
WHERE eliminado = false
ORDER BY apellido, nombre;

-- 2. Ver todas las historias clínicas activas
SELECT 
    id,
    nro_historia,
    grupo_sangre,
    antecedentes,
    medicacionActual,
    id_paciente,
    eliminado
FROM historiaClinica
WHERE eliminado = false
ORDER BY nro_historia;

-- ====================
-- RELACIÓN 1→1
-- ====================

-- 3. Pacientes CON historia clínica (INNER JOIN)
SELECT 
    p.id AS paciente_id,
    p.apellido,
    p.nombre,
    p.dni,
    hc.id AS hc_id,
    hc.nro_historia,
    hc.grupo_sangre
FROM paciente p
INNER JOIN historiaClinica hc ON p.id = hc.id_paciente
WHERE p.eliminado = false AND hc.eliminado = false
ORDER BY p.apellido;

-- 4. Pacientes SIN historia clínica (LEFT JOIN)
SELECT 
    p.id,
    p.apellido,
    p.nombre,
    p.dni
FROM paciente p
LEFT JOIN historiaClinica hc ON p.id = hc.id_paciente AND hc.eliminado = false
WHERE p.eliminado = false AND hc.id IS NULL
ORDER BY p.apellido;

-- 5. Historias clínicas sin asociar a ningún paciente
SELECT 
    id,
    nro_historia,
    grupo_sangre
FROM historiaClinica
WHERE eliminado = false AND id_paciente IS NULL
ORDER BY nro_historia;

-- ====================
-- ESTADÍSTICAS
-- ====================

-- 6. Contar pacientes activos
SELECT COUNT(*) AS total_pacientes_activos
FROM paciente
WHERE eliminado = false;

-- 7. Contar pacientes con y sin historia clínica
SELECT 
    CASE 
        WHEN hc.id IS NOT NULL THEN 'Con Historia Clínica'
        ELSE 'Sin Historia Clínica'
    END AS estado,
    COUNT(*) AS cantidad
FROM paciente p
LEFT JOIN historiaClinica hc ON p.id = hc.id_paciente AND hc.eliminado = false
WHERE p.eliminado = false
GROUP BY estado;

-- 8. Distribución por grupo sanguíneo
SELECT 
    grupo_sangre,
    COUNT(*) AS cantidad
FROM historiaClinica
WHERE eliminado = false
GROUP BY grupo_sangre
ORDER BY cantidad DESC;

-- 9. Edad promedio de los pacientes
SELECT 
    AVG(YEAR(CURDATE()) - YEAR(fecha_nac)) AS edad_promedio,
    MIN(YEAR(CURDATE()) - YEAR(fecha_nac)) AS edad_minima,
    MAX(YEAR(CURDATE()) - YEAR(fecha_nac)) AS edad_maxima
FROM paciente
WHERE eliminado = false;

-- ====================
-- VERIFICAR INTEGRIDAD
-- ====================

-- 10. Verificar que no haya pacientes con más de una historia clínica (debe devolver 0 filas)
SELECT 
    id_paciente,
    COUNT(*) AS cantidad_hc
FROM historiaClinica
WHERE eliminado = false AND id_paciente IS NOT NULL
GROUP BY id_paciente
HAVING COUNT(*) > 1;

-- 11. Verificar constraint UNIQUE en nro_historia (intentar duplicados)
-- Esta consulta es solo para documentación, no ejecutar si ya existen datos
-- INSERT INTO historiaClinica (eliminado, nro_historia, grupo_sangre, antecedentes, observaciones, id_paciente)
-- VALUES (false, 'HC-2024-001', 'A+', 'Test', 'Test', NULL);
-- Resultado esperado: ERROR 1062 - Duplicate entry

-- 12. Verificar constraint UNIQUE en DNI (intentar duplicados)
-- Esta consulta es solo para documentación, no ejecutar si ya existen datos
-- INSERT INTO paciente (eliminado, apellido, nombre, dni, fecha_nac)
-- VALUES (false, 'TEST', 'TEST', '12345678', '1990-01-01');
-- Resultado esperado: ERROR 1062 - Duplicate entry

-- ====================
-- BÚSQUEDAS ESPECÍFICAS
-- ====================

-- 13. Buscar paciente por DNI
SELECT 
    p.id,
    p.apellido,
    p.nombre,
    p.dni,
    p.fecha_nac,
    hc.nro_historia,
    hc.grupo_sangre
FROM paciente p
LEFT JOIN historiaClinica hc ON p.id = hc.id_paciente AND hc.eliminado = false
WHERE p.dni = '12345678' AND p.eliminado = false;

-- 14. Buscar historia clínica por número
SELECT 
    hc.id,
    hc.nro_historia,
    hc.grupo_sangre,
    hc.antecedentes,
    hc.medicacionActual,
    hc.observaciones,
    p.apellido,
    p.nombre,
    p.dni
FROM historiaClinica hc
LEFT JOIN paciente p ON hc.id_paciente = p.id AND p.eliminado = false
WHERE hc.nro_historia = 'HC-2024-001' AND hc.eliminado = false;

-- 15. Buscar pacientes por apellido (usando LIKE)
SELECT 
    id,
    apellido,
    nombre,
    dni
FROM paciente
WHERE apellido LIKE 'G%' AND eliminado = false
ORDER BY apellido, nombre;

-- ====================
-- VISTA COMPLETA
-- ====================

-- 16. Vista completa: Paciente + Historia Clínica con toda la información
SELECT 
    -- Datos del Paciente
    p.id AS paciente_id,
    p.apellido,
    p.nombre,
    p.dni,
    p.fecha_nac,
    YEAR(CURDATE()) - YEAR(p.fecha_nac) AS edad,
    
    -- Datos de la Historia Clínica
    hc.id AS hc_id,
    hc.nro_historia,
    hc.grupo_sangre,
    hc.antecedentes,
    hc.medicacionActual,
    hc.observaciones,
    
    -- Estado
    CASE 
        WHEN hc.id IS NOT NULL THEN 'COMPLETO'
        ELSE 'PENDIENTE HC'
    END AS estado_registro
    
FROM paciente p
LEFT JOIN historiaClinica hc ON p.id = hc.id_paciente AND hc.eliminado = false
WHERE p.eliminado = false
ORDER BY p.apellido, p.nombre;

-- ====================
-- BAJA LÓGICA
-- ====================

-- 17. Ver todos los registros eliminados (baja lógica)
SELECT 'Pacientes Eliminados' AS tipo, COUNT(*) AS cantidad
FROM paciente
WHERE eliminado = true
UNION ALL
SELECT 'Historias Clínicas Eliminadas' AS tipo, COUNT(*) AS cantidad
FROM historiaClinica
WHERE eliminado = true;

-- 18. Ver detalle de pacientes eliminados
SELECT 
    id,
    apellido,
    nombre,
    dni,
    fecha_nac
FROM paciente
WHERE eliminado = true;

-- ====================
-- AUDITORÍA
-- ====================

-- 19. Verificar integridad referencial: HCs con id_paciente que no existe
SELECT hc.*
FROM historiaClinica hc
LEFT JOIN paciente p ON hc.id_paciente = p.id
WHERE hc.id_paciente IS NOT NULL 
  AND p.id IS NULL
  AND hc.eliminado = false;

-- 20. Pacientes más antiguos en el sistema (por ID)
SELECT 
    id,
    apellido,
    nombre,
    dni,
    fecha_nac
FROM paciente
WHERE eliminado = false
ORDER BY id ASC
LIMIT 5;

-- ====================
-- PARA EL INFORME
-- ====================

-- 21. Resumen general del sistema (para incluir en el informe)
SELECT 
    'Total Pacientes Activos' AS metrica,
    COUNT(*) AS valor
FROM paciente WHERE eliminado = false
UNION ALL
SELECT 
    'Total Historias Clínicas Activas' AS metrica,
    COUNT(*) AS valor
FROM historiaClinica WHERE eliminado = false
UNION ALL
SELECT 
    'Pacientes con HC' AS metrica,
    COUNT(DISTINCT hc.id_paciente) AS valor
FROM historiaClinica hc WHERE eliminado = false AND id_paciente IS NOT NULL
UNION ALL
SELECT 
    'Pacientes sin HC' AS metrica,
    COUNT(*) AS valor
FROM paciente p
LEFT JOIN historiaClinica hc ON p.id = hc.id_paciente AND hc.eliminado = false
WHERE p.eliminado = false AND hc.id IS NULL
UNION ALL
SELECT 
    'HC sin Asociar' AS metrica,
    COUNT(*) AS valor
FROM historiaClinica WHERE eliminado = false AND id_paciente IS NULL;

-- ====================
-- LIMPIEZA (SOLO PARA PRUEBAS)
-- ====================

-- 22. Eliminar (baja lógica) un paciente específico
-- UPDATE paciente SET eliminado = true WHERE id = 1;

-- 23. Restaurar un paciente eliminado
-- UPDATE paciente SET eliminado = false WHERE id = 1;

-- 24. Eliminar físicamente todos los registros (PELIGRO - solo para resetear BD en desarrollo)
-- DELETE FROM historiaClinica;
-- DELETE FROM paciente;
-- ALTER TABLE historiaClinica AUTO_INCREMENT = 1;
-- ALTER TABLE paciente AUTO_INCREMENT = 1;

-- ============================================================================
-- Notas:
-- - Todas estas consultas son seguras y no modifican datos (excepto las comentadas)
-- - Puedes ejecutarlas en MySQL Workbench o desde la línea de comandos
-- - Son útiles para el informe y para demostrar el funcionamiento del sistema
-- ============================================================================

