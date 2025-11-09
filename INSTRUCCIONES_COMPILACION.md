# 🚀 Instrucciones de Compilación y Ejecución

## Pasos Rápidos

### 1️⃣ Preparar la Base de Datos

```bash
# Conectar a MySQL
mysql -u root -p

# Crear base de datos y tablas
source /home/agus/Escritorio/Yo/juan-tp/db.sql

# Insertar datos de prueba
source /home/agus/Escritorio/Yo/juan-tp/datos_prueba.sql

# Salir de MySQL
exit
```

### 2️⃣ Configurar la Conexión

Editar `src/main/resources/database.properties` con tu contraseña de MySQL:

```properties
db.url=jdbc:mysql://localhost:3306/pacienteHistoriaClinica?useSSL=false&serverTimezone=UTC
db.username=root
db.password=TU_PASSWORD_AQUI
db.driver=com.mysql.cj.jdbc.Driver
```

### 3️⃣ Compilar y Ejecutar

#### Opción A: Con Maven Wrapper (Recomendado - Windows)

```powershell
# Compilar el proyecto
./mvnw.cmd clean compile

# Ejecutar la aplicación con encoding UTF-8 (forma rápida)
./ejecutar.ps1

# O ejecutar directamente
./mvnw.cmd exec:java "-Dexec.mainClass=ar.edu.uner.tpi.main.Main"

# O crear un JAR ejecutable con todas las dependencias
./mvnw.cmd clean package
java -jar target/paciente-historia-clinica.jar
```

#### Opción A.a: Con Maven Wrapper (Linux/Mac)

```bash
# Compilar el proyecto
./mvnw clean compile

# Ejecutar la aplicación
./mvnw exec:java -Dexec.mainClass="ar.edu.uner.tpi.main.Main"

# O crear un JAR ejecutable con todas las dependencias
./mvnw clean package
java -jar target/paciente-historia-clinica.jar

#### Opción B: Con Maven 

```bash
cd /home/agus/Escritorio/Yo/juan-tp

# Compilar el proyecto
mvn clean compile

# Ejecutar la aplicación
mvn exec:java -Dexec.mainClass="ar.edu.uner.tpi.main.Main"

# O crear un JAR ejecutable con todas las dependencias
mvn clean package
java -jar target/paciente-historia-clinica.jar

#### Opción B: Sin Maven (Solo Java y javac)

```bash
cd /home/agus/Escritorio/Yo/juan-tp

# Crear directorio para clases compiladas
mkdir -p bin

# Descargar el driver MySQL (si no lo tienes)
wget https://repo1.maven.org/maven2/com/mysql/mysql-connector-j/8.3.0/mysql-connector-j-8.3.0.jar

# Compilar
javac -cp "mysql-connector-j-8.3.0.jar" \
  -d bin \
  -sourcepath src/main/java \
  src/main/java/ar/edu/uner/tpi/**/*.java

# Copiar el archivo de propiedades
cp src/main/resources/database.properties bin/

# Ejecutar
java -cp "bin:mysql-connector-j-8.3.0.jar" ar.edu.uner.tpi.main.Main
```

## 📦 Estructura Creada

```
juan-tp/
├── pom.xml                          # Configuración Maven
├── .gitignore                       # Archivos a ignorar en Git
├── README.md                        # Documentación principal
├── db.sql                           # Script creación BD y tablas
├── datos_prueba.sql                 # Datos de prueba
├── INSTRUCCIONES_COMPILACION.md     # Este archivo
│
└── src/
    └── main/
        ├── java/ar/edu/uner/tpi/
        │   ├── config/              # Configuración DB
        │   │   └── DatabaseConnection.java
        │   │
        │   ├── entities/            # Entidades del dominio
        │   │   ├── Paciente.java
        │   │   └── HistoriaClinica.java
        │   │
        │   ├── enums/               # Enumeraciones
        │   │   └── GrupoSanguineo.java
        │   │
        │   ├── dao/                 # Acceso a datos
        │   │   ├── GenericDao.java
        │   │   ├── PacienteDao.java
        │   │   └── HistoriaClinicaDao.java
        │   │
        │   ├── service/             # Lógica de negocio
        │   │   ├── GenericService.java
        │   │   ├── PacienteService.java
        │   │   └── HistoriaClinicaService.java
        │   │
        │   ├── exceptions/          # Excepciones personalizadas
        │   │   ├── DatabaseException.java
        │   │   └── ValidacionException.java
        │   │
        │   ├── util/                # Utilidades
        │   │   └── Validador.java
        │   │
        │   └── main/                # Punto de entrada
        │       ├── Main.java
        │       └── AppMenu.java
        │
        └── resources/
            └── database.properties  # Config de conexión

Total: 15 archivos Java + 3 SQL + 2 MD + 1 XML + 1 properties = 22 archivos
```

