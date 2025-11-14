package main;

import entities.HistoriaClinica;
import entities.Paciente;
import enums.GrupoSanguineo;
import exceptions.DatabaseException;
import exceptions.ValidacionException;
import service.HistoriaClinicaService;
import service.PacienteService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

/**
 * Menú de consola para interactuar con el sistema de Pacientes e Historias Clínicas
 */
public class AppMenu {

    private final Scanner scanner;
    private final PacienteService pacienteService;
    private final HistoriaClinicaService historiaClinicaService;
    private final DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public AppMenu() {
        this.scanner = new Scanner(System.in);
        this.pacienteService = new PacienteService();
        this.historiaClinicaService = new HistoriaClinicaService();
    }

    /**
     * Muestra el menú principal y procesa las opciones
     */
    public void mostrar() {
        boolean salir = false;

        while (!salir) {
            mostrarMenuPrincipal();
            String opcion = scanner.nextLine().trim().toUpperCase();

            try {
                salir = procesarOpcionPrincipal(opcion);
            } catch (ValidacionException e) {
                System.err.println("\nError de validación: " + e.getMessage());
            } catch (DatabaseException e) {
                System.err.println("\nError de base de datos: " + e.getMessage());
            } catch (Exception e) {
                System.err.println("\nError inesperado: " + e.getMessage());
                e.printStackTrace();
            }

            if (!salir) {
                pausar();
            }
        }

        scanner.close();
    }

    private void mostrarMenuPrincipal() {
        System.out.println("\n╔══════════════════════════════════════════════════════════════════╗");
        System.out.println("║  SISTEMA DE GESTIÓN DE PACIENTES E HISTORIAS CLÍNICAS  ║");
        System.out.println("╚══════════════════════════════════════════════════════════════════╝");
        System.out.println("\n┌─ MENÚ PRINCIPAL ─────────────────────────────────────────────┐");
        System.out.println("│                                                       │");
        System.out.println("│  [1] Gestión de Pacientes                             │");
        System.out.println("│  [2] Gestión de Historias Clínicas                    │");
        System.out.println("│  [3] Operaciones Combinadas                           │");
        System.out.println("│  [0] Salir                                            │");
        System.out.println("│                                                       │");
        System.out.println("└─────────────────────────────────────────────────────────────────┘");
        System.out.print("\n➤ Seleccione una opción: ");
    }

    private boolean procesarOpcionPrincipal(String opcion) {
        switch (opcion) {
            case "1" -> menuPacientes();
            case "2" -> menuHistoriasClinicas();
            case "3" -> menuOperacionesCombinadas();
            case "0" -> {
                System.out.println("\n¡Hasta luego!");
                return true;
            }
            default -> System.out.println("\nOpción inválida. Por favor, intente nuevamente.");
        }
        return false;
    }

    // ==================== MENÚ PACIENTES ====================

    private void menuPacientes() {
        boolean volver = false;

        while (!volver) {
            System.out.println("\n┌─ GESTIÓN DE PACIENTES ───────────────────────────────────────┐");
            System.out.println("│                                                        │");
            System.out.println("│  [1] Crear Paciente                                    │");
            System.out.println("│  [2] Listar Todos los Pacientes                        │");
            System.out.println("│  [3] Buscar Paciente por ID                            │");
            System.out.println("│  [4] Buscar Paciente por DNI                           │");
            System.out.println("│  [5] Actualizar Paciente                               │");
            System.out.println("│  [6] Eliminar Paciente                                 │");
            System.out.println("│  [0] Volver al Menú Principal                          │");
            System.out.println("│                                                        │");
            System.out.println("└──────────────────────────────────────────────────────────────────┘");
            System.out.print("\n➤ Seleccione una opción: ");

            String opcion = scanner.nextLine().trim().toUpperCase();

            try {
                switch (opcion) {
                    case "1" -> crearPaciente();
                    case "2" -> listarPacientes();
                    case "3" -> buscarPacientePorId();
                    case "4" -> buscarPacientePorDni();
                    case "5" -> actualizarPaciente();
                    case "6" -> eliminarPaciente();
                    case "0" -> volver = true;
                    default -> System.out.println("\nOpción inválida.");
                }
            } catch (Exception e) {
                System.err.println("\nError: " + e.getMessage());
            }

            if (!volver) {
                pausar();
            }
        }
    }

