#!/bin/bash

# Script de compilación y ejecución para el proyecto
# Uso: ./compilar.sh [opcion]
# Opciones: compile, run, package, clean

echo "════════════════════════════════════════════════════════════"
echo "  Sistema de Gestión de Pacientes e Historias Clínicas"
echo "  Script de Compilación y Ejecución"
echo "════════════════════════════════════════════════════════════"
echo ""

# Verificar que estamos en el directorio correcto
if [ ! -f "pom.xml" ]; then
    echo "❌ Error: No se encontró pom.xml"
    echo "   Ejecuta este script desde el directorio raíz del proyecto"
    exit 1
fi

# Función para compilar
compilar() {
    echo "📦 Compilando el proyecto..."
    mvn clean compile
    if [ $? -eq 0 ]; then
        echo "✅ Compilación exitosa"
        return 0
    else
        echo "❌ Error en la compilación"
        return 1
    fi
}

# Función para ejecutar
ejecutar() {
    echo "🚀 Ejecutando la aplicación..."
    echo ""
    mvn exec:java -Dexec.mainClass="ar.edu.uner.tpi.main.Main"
}

# Función para empaquetar
empaquetar() {
    echo "📦 Creando JAR ejecutable..."
    mvn clean package
    if [ $? -eq 0 ]; then
        echo "✅ JAR creado exitosamente"
        echo "   Ubicación: target/paciente-historia-clinica.jar"
        echo ""
        echo "   Para ejecutar: java -jar target/paciente-historia-clinica.jar"
        return 0
    else
        echo "❌ Error al crear JAR"
        return 1
    fi
}

# Función para limpiar
limpiar() {
    echo "🧹 Limpiando archivos compilados..."
    mvn clean
    rm -rf bin/
    echo "✅ Limpieza completada"
}

# Verificar Maven
if ! command -v mvn &> /dev/null; then
    echo "❌ Error: Maven no está instalado"
    echo "   Instala Maven o usa los comandos javac/java directamente"
    exit 1
fi

# Procesar argumentos
case "$1" in
    compile)
        compilar
        ;;
    run)
        compilar && ejecutar
        ;;
    package)
        empaquetar
        ;;
    clean)
        limpiar
        ;;
    jar)
        empaquetar && java -jar target/paciente-historia-clinica.jar
        ;;
    *)
        echo "Uso: ./compilar.sh [opcion]"
        echo ""
        echo "Opciones disponibles:"
        echo "  compile  - Solo compilar el proyecto"
        echo "  run      - Compilar y ejecutar"
        echo "  package  - Crear JAR ejecutable"
        echo "  jar      - Crear JAR y ejecutarlo"
        echo "  clean    - Limpiar archivos compilados"
        echo ""
        echo "Ejemplo: ./compilar.sh run"
        exit 1
        ;;
esac

