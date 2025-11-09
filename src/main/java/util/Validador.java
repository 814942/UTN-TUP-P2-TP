package util;

import exceptions.ValidacionException;

import java.time.LocalDate;
import java.util.regex.Pattern;

/**
 * Clase utilitaria para validar datos de entrada
 */
public class Validador {

    private static final Pattern PATRON_EMAIL = Pattern.compile(
        "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );

    private static final Pattern PATRON_DNI = Pattern.compile("^[0-9]{7,8}$");

    /**
     * Valida que una cadena no sea nula ni vacía
     */
    public static void validarNoVacio(String valor, String nombreCampo) {
        if (valor == null || valor.trim().isEmpty()) {
            throw new ValidacionException("El campo " + nombreCampo + " es obligatorio");
        }
    }

    /**
     * Valida la longitud máxima de una cadena
     */
    public static void validarLongitudMaxima(String valor, int longitudMaxima, String nombreCampo) {
        if (valor != null && valor.length() > longitudMaxima) {
            throw new ValidacionException(
                "El campo " + nombreCampo + " excede la longitud máxima de " + longitudMaxima + " caracteres"
            );
        }
    }

    /**
     * Valida el formato de un email
     */
    public static void validarEmail(String email) {
        if (email != null && !email.trim().isEmpty()) {
            if (!PATRON_EMAIL.matcher(email).matches()) {
                throw new ValidacionException("El formato del email es inválido");
            }
        }
    }

    /**
     * Valida el formato de un DNI argentino
     */
    public static void validarDni(String dni) {
        validarNoVacio(dni, "DNI");
        if (!PATRON_DNI.matcher(dni).matches()) {
            throw new ValidacionException("El DNI debe contener entre 7 y 8 dígitos numéricos");
        }
    }

    /**
     * Valida que una fecha de nacimiento sea razonable
     */
    public static void validarFechaNacimiento(LocalDate fechaNacimiento) {
        if (fechaNacimiento == null) {
            throw new ValidacionException("La fecha de nacimiento es obligatoria");
        }
        
        LocalDate fechaMinima = LocalDate.of(1900, 1, 1);
        LocalDate fechaMaxima = LocalDate.now();
        
        if (fechaNacimiento.isBefore(fechaMinima)) {
            throw new ValidacionException("La fecha de nacimiento no puede ser anterior a 1900");
        }
        
        if (fechaNacimiento.isAfter(fechaMaxima)) {
            throw new ValidacionException("La fecha de nacimiento no puede ser futura");
        }
    }

    /**
     * Valida que un objeto no sea nulo
     */
    public static void validarNoNulo(Object objeto, String nombreCampo) {
        if (objeto == null) {
            throw new ValidacionException("El campo " + nombreCampo + " es obligatorio");
        }
    }

    /**
     * Valida que un número sea positivo
     */
    public static void validarPositivo(Number numero, String nombreCampo) {
        if (numero == null || numero.doubleValue() <= 0) {
            throw new ValidacionException("El campo " + nombreCampo + " debe ser un número positivo");
        }
    }
}