    private void crearPaciente() {
        System.out.println("\n═══ CREAR NUEVO PACIENTE ═══\n");

        System.out.print("Apellido: ");
        String apellido = scanner.nextLine().trim().toUpperCase();

        System.out.print("Nombre: ");
        String nombre = scanner.nextLine().trim().toUpperCase();

        System.out.print("DNI (sin puntos): ");
        String dni = scanner.nextLine().trim();

        System.out.print("Fecha de Nacimiento (dd/MM/yyyy): ");
        String fechaStr = scanner.nextLine().trim();
        LocalDate fechaNacimiento = parsearFecha(fechaStr);

        Paciente paciente = new Paciente();
        paciente.setApellido(apellido);
        paciente.setNombre(nombre);
        paciente.setDni(dni);
        paciente.setFechaNacimiento(fechaNacimiento);
        paciente.setEliminado(false);

        Paciente creado = pacienteService.insertar(paciente);
        System.out.println("\nPaciente creado exitosamente con ID: " + creado.getId());
    }

    private void listarPacientes() {
        System.out.println("\n═══ LISTADO DE PACIENTES ═══\n");

        List<Paciente> pacientes = pacienteService.obtenerTodos();

        if (pacientes.isEmpty()) {
            System.out.println("No hay pacientes registrados.");
            return;
        }

        System.out.println("┌────────────────────────────────────────────────────────────────────────────────────────┐");
        System.out.printf("│ %-5s │ %-20s │ %-20s │ %-10s │ %-15s │ %-10s │%n",
                "ID", "APELLIDO", "NOMBRE", "DNI", "FECHA NAC.", "HISTORIA CLÍ");
        System.out.println("├────────────────────────────────────────────────────────────────────────────────────────┤");

        for (Paciente p : pacientes) {
            String tieneHC = (p.getHistoriaClinica() != null) ? "SÍ" : "NO";
            System.out.printf("│ %-5d │ %-20s │ %-20s │ %-10s │ %-15s │ %-10s │%n",
                    p.getId(),
                    truncar(p.getApellido(), 20),
                    truncar(p.getNombre(), 20),
                    p.getDni(),
                    p.getFechaNacimiento().format(formatoFecha),
                    tieneHC);
        }

        System.out.println("└────────────────────────────────────────────────────────────────────────────────────────┘");
        System.out.println("\nTotal de pacientes: " + pacientes.size());
    }

    private void buscarPacientePorId() {
        System.out.println("\n═══ BUSCAR PACIENTE POR ID ═══\n");

        System.out.print("Ingrese el ID del paciente: ");
        Long id = leerLong();

        Optional<Paciente> paciente = pacienteService.obtenerPorId(id);

        if (paciente.isPresent()) {
            mostrarDetallePaciente(paciente.get());
        } else {
            System.out.println("\nNo se encontró un paciente con ID: " + id);
        }
    }

    private void buscarPacientePorDni() {
        System.out.println("\n═══ BUSCAR PACIENTE POR DNI ═══\n");

        System.out.print("Ingrese el DNI (sin puntos): ");
        String dni = scanner.nextLine().trim();

        Optional<Paciente> paciente = pacienteService.buscarPorDni(dni);

        if (paciente.isPresent()) {
            mostrarDetallePaciente(paciente.get());
        } else {
            System.out.println("\nNo se encontró un paciente con DNI: " + dni);
        }
    }

