package main;

import config.DatabaseConnection;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

/**
 * Clase principal que inicia la aplicación
 */
public class Main {

    public static void main(String[] args) {
        // Configurar encoding UTF-8 para la consola
        try {
            System.setOut(new PrintStream(System.out, true, "UTF-8"));
            System.setErr(new PrintStream(System.err, true, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            System.err.println("Error al configurar UTF-8");
        }
        
        System.out.println("════════════════════════════════════════════════════════════════");
        System.out.println("     Sistema de Gestión de Pacientes e Historias Clínicas");
        System.out.println("════════════════════════════════════════════════════════════════");
        System.out.println();

        // Verificar conexión a la base de datos
        System.out.print("Verificando conexión a la base de datos... ");
        
        try {
            if (DatabaseConnection.probarConexion()) {
                System.out.println("Conexión exitosa\n");
                
                // Iniciar el menú de la aplicación
                AppMenu menu = new AppMenu();
                menu.mostrar();
                
            } else {
                System.err.println("Error al conectar");
                System.err.println("\nVerifique:");
                System.err.println("  1. MySQL está ejecutándose");
                System.err.println("  2. La base de datos 'pacienteHistoriaClinica' existe");
                System.err.println("  3. Las credenciales en database.properties son correctas");
                System.exit(1);
            }
            
        } catch (Exception e) {
            System.err.println("Error fatal: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}

