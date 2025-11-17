# Sistema de Gestión de Pacientes e Historias Clínicas

Trabajo Final Integrador - Programación 2  
Tecnicatura Universitaria en Programación

## Descripción del Dominio

Este proyecto implementa un sistema de gestión para Pacientes e Historias Clínicas con una relación unidireccional 1→1 (el Paciente referencia a la Historia Clínica). El sistema permite realizar operaciones CRUD completas sobre ambas entidades, con validaciones, transacciones y baja lógica.

### Características Principales

- Relación 1→1 unidireccional: `Paciente → HistoriaClinica`
- Persistencia con JDBC (sin ORM) en MySQL
- Patrón DAO y Service
- Transacciones con commit/rollback
- Baja lógica (no se eliminan físicamente los registros)
- Validaciones completas en todas las capas
- Menú de consola interactivo
- Manejo robusto de excepciones

## Tecnologías Utilizadas

- Java 21
- MySQL 8.0+
- JDBC (MySQL Connector/J)
- Maven (estructura de proyecto)

## Estructura del Proyecto

src/main/java/
├── config/              # Configuración de base de datos
│   └── DatabaseConnection.java
├── entities/            # Entidades del dominio
│   ├── Paciente.java
│   └── HistoriaClinica.java
├── enums/               # Enumeraciones
│   └── GrupoSanguineo.java
├── dao/                 # Capa de acceso a datos
│   ├── GenericDao.java
│   ├── PacienteDao.java
│   └── HistoriaClinicaDao.java
├── service/             # Lógica de negocio y transacciones
│   ├── GenericService.java
│   ├── PacienteService.java
│   └── HistoriaClinicaService.java
├── exceptions/          # Excepciones personalizadas
│   ├── DatabaseException.java
│   └── ValidacionException.java
├── util/                # Utilidades
│   └── Validador.java
└── main/                # Punto de entrada
    ├── Main.java
    └── AppMenu.java

src/main/resources/
└── database.properties  # Configuración de conexión a BD

## Requisitos Previos

### Software Necesario

1. Java Development Kit (JDK) 21
   - Verificar: `java -version`
   - Descargar: https://www.oracle.com/java/technologies/downloads/

2. MySQL 8.0 o superior
   - Verificar: `mysql --version`
   - Descargar: https://dev.mysql.com/downloads/mysql/

3. MySQL Connector/J (Driver JDBC)
   - Descargar: https://dev.mysql.com/downloads/connector/j/
   - O usar Maven para gestionar dependencias

## Instalación y Configuración

### 1. Clonar el Repositorio

bash
git clone https://github.com/814942/UTN-TUP-P2-TP.git

### 2. Crear la Base de Datos

Ejecutar el script `db.sql` para crear la base de datos y las tablas:

bash
mysql -u root -p < db.sql

O desde el cliente MySQL:

sql
SOURCE /ruta/completa/a/db.sql;

### 3. Insertar Datos de Prueba

Ejecutar el script `datos_prueba.sql`:

bash
mysql -u root -p pacienteHistoriaClinica < datos_prueba.sql

### 4. Configurar la Conexión a la Base de Datos

Editar el archivo `src/main/resources/database.properties`:

properties
db.url=jdbc:mysql://localhost:3306/pacienteHistoriaClinica?useSSL=false&serverTimezone=UTC
db.username=root
db.password=tu_password_aqui
db.driver=com.mysql.cj.jdbc.Driver

### 5. Agregar el Driver MySQL al Proyecto

Opción A: Maven (Recomendado)

Crear un archivo `pom.xml` en la raíz del proyecto:

xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0
         http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>ar.edu.UTN.tpi</groupId>
    <artifactId>paciente-historia-clinica</artifactId>
    <version>1.0-SNAPSHOT</version>

    <properties>
        <maven.compiler.source>21</maven.compiler.source>
        <maven.compiler.target>21</maven.compiler.target>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    </properties>

    <dependencies>
        <!-- MySQL Connector -->
        <dependency>
            <groupId>com.mysql</groupId>
            <artifactId>mysql-connector-j</artifactId>
            <version>8.3.0</version>
        </dependency>
    </dependencies>
</project>

**Opción B: Manual**

Descargar el JAR de MySQL Connector/J y agregarlo al classpath:

bash
javac -cp ".:mysql-connector-j-8.3.0.jar" src/main/java/main/Main.java
java -cp ".:mysql-connector-j-8.3.0.jar:src/main/java" Main

## Ejecución

### Con Maven

bash
# Compilar
mvn clean compile

# Ejecutar
mvn exec:java -Dexec.mainClass="Main"

### Con Java directamente