    private void actualizarPaciente() {
        System.out.println("\n═══ ACTUALIZAR PACIENTE ═══\n");

        System.out.print("Ingrese el ID del paciente a actualizar: ");
        Long id = leerLong();

        Optional<Paciente> pacienteOpt = pacienteService.obtenerPorId(id);

        if (pacienteOpt.isEmpty()) {
            System.out.println("\nNo se encontró un paciente con ID: " + id);
            return;
        }

        Paciente paciente = pacienteOpt.get();
        System.out.println("\nDatos actuales:");
        mostrarDetallePaciente(paciente);

        System.out.println("\nIngrese los nuevos datos (presione Enter para mantener el valor actual):\n");

        System.out.print("Apellido [" + paciente.getApellido() + "]: ");
        String apellido = scanner.nextLine().trim().toUpperCase();
        if (!apellido.isEmpty()) {
            paciente.setApellido(apellido);
        }

        System.out.print("Nombre [" + paciente.getNombre() + "]: ");
        String nombre = scanner.nextLine().trim().toUpperCase();
        if (!nombre.isEmpty()) {
            paciente.setNombre(nombre);
        }

        System.out.print("DNI [" + paciente.getDni() + "]: ");
        String dni = scanner.nextLine().trim();
        if (!dni.isEmpty()) {
            paciente.setDni(dni);
        }

        System.out.print("Fecha de Nacimiento [" + paciente.getFechaNacimiento().format(formatoFecha) + "] (dd/MM/yyyy): ");
        String fechaStr = scanner.nextLine().trim();
        if (!fechaStr.isEmpty()) {
            paciente.setFechaNacimiento(parsearFecha(fechaStr));
        }

        pacienteService.actualizar(paciente);
        System.out.println("\nPaciente actualizado exitosamente.");
    }

    private void eliminarPaciente() {
        System.out.println("\n═══ ELIMINAR PACIENTE ═══\n");

        System.out.print("Ingrese el ID del paciente a eliminar: ");
        Long id = leerLong();

        Optional<Paciente> paciente = pacienteService.obtenerPorId(id);

        if (paciente.isEmpty()) {
            System.out.println("\nNo se encontró un paciente con ID: " + id);
            return;
        }

        mostrarDetallePaciente(paciente.get());

        System.out.print("\n¿Está seguro que desea eliminar este paciente? (S/N): ");
        String confirmacion = scanner.nextLine().trim().toUpperCase();

        if (confirmacion.equals("S")) {
            pacienteService.eliminar(id);
            System.out.println("\nPaciente eliminado exitosamente (baja lógica).");
        } else {
            System.out.println("\nOperación cancelada.");
        }
    }

    // ==================== MENÚ HISTORIAS CLÍNICAS ====================

    private void menuHistoriasClinicas() {
        boolean volver = false;

        while (!volver) {
            System.out.println("\n┌─ GESTIÓN DE HISTORIAS CLÍNICAS ──────────────────────────────┐");
            System.out.println("│                                                         │");
            System.out.println("│  [1] Crear Historia Clínica                             │");
            System.out.println("│  [2] Listar Todas las Historias Clínicas                │");
            System.out.println("│  [3] Buscar Historia Clínica por ID                     │");
            System.out.println("│  [4] Buscar Historia Clínica por Número                 │");
            System.out.println("│  [5] Actualizar Historia Clínica                        │");
            System.out.println("│  [6] Eliminar Historia Clínica                          │");
            System.out.println("│  [0] Volver al Menú Principal                           │");
            System.out.println("│                                                         │");
            System.out.println("└───────────────────────────────────────────────────────────────────┘");
            System.out.print("\n➤ Seleccione una opción: ");

            String opcion = scanner.nextLine().trim().toUpperCase();

            try {
                switch (opcion) {
                    case "1" -> crearHistoriaClinica();
                    case "2" -> listarHistoriasClinicas();
                    case "3" -> buscarHistoriaClinicaPorId();
                    case "4" -> buscarHistoriaClinicaPorNumero();
                    case "5" -> actualizarHistoriaClinica();
                    case "6" -> eliminarHistoriaClinica();
                    case "0" -> volver = true;
                    default -> System.out.println("\nOpción inválida.");
                }
            } catch (Exception e) {
                System.err.println("\nError: " + e.getMessage());
            }

            if (!volver) {
                pausar();
            }
        }
    }

