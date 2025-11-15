package config;

import exceptions.DatabaseException;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Clase para gestionar la conexión a la base de datos
 * Lee la configuración desde el archivo database.properties
 */
public class DatabaseConnection {
    
    private static Properties propiedades = new Properties();
    private static boolean cargado = false;

    // Bloque estático para cargar las propiedades al inicializar la clase
    static {
        cargarPropiedades();
    }

    /**
     * Carga las propiedades desde el archivo database.properties
     */
    private static void cargarPropiedades() {
        try (InputStream input = DatabaseConnection.class
                .getClassLoader()
                .getResourceAsStream("database.properties")) {
            
            if (input == null) {
                throw new DatabaseException("No se pudo encontrar el archivo database.properties");
            }
            
            propiedades.load(input);
            cargado = true;
            
            // Cargar el driver JDBC
            Class.forName(propiedades.getProperty("db.driver"));
            
        } catch (IOException e) {
            throw new DatabaseException("Error al cargar database.properties", e);
        } catch (ClassNotFoundException e) {
            throw new DatabaseException("No se encontró el driver de MySQL", e);
        }
    }

    /**
     * Obtiene una nueva conexión a la base de datos
     * 
     * @return Connection objeto de conexión a la base de datos
     * @throws DatabaseException si hay error al conectar
     */
    public static Connection getConnection() {
        if (!cargado) {
            cargarPropiedades();
        }

        try {
            String url = propiedades.getProperty("db.url");
            String usuario = propiedades.getProperty("db.username");
            String password = propiedades.getProperty("db.password");
            
            return DriverManager.getConnection(url, usuario, password);
            
        } catch (SQLException e) {
            throw new DatabaseException("Error al conectar a la base de datos: " + e.getMessage(), e);
        }
    }

    /**
     * Cierra una conexión de forma segura
     * 
     * @param conexion la conexión a cerrar
     */
    public static void cerrarConexion(Connection conexion) {
        if (conexion != null) {
            try {
                conexion.close();
            } catch (SQLException e) {
                System.err.println("Error al cerrar la conexión: " + e.getMessage());
            }
        }
    }

    /**
     * Prueba la conexión a la base de datos
     * 
     * @return true si la conexión es exitosa
     */
    public static boolean probarConexion() {
        try (Connection conn = getConnection()) {
            return conn != null && !conn.isClosed();
        } catch (Exception e) {
            System.err.println("Error al probar la conexión: " + e.getMessage());
            return false;
        }
    }
}