## ✅ Verificar Instalación

### Verificar Java 21

```bash
java -version
# Debe mostrar: java version "21.x.x"
```

### Verificar Maven (si usas Maven)

```bash
mvn -version
# Debe mostrar: Apache Maven 3.x.x
```

### Verificar MySQL

```bash
mysql --version
# Debe mostrar: mysql Ver 8.x.x
```

### Probar Conexión a la BD

```bash
mysql -u root -p -e "USE pacienteHistoriaClinica; SELECT COUNT(*) FROM paciente;"
# Debe mostrar el número de pacientes
```

## 🐛 Solución de Problemas

### Error: "Access denied for user"

**Problema**: Credenciales incorrectas en `database.properties`

**Solución**: Verificar usuario y contraseña de MySQL

### Error: "No suitable driver found"

**Problema**: Falta el driver MySQL en el classpath

**Solución**: 
- Con Maven: `mvn dependency:resolve`
- Manual: Descargar `mysql-connector-j-8.3.0.jar` y agregarlo al classpath

### Error: "Unknown database 'pacienteHistoriaClinica'"

**Problema**: No se ejecutó el script `db.sql`

**Solución**: 
```bash
mysql -u root -p < db.sql
```

### Error: "Communications link failure"

**Problema**: MySQL no está ejecutándose

**Solución**:
```bash
# Linux
sudo systemctl start mysql

# Windows
net start MySQL80
```

## 📝 Datos de Prueba Incluidos

El sistema viene con 8 pacientes de prueba:

1. GARCÍA, JUAN CARLOS (DNI: 12345678) - Con HC
2. RODRÍGUEZ, MARÍA FERNANDA (DNI: 23456789) - Con HC
3. LÓPEZ, PEDRO LUIS (DNI: 34567890) - Con HC
4. MARTÍNEZ, ANA SOFÍA (DNI: 45678901) - Con HC
5. GONZÁLEZ, ROBERTO DANIEL (DNI: 56789012) - Con HC
6. FERNÁNDEZ, LUCÍA BEATRIZ (DNI: 67890123) - Sin HC
7. PÉREZ, DIEGO MATÍAS (DNI: 78901234) - Sin HC
8. SÁNCHEZ, CAROLINA ISABEL (DNI: 89012345) - Sin HC

También hay 2 historias clínicas sin asociar para probar operaciones.

## 🎯 Flujo de Prueba Sugerido

1. **Listar pacientes**: Ver los 8 pacientes de prueba
2. **Buscar por DNI**: Buscar paciente "12345678"
3. **Crear paciente con HC**: Operación transaccional completa
4. **Actualizar paciente**: Modificar datos de un paciente
5. **Intentar duplicar DNI**: Probar validación
6. **Eliminar paciente**: Baja lógica (verificar en BD que eliminado=true)
7. **Asociar HC**: Asociar una HC existente a un paciente sin HC

## 📧 Soporte

Si encuentras problemas durante la compilación o ejecución, revisa:

1. ✅ Java 21 instalado correctamente
2. ✅ MySQL ejecutándose
3. ✅ Base de datos creada con `db.sql`
4. ✅ Credenciales correctas en `database.properties`
5. ✅ Driver MySQL en el classpath (Maven lo hace automáticamente)

---

**Última actualización**: $(date +%Y-%m-%d)