    private void crearHistoriaClinica() {
        System.out.println("\n═══ CREAR NUEVA HISTORIA CLÍNICA ═══\n");

        // Mostrar pacientes disponibles
        System.out.println("--- PACIENTES DISPONIBLES ---\n");
        List<Paciente> pacientes = pacienteService.obtenerTodos();

        if (pacientes.isEmpty()) {
            System.out.println("No hay pacientes registrados. Debe crear un paciente primero.");
            return;
        }

        System.out.println("┌────────────────────────────────────────────────────────────────────┐");
        System.out.printf("│ %-5s │ %-20s │ %-20s │ %-10s │%n",
                "ID", "APELLIDO", "NOMBRE", "TIENE HC");
        System.out.println("├────────────────────────────────────────────────────────────────────┤");

        for (Paciente p : pacientes) {
            String tieneHC = (p.getHistoriaClinica() != null) ? "SÍ" : "NO";
            System.out.printf("│ %-5d │ %-20s │ %-20s │ %-10s │%n",
                    p.getId(),
                    truncar(p.getApellido(), 20),
                    truncar(p.getNombre(), 20),
                    tieneHC);
        }
        System.out.println("└────────────────────────────────────────────────────────────────────┘\n");

        // Solicitar ID del paciente
        System.out.print("ID del Paciente: ");
        Long idPaciente = leerLong();

        // Validar que el paciente existe
        Optional<Paciente> pacienteOpt = pacienteService.obtenerPorId(idPaciente);
        if (pacienteOpt.isEmpty()) {
            System.out.println("\nNo existe un paciente con ID: " + idPaciente);
            return;
        }

        Paciente paciente = pacienteOpt.get();

        // Validar que el paciente no tenga ya una historia clínica
        if (paciente.getHistoriaClinica() != null) {
            System.out.println("\nEl paciente " + paciente.getNombre() + " " + paciente.getApellido() +
                             " ya tiene una historia clínica asociada (ID: " + paciente.getHistoriaClinica().getId() + ")");
            return;
        }

        // Solicitar datos de la historia clínica
        System.out.println("\n--- DATOS DE LA HISTORIA CLÍNICA ---\n");

        System.out.print("Número de Historia: ");
        String nroHistoria = scanner.nextLine().trim().toUpperCase();

        System.out.println("\nGrupos Sanguíneos disponibles:");
        for (GrupoSanguineo gs : GrupoSanguineo.values()) {
            System.out.println("  - " + gs.getValor());
        }
        System.out.print("Grupo Sanguíneo: ");
        String grupoStr = scanner.nextLine().trim().toUpperCase();
        GrupoSanguineo grupoSanguineo = GrupoSanguineo.fromString(grupoStr);

        System.out.print("Antecedentes: ");
        String antecedentes = scanner.nextLine().trim();

        System.out.print("Medicación Actual (opcional): ");
        String medicacion = scanner.nextLine().trim();

        System.out.print("Observaciones: ");
        String observaciones = scanner.nextLine().trim();

        HistoriaClinica hc = new HistoriaClinica();
        hc.setNroHistoria(nroHistoria);
        hc.setGrupoSanguineo(grupoSanguineo);
        hc.setAntecedentes(antecedentes);
        hc.setMedicacionActual(medicacion.isEmpty() ? null : medicacion);
        hc.setObservaciones(observaciones);
        hc.setIdPaciente(idPaciente);
        hc.setEliminado(false);

        HistoriaClinica creada = historiaClinicaService.insertar(hc);
        System.out.println("\nHistoria clínica creada exitosamente!");
        System.out.println("   - ID de la Historia Clínica: " + creada.getId());
        System.out.println("   - Asociada al paciente: " + paciente.getNombre() + " " + paciente.getApellido() + " (ID: " + idPaciente + ")");
    }

    private void listarHistoriasClinicas() {
        System.out.println("\n═══ LISTADO DE HISTORIAS CLÍNICAS ═══\n");

        List<HistoriaClinica> historias = historiaClinicaService.obtenerTodos();

        if (historias.isEmpty()) {
            System.out.println("No hay historias clínicas registradas.");
            return;
        }

        System.out.println("┌──────────────────────────────────────────────────────────────────────────┐");
        System.out.printf("│ %-5s │ %-18s │ %-15s │ %-20s │%n",
                "ID", "NRO. HISTORIA", "GRUPO SANG.", "ANTECEDENTES");
        System.out.println("├──────────────────────────────────────────────────────────────────────────┤");

        for (HistoriaClinica hc : historias) {
            System.out.printf("│ %-5d │ %-18s │ %-15s │ %-20s │%n",
                    hc.getId(),
                    hc.getNroHistoria(),
                    hc.getGrupoSanguineo().getValor(),
                    truncar(hc.getAntecedentes(), 20));
        }

        System.out.println("└──────────────────────────────────────────────────────────────────────────┘");
        System.out.println("\nTotal de historias clínicas: " + historias.size());
    }

    private void buscarHistoriaClinicaPorId() {
        System.out.println("\n═══ BUSCAR HISTORIA CLÍNICA POR ID ═══\n");

        System.out.print("Ingrese el ID de la historia clínica: ");
        Long id = leerLong();

        Optional<HistoriaClinica> hc = historiaClinicaService.obtenerPorId(id);

        if (hc.isPresent()) {
            mostrarDetalleHistoriaClinica(hc.get());
        } else {
            System.out.println("\nNo se encontró una historia clínica con ID: " + id);
        }
    }

    private void buscarHistoriaClinicaPorNumero() {
        System.out.println("\n═══ BUSCAR HISTORIA CLÍNICA POR NÚMERO ═══\n");

        System.out.print("Ingrese el número de historia: ");
        String numero = scanner.nextLine().trim().toUpperCase();

        Optional<HistoriaClinica> hc = historiaClinicaService.buscarPorNumero(numero);

        if (hc.isPresent()) {
            mostrarDetalleHistoriaClinica(hc.get());
        } else {
            System.out.println("\nNo se encontró una historia clínica con número: " + numero);
        }
    }

    private void actualizarHistoriaClinica() {
        System.out.println("\n═══ ACTUALIZAR HISTORIA CLÍNICA ═══\n");

        System.out.print("Ingrese el ID de la historia clínica a actualizar: ");
        Long id = leerLong();

        Optional<HistoriaClinica> hcOpt = historiaClinicaService.obtenerPorId(id);

        if (hcOpt.isEmpty()) {
            System.out.println("\nNo se encontró una historia clínica con ID: " + id);
            return;
        }

        HistoriaClinica hc = hcOpt.get();
        System.out.println("\nDatos actuales:");
        mostrarDetalleHistoriaClinica(hc);

        System.out.println("\nIngrese los nuevos datos (presione Enter para mantener el valor actual):\n");

        System.out.print("Número de Historia [" + hc.getNroHistoria() + "]: ");
        String nroHistoria = scanner.nextLine().trim().toUpperCase();
        if (!nroHistoria.isEmpty()) {
            hc.setNroHistoria(nroHistoria);
        }

        System.out.print("Grupo Sanguíneo [" + hc.getGrupoSanguineo().getValor() + "]: ");
        String grupoStr = scanner.nextLine().trim().toUpperCase();
        if (!grupoStr.isEmpty()) {
            hc.setGrupoSanguineo(GrupoSanguineo.fromString(grupoStr));
        }

        System.out.print("Antecedentes [" + truncar(hc.getAntecedentes(), 30) + "...]: ");
        String antecedentes = scanner.nextLine().trim();
        if (!antecedentes.isEmpty()) {
            hc.setAntecedentes(antecedentes);
        }

        System.out.print("Medicación Actual [" + (hc.getMedicacionActual() != null ? hc.getMedicacionActual() : "N/A") + "]: ");
        String medicacion = scanner.nextLine().trim();
        if (!medicacion.isEmpty()) {
            hc.setMedicacionActual(medicacion);
        }

        System.out.print("Observaciones [" + truncar(hc.getObservaciones(), 30) + "...]: ");
        String observaciones = scanner.nextLine().trim();
        if (!observaciones.isEmpty()) {
            hc.setObservaciones(observaciones);
        }

        historiaClinicaService.actualizar(hc);
        System.out.println("\nHistoria clínica actualizada exitosamente.");
    }