bash
# Compilar
cd src/main/java
javac -d ../../../bin **/*.java

# Ejecutar
cd ../../../
java -cp bin:src/main/resources Main

## Flujo de Uso

### Menú Principal

Al iniciar la aplicación, verás el menú principal con las siguientes opciones:

1. Gestión de Pacientes: CRUD completo de pacientes
2. Gestión de Historias Clínicas: CRUD completo de historias clínicas
3. Operaciones Combinadas: Operaciones transaccionales
4. Salir

### Operaciones Disponibles

#### Pacientes
- Crear paciente
- Listar todos los pacientes
- Buscar paciente por ID
- Buscar paciente por DNI
- Actualizar datos del paciente
- Eliminar paciente (baja lógica)

#### Historias Clínicas
- Crear historia clínica
- Listar todas las historias clínicas
- Buscar historia clínica por ID
- Buscar historia clínica por número
- Actualizar historia clínica
- Eliminar historia clínica (baja lógica)

#### Operaciones Combinadas (Transaccionales)
- Crear paciente con historia clínica: Crea ambas entidades en una sola transacción (commit/rollback)

### Ejemplo de Uso: Crear Paciente con Historia Clínica

Esta operación demuestra el uso de transacciones:

1. Seleccionar opción `3` (Operaciones Combinadas)
2. Seleccionar opción `1` (Crear Paciente con Historia Clínica)
3. Ingresar datos del paciente:
   - Apellido: PÉREZ
   - Nombre: CARLOS
   - DNI: 98765432
   - Fecha de Nacimiento: 15/08/1990
4. Ingresar datos de la historia clínica:
   - Número de Historia: HC-2024-010
   - Grupo Sanguíneo: O+
   - Antecedentes: Sin antecedentes
   - Medicación: (opcional)
   - Observaciones: Paciente sano

Si todo es correcto, ambas entidades se crean en una transacción. Si ocurre un error (ej: DNI duplicado), se hace rollback y no se crea ninguna.

## Casos de Prueba

### 1. Validación de DNI Único
- Intentar crear dos pacientes con el mismo DNI
- Resultado esperado: Error de validación

### 2. Validación de Número de Historia Único
- Intentar crear dos historias clínicas con el mismo número
- Resultado esperado: Error de validación

### 3. Relación 1→1
- Intentar asociar una segunda historia clínica a un paciente que ya tiene una
- Resultado esperado: Error de validación

### 4. Transacción con Rollback
- Crear un paciente con historia clínica usando un número de historia duplicado
- Resultado esperado: Rollback completo, el paciente tampoco se crea

### 5. Baja Lógica
- Eliminar un paciente
- Verificar en la BD que el campo `eliminado = true`
- El paciente no debe aparecer en los listados

## Estructura de la Base de Datos

### Tabla: `paciente`

| Campo       | Tipo         | Restricciones           |
|-------------|--------------|-------------------------|
| id          | INT          | PK, AUTO_INCREMENT      |
| eliminado   | BOOLEAN      | NOT NULL, DEFAULT false |
| apellido    | VARCHAR(40)  | NOT NULL                |
| nombre      | VARCHAR(40)  | NOT NULL                |
| dni         | VARCHAR(15)  | NOT NULL, UNIQUE        |
| fecha_nac   | DATE         | NOT NULL                |

### Tabla: `historiaClinica`

| Campo            | Tipo         | Restricciones                |
|------------------|--------------|------------------------------|
| id               | INT          | PK, AUTO_INCREMENT           |
| eliminado        | BOOLEAN      | NOT NULL, DEFAULT false      |
| nro_historia     | VARCHAR(20)  | NOT NULL, UNIQUE             |
| grupo_sangre     | VARCHAR(10)  | NOT NULL                     |
| antecedentes     | TEXT         | NOT NULL                     |
| medicacionActual | VARCHAR(255) | NULL                         |
| observaciones    | TEXT         | NOT NULL                     |
| id_paciente      | INT          | FK → paciente(id), NOT NULL  |
|                  |              |  UNIQUE                      |

### Relación 1→1

La relación se garantiza mediante:
- Foreign Key: `id_paciente` → `paciente(id)`
- Constraint UNIQUE: Solo una historia clínica por paciente
- ON DELETE CASCADE: Si se elimina un paciente, su HC también

## Video de Demostración

**Enlace al video**: [Aquí irá el enlace al video en YouTube]

El video incluye:
- Presentación del equipo (4 integrantes)
- Demostración completa del flujo CRUD
- Explicación de la relación 1→1
- Revisión del código por capas
- Demostración de una transacción con rollback

## Integrantes del Equipo

1. Pablo Garay
2. Jose Dario Gimenez
3. Israel Garcia Moscoso
4. Juan Esteban Gelos

## Decisiones de Diseño

### 1. Relación 1→1 con Foreign Key Única

Se eligió implementar la relación usando una Foreign Key con restricción UNIQUE en la tabla `historiaClinica` apuntando a `paciente`. Esto garantiza que:
- Una historia clínica solo puede asociarse a un paciente
- Un paciente puede tener máximo una historia clínica
- Es más flexible que usar Primary Key compartida

### 2. Baja Lógica

Todas las entidades tienen el campo `eliminado` (BOOLEAN) para implementar soft delete. Esto permite:
- Mantener el historial de datos
- Recuperar información si es necesario
- Cumplir con regulaciones de auditoría en sistemas médicos

### 3. Validaciones en Múltiples Capas

- DAO: Validaciones de integridad referencial
- Service: Validaciones de negocio y unicidad
- Util: Validaciones de formato (DNI, fechas, etc.)

### 4. Transacciones Explícitas

El `PacienteService` maneja transacciones explícitamente:
java
conexion.setAutoCommit(false);
try {
    // operaciones
    conexion.commit();
} catch (Exception e) {
    conexion.rollback();
}

## Referencias y Fuentes

- Oracle Java Documentation: https://docs.oracle.com/en/java/javase/21/
- MySQL Reference Manual: https://dev.mysql.com/doc/refman/8.0/en/
- JDBC Tutorial: https://docs.oracle.com/javase/tutorial/jdbc/
- Material del curso: Programación 2 - UTN
- Asistencia de IA: ChatGPT para consultas de sintaxis y buenas prácticas

## Licencia

Este proyecto es un trabajo académico para la Tecnicatura Universitaria en Programación a distancia de la UTN.

Fecha de entrega: 17/11/2025  
Versión: 1.0  
Estado: Completo