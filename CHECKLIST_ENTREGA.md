# ✅ Checklist de Entrega - TFI Programación 2

## 📋 Requisitos Obligatorios

### 1. Estructura del Proyecto

- [x] Paquete `config/` con DatabaseConnection
- [x] Paquete `entities/` con Paciente y HistoriaClinica
- [x] Paquete `dao/` con interfaces y DAOs concretos
- [x] Paquete `service/` con lógica de negocio
- [x] Paquete `main/` con Main y AppMenu
- [x] Paquete `exceptions/` con excepciones personalizadas
- [x] Paquete `enums/` con GrupoSanguineo
- [x] Paquete `util/` con Validador

### 2. Diseño

- [x] UML de clases (falta crear la imagen)
- [x] Relación 1→1 unidireccional explícita (Paciente → HistoriaClinica)
- [x] Atributos con tipo y visibilidad
- [x] Métodos con firma y visibilidad

### 3. Entidades

- [x] Paciente con id, eliminado y atributos propios
- [x] HistoriaClinica con id, eliminado y atributos propios
- [x] Paciente contiene referencia a HistoriaClinica
- [x] Constructores (vacío y completo)
- [x] Getters y Setters
- [x] toString() legible

### 4. Base de Datos

- [x] Archivo `db.sql` con CREATE DATABASE y CREATE TABLE
- [x] Claves primarias definidas
- [x] Clave foránea única (id_paciente UNIQUE en historiaClinica)
- [x] Restricciones CHECK para eliminado
- [x] ON DELETE CASCADE
- [x] Archivo `datos_prueba.sql` con INSERTs

### 5. DAO

- [x] Interfaz GenericDao<T> con métodos CRUD
- [x] PacienteDao implementa GenericDao<Paciente>
- [x] HistoriaClinicaDao implementa GenericDao<HistoriaClinica>
- [x] Uso de PreparedStatement en todas las operaciones
- [x] Métodos que aceptan Connection externa (para transacciones)
- [x] Método adicional: buscarPorDni() en PacienteDao
- [x] Método adicional: buscarPorNroHistoria() en HistoriaClinicaDao

### 6. Service (Transacciones)

- [x] Interfaz GenericService<T>
- [x] PacienteService y HistoriaClinicaService
- [x] setAutoCommit(false) para iniciar transacción
- [x] commit() si todo OK
- [x] rollback() ante errores
- [x] Restablecer autoCommit(true)
- [x] Cerrar recursos en finally
- [x] Validaciones de campos obligatorios
- [x] Validaciones de formatos (DNI, fechas)
- [x] Validación de unicidad (DNI, número de historia)
- [x] Validación de relación 1→1

### 7. AppMenu (Consola)

- [x] Main invoca AppMenu
- [x] Entradas convertidas a mayúsculas donde aplica
- [x] CRUD completo de Paciente
- [x] CRUD completo de HistoriaClinica
- [x] Búsqueda por DNI (campo relevante)
- [x] Búsqueda por número de historia
- [x] Manejo de entradas inválidas
- [x] Manejo de IDs inexistentes
- [x] Manejo de errores de BD
- [x] Mensajes claros de éxito/error
- [x] Operación transaccional: Crear paciente con HC

### 8. Repositorio GitHub

- [ ] Repositorio público creado
- [x] Código fuente completo
- [x] README.md completo con:
  - [x] Descripción del dominio
  - [x] Requisitos (Java/BD)
  - [x] Pasos para crear la base
  - [x] Cómo compilar y ejecutar
  - [ ] Enlace al video (falta subir video)
- [x] Archivo `db.sql` (creación)
- [x] Archivo `datos_prueba.sql` (datos de prueba)
- [ ] UML (imagen .png/.jpg) (falta crear)
- [ ] Informe (PDF) (falta crear)
- [x] .gitignore configurado
- [x] pom.xml para Maven

### 9. Informe (PDF, 6-8 páginas)

- [ ] Integrantes (4) y roles
- [ ] Elección del dominio y justificación
- [ ] Diseño: decisiones clave (1→1, FK única)
- [ ] UML incluido
- [ ] Arquitectura por capas
- [ ] Persistencia: estructura de BD
- [ ] Orden de operaciones en transacciones
- [ ] Dónde se hace commit/rollback
- [ ] Validaciones y reglas de negocio
- [ ] Pruebas realizadas (capturas)
- [ ] Consultas SQL útiles
- [ ] Conclusiones y mejoras futuras
- [ ] Fuentes citadas (incluir IA si se usó)