    private void eliminarHistoriaClinica() {
        System.out.println("\n═══ ELIMINAR HISTORIA CLÍNICA ═══\n");

        System.out.print("Ingrese el ID de la historia clínica a eliminar: ");
        Long id = leerLong();

        Optional<HistoriaClinica> hc = historiaClinicaService.obtenerPorId(id);

        if (hc.isEmpty()) {
            System.out.println("\nNo se encontró una historia clínica con ID: " + id);
            return;
        }

        mostrarDetalleHistoriaClinica(hc.get());

        System.out.print("\n¿Está seguro que desea eliminar esta historia clínica? (S/N): ");
        String confirmacion = scanner.nextLine().trim().toUpperCase();

        if (confirmacion.equals("S")) {
            historiaClinicaService.eliminar(id);
            System.out.println("\nHistoria clínica eliminada exitosamente (baja lógica).");
        } else {
            System.out.println("\nOperación cancelada.");
        }
    }

    // ==================== MENÚ OPERACIONES COMBINADAS ====================

    private void menuOperacionesCombinadas() {
        boolean volver = false;

        while (!volver) {
            System.out.println("\n┌─ OPERACIONES COMBINADAS ────────────────────────────────────────┐");
            System.out.println("│                                                           │");
            System.out.println("│  [1] Crear Paciente con Historia Clínica (Transacción)    │");
            System.out.println("│  [0] Volver al Menú Principal                             │");
            System.out.println("│                                                           │");
            System.out.println("└──────────────────────────────────────────────────────────────────────┘");
            System.out.print("\n➤ Seleccione una opción: ");

            String opcion = scanner.nextLine().trim().toUpperCase();

            try {
                switch (opcion) {
                    case "1" -> crearPacienteConHistoriaClinica();
                    case "0" -> volver = true;
                    default -> System.out.println("\nOpción inválida.");
                }
            } catch (Exception e) {
                System.err.println("\nError: " + e.getMessage());
            }

            if (!volver) {
                pausar();
            }
        }
    }

    private void crearPacienteConHistoriaClinica() {
        System.out.println("\n═══ CREAR PACIENTE CON HISTORIA CLÍNICA (TRANSACCIÓN) ═══\n");
        System.out.println("Esta operación crea un paciente y su historia clínica en una sola transacción.");
        System.out.println("Si ocurre un error, ambas operaciones se revierten (rollback).\n");

        // Datos del paciente
        System.out.println("--- DATOS DEL PACIENTE ---\n");

        System.out.print("Apellido: ");
        String apellido = scanner.nextLine().trim().toUpperCase();

        System.out.print("Nombre: ");
        String nombre = scanner.nextLine().trim().toUpperCase();

        System.out.print("DNI (sin puntos): ");
        String dni = scanner.nextLine().trim();

        System.out.print("Fecha de Nacimiento (dd/MM/yyyy): ");
        String fechaStr = scanner.nextLine().trim();
        LocalDate fechaNacimiento = parsearFecha(fechaStr);

        // Datos de la historia clínica
        System.out.println("\n--- DATOS DE LA HISTORIA CLÍNICA ---\n");

        System.out.print("Número de Historia: ");
        String nroHistoria = scanner.nextLine().trim().toUpperCase();

        System.out.println("\nGrupos Sanguíneos disponibles:");
        for (GrupoSanguineo gs : GrupoSanguineo.values()) {
            System.out.println("  - " + gs.getValor());
        }
        System.out.print("Grupo Sanguíneo: ");
        String grupoStr = scanner.nextLine().trim().toUpperCase();
        GrupoSanguineo grupoSanguineo = GrupoSanguineo.fromString(grupoStr);

        System.out.print("Antecedentes: ");
        String antecedentes = scanner.nextLine().trim();

        System.out.print("Medicación Actual (opcional): ");
        String medicacion = scanner.nextLine().trim();

        System.out.print("Observaciones: ");
        String observaciones = scanner.nextLine().trim();

        // Crear objetos
        Paciente paciente = new Paciente();
        paciente.setApellido(apellido);
        paciente.setNombre(nombre);
        paciente.setDni(dni);
        paciente.setFechaNacimiento(fechaNacimiento);
        paciente.setEliminado(false);

        HistoriaClinica hc = new HistoriaClinica();
        hc.setNroHistoria(nroHistoria);
        hc.setGrupoSanguineo(grupoSanguineo);
        hc.setAntecedentes(antecedentes);
        hc.setMedicacionActual(medicacion.isEmpty() ? null : medicacion);
        hc.setObservaciones(observaciones);
        hc.setEliminado(false);

        // Ejecutar transacción
        Paciente creado = pacienteService.crearConHistoriaClinica(paciente, hc);

        System.out.println("\nPaciente e Historia Clínica creados exitosamente en una transacción!");
        System.out.println("   - ID del Paciente: " + creado.getId());
        System.out.println("   - ID de la Historia Clínica: " + creado.getHistoriaClinica().getId());
    }

