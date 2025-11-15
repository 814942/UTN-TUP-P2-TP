package exceptions;

/**
 * Excepción personalizada para errores de validación de datos
 */
public class ValidacionException extends RuntimeException {
    
    public ValidacionException(String mensaje) {
        super(mensaje);
    }

    public ValidacionException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}