### 10. Video (10-15 minutos)

- [ ] Presentación de los 4 integrantes con rostro visible
- [ ] Demostración del flujo CRUD
- [ ] Demostración de la relación 1→1 funcionando
- [ ] Explicación de código (entities, dao, service, menú)
- [ ] Mostrar operación transaccional
- [ ] Evidenciar rollback ante fallo simulado
- [ ] Video subido (YouTube o similar)
- [ ] Enlace agregado al README

## 🎯 Criterios de Evaluación

### Correctitud Funcional
- [x] CRUD completo de Paciente
- [x] CRUD completo de HistoriaClinica
- [x] Relación 1→1 real en código
- [x] Relación 1→1 real en base de datos
- [x] Baja lógica funcionando

### Diseño/Arquitectura
- [x] Patrón DAO implementado
- [x] Patrón Service implementado
- [x] Responsabilidades claras por capa
- [x] Validaciones en Service
- [x] Transacciones con commit/rollback

### Calidad de Código
- [x] Código legible y bien estructurado
- [x] Nombres descriptivos (variables, métodos, clases)
- [x] Manejo de excepciones en todas las capas
- [x] Uso correcto de PreparedStatement
- [x] Código en español (según requerimiento)

### Persistencia
- [x] Integridad referencial garantizada
- [x] Unicidad para relación 1→1
- [x] Script SQL reproducible (db.sql)
- [x] Script con datos de prueba (datos_prueba.sql)

### Documentación
- [x] README claro y completo
- [ ] Informe sólido (pendiente)
- [ ] UML coherente (pendiente crear imagen)

### Presentación
- [ ] Video dentro del tiempo (10-15 min)
- [ ] Explicación técnica clara
- [ ] Participación equitativa de los 4 integrantes
- [ ] Demostración completa del sistema

### Entrega
- [ ] Repositorio público accesible
- [x] Proyecto compilable desde cero
- [x] Proyecto ejecutable siguiendo el README
- [ ] Todos los entregables presentes

## 📝 Tareas Pendientes

### Alta Prioridad
1. [ ] **Crear diagrama UML** (PlantUML, Draw.io, o Lucidchart)
2. [ ] **Redactar informe PDF** (6-8 páginas)
3. [ ] **Grabar video** (10-15 minutos)
4. [ ] **Subir video** (YouTube, Google Drive, etc.)
5. [ ] **Crear repositorio GitHub público**
6. [ ] **Subir todo el código a GitHub**

### Media Prioridad
7. [ ] Agregar nombres de integrantes al README
8. [ ] Completar sección de roles en el README
9. [ ] Tomar capturas de pantalla para el informe
10. [ ] Preparar consultas SQL de ejemplo para el informe

### Baja Prioridad (Mejoras)
11. [ ] Agregar tests unitarios (opcional)
12. [ ] Mejorar mensajes de error (más descriptivos)
13. [ ] Agregar más datos de prueba
14. [ ] Documentar métodos con Javadoc

## 🚀 Pasos Finales Antes de Entregar

1. **Compilar y probar**:
   ```bash
   mvn clean compile
   mvn exec:java
   ```

2. **Probar todos los casos de uso**:
   - Crear paciente
   - Crear historia clínica
   - Crear paciente con HC (transacción)
   - Actualizar datos
   - Buscar por DNI
   - Buscar por ID
   - Eliminar (baja lógica)
   - Intentar duplicar DNI (debe fallar)
   - Intentar duplicar nro. historia (debe fallar)

3. **Verificar base de datos**:
   ```sql
   USE pacienteHistoriaClinica;
   SELECT * FROM paciente;
   SELECT * FROM historiaClinica;
   ```

4. **Crear UML**: Usar herramienta online o software

5. **Escribir informe**: 6-8 páginas con todos los puntos

6. **Grabar video**: 10-15 minutos con demostración

7. **Subir a GitHub**: Repositorio público

8. **Verificar README**: Enlace al video incluido

9. **Entregar**: Según indicaciones del docente

---

**Estado actual**: ✅ Código completo | ⏳ Documentación pendiente | ⏳ Video pendiente