    // ==================== MÉTODOS AUXILIARES ====================

    private void mostrarDetallePaciente(Paciente p) {
        System.out.println("\n╔═══════════════════════════════════════════════════════════════╗");
        System.out.println("║                    DETALLE DEL PACIENTE                       ║");
        System.out.println("╚═══════════════════════════════════════════════════════════════╝");
        System.out.println("\n  ID:                  " + p.getId());
        System.out.println("  Apellido:            " + p.getApellido());
        System.out.println("  Nombre:              " + p.getNombre());
        System.out.println("  DNI:                 " + p.getDni());
        System.out.println("  Fecha de Nacimiento: " + p.getFechaNacimiento().format(formatoFecha));
        System.out.println("  Eliminado:           " + (p.isEliminado() ? "SÍ" : "NO"));

        if (p.getHistoriaClinica() != null) {
            HistoriaClinica hc = p.getHistoriaClinica();
            System.out.println("\n  ┌─ HISTORIA CLÍNICA ASOCIADA ─────────────────────────────┐");
            System.out.println("  │  ID:              " + hc.getId());
            System.out.println("  │  Nro. Historia:   " + hc.getNroHistoria());
            System.out.println("  │  Grupo Sanguíneo: " + hc.getGrupoSanguineo().getValor());
            System.out.println("  │  Antecedentes:    " + hc.getAntecedentes());
            System.out.println("  │  Medicación:      " + (hc.getMedicacionActual() != null ? hc.getMedicacionActual() : "N/A"));
            System.out.println("  │  Observaciones:   " + hc.getObservaciones());
            System.out.println("  └─────────────────────────────────────────────────────────┘");
        } else {
            System.out.println("\n  Historia Clínica:    NO ASOCIADA");
        }
    }

    private void mostrarDetalleHistoriaClinica(HistoriaClinica hc) {
        System.out.println("\n╔═══════════════════════════════════════════════════════════════╗");
        System.out.println("║                 DETALLE DE HISTORIA CLÍNICA                   ║");
        System.out.println("╚═══════════════════════════════════════════════════════════════╝");
        System.out.println("\n  ID:              " + hc.getId());
        System.out.println("  Nro. Historia:   " + hc.getNroHistoria());
        System.out.println("  Grupo Sanguíneo: " + hc.getGrupoSanguineo().getValor());
        System.out.println("  Antecedentes:    " + hc.getAntecedentes());
        System.out.println("  Medicación:      " + (hc.getMedicacionActual() != null ? hc.getMedicacionActual() : "N/A"));
        System.out.println("  Observaciones:   " + hc.getObservaciones());
        System.out.println("  Eliminado:       " + (hc.isEliminado() ? "SÍ" : "NO"));

        if (hc.getIdPaciente() != null) {
            System.out.println("  ID Paciente:     " + hc.getIdPaciente());
        } else {
            System.out.println("  ID Paciente:     NO ASOCIADO");
        }
    }

    private LocalDate parsearFecha(String fechaStr) {
        try {
            return LocalDate.parse(fechaStr, formatoFecha);
        } catch (DateTimeParseException e) {
            throw new ValidacionException("Formato de fecha inválido. Use dd/MM/yyyy");
        }
    }

    private Long leerLong() {
        try {
            String input = scanner.nextLine().trim();
            return Long.parseLong(input);
        } catch (NumberFormatException e) {
            throw new ValidacionException("Debe ingresar un número válido");
        }
    }

    private String truncar(String texto, int longitud) {
        if (texto == null) {
            return "";
        }
        if (texto.length() <= longitud) {
            return texto;
        }
        return texto.substring(0, longitud - 3) + "...";
    }

    private void pausar() {
        System.out.print("\nPresione Enter para continuar...");
        scanner.nextLine();
    }
}

