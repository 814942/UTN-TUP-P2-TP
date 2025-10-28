package ar.edu.uner.tpi.exceptions;

/**
 * Excepción personalizada para errores de base de datos
 */
public class DatabaseException extends RuntimeException {
    
    public DatabaseException(String mensaje) {
        super(mensaje);
    }

    public DatabaseException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}

