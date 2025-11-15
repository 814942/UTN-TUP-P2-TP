package service;

import dao.HistoriaClinicaDao;
import entities.HistoriaClinica;
import exceptions.ValidacionException;
import util.Validador;

import java.util.List;
import java.util.Optional;

/**
 * Servicio para gestionar operaciones de HistoriaClinica
 */
public class HistoriaClinicaService implements GenericService<HistoriaClinica> {

    private final HistoriaClinicaDao dao;

    public HistoriaClinicaService() {
        this.dao = new HistoriaClinicaDao();
    }

    @Override
    public HistoriaClinica insertar(HistoriaClinica entidad) {
        try {
            validar(entidad);
            
            validarNroHistoriaUnico(entidad.getNroHistoria(), null);
            
            HistoriaClinica resultado = dao.crear(entidad);
            return resultado;
            
        } catch (Exception e) {
            System.err.println("[ERROR] HistoriaClinicaService.insertar - Excepción:");
            System.err.println("  - Tipo: " + e.getClass().getName());
            System.err.println("  - Mensaje: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public HistoriaClinica actualizar(HistoriaClinica entidad) {
        validar(entidad);
        Validador.validarNoNulo(entidad.getId(), "ID");
        
        // Verificar que existe
        Optional<HistoriaClinica> existente = dao.leer(entidad.getId());
        if (existente.isEmpty()) {
            throw new ValidacionException("No existe una historia clínica con ID: " + entidad.getId());
        }
        
        // Validar que el número de historia sea único (excepto para el mismo registro)
        validarNroHistoriaUnico(entidad.getNroHistoria(), entidad.getId());
        
        boolean actualizado = dao.actualizar(entidad);
        if (!actualizado) {
            throw new ValidacionException("No se pudo actualizar la historia clínica");
        }
        
        return entidad;
    }

    @Override
    public void eliminar(Long id) {
        Validador.validarNoNulo(id, "ID");
        
        Optional<HistoriaClinica> existente = dao.leer(id);
        if (existente.isEmpty()) {
            throw new ValidacionException("No existe una historia clínica con ID: " + id);
        }
        
        boolean eliminado = dao.eliminar(id);
        if (!eliminado) {
            throw new ValidacionException("No se pudo eliminar la historia clínica");
        }
    }

    @Override
    public Optional<HistoriaClinica> obtenerPorId(Long id) {
        Validador.validarNoNulo(id, "ID");
        return dao.leer(id);
    }

    @Override
    public List<HistoriaClinica> obtenerTodos() {
        return dao.leerTodos();
    }

    /**
     * Busca una historia clínica por su número
     */
    public Optional<HistoriaClinica> buscarPorNumero(String nroHistoria) {
        Validador.validarNoVacio(nroHistoria, "Número de historia");
        return dao.buscarPorNroHistoria(nroHistoria);
    }

    /**
     * Busca una historia clínica por ID de paciente
     */
    public Optional<HistoriaClinica> buscarPorIdPaciente(Long idPaciente) {
        Validador.validarNoNulo(idPaciente, "ID del paciente");
        return dao.buscarPorIdPaciente(idPaciente);
    }

    /**
     * Crea o actualiza una historia clínica para un paciente.
     * Si el paciente ya tiene una HC, CONCATENA los nuevos datos (historial acumulativo).
     * Si no, crea una nueva HC.
     * 
     * CAMPOS QUE SE CONCATENAN (mantienen historial):
     * - antecedentes
     * - medicacionActual
     * - observaciones
     * 
     * CAMPOS QUE SE REEMPLAZAN (valor actual):
     * - grupoSanguineo
     */
    public HistoriaClinica crearOActualizar(HistoriaClinica entidad) {        
        validar(entidad);
        Validador.validarNoNulo(entidad.getIdPaciente(), "ID del paciente");
        
        // Buscar si ya existe una HC para este paciente
        Optional<HistoriaClinica> existente = dao.buscarPorIdPaciente(entidad.getIdPaciente());
        
        if (existente.isPresent()) {
            // Actualizar la HC existente CONCATENANDO la información
            HistoriaClinica hcExistente = existente.get();
            
            // Mantener el ID y el número de historia original
            entidad.setId(hcExistente.getId());
            entidad.setNroHistoria(hcExistente.getNroHistoria());
            
            // CONCATENAR campos históricos con separador de fecha/hora
            String separador = "\n\n--- ENTRADA: " + java.time.LocalDateTime.now().format(
                java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")) + " ---\n";
            
            // Concatenar antecedentes
            String antecedentesAcumulados = hcExistente.getAntecedentes() + separador + entidad.getAntecedentes();
            entidad.setAntecedentes(antecedentesAcumulados);
            
            // Concatenar medicación (si hay nueva medicación)
            if (entidad.getMedicacionActual() != null && !entidad.getMedicacionActual().trim().isEmpty()) {
                String medicacionBase = (hcExistente.getMedicacionActual() != null) 
                    ? hcExistente.getMedicacionActual() 
                    : "";
                String medicacionAcumulada = medicacionBase + separador + entidad.getMedicacionActual();
                entidad.setMedicacionActual(medicacionAcumulada);
            } else {
                // Si no hay nueva medicación, mantener la anterior
                entidad.setMedicacionActual(hcExistente.getMedicacionActual());
            }
            
            // Concatenar observaciones
            String observacionesAcumuladas = hcExistente.getObservaciones() + separador + entidad.getObservaciones();
            entidad.setObservaciones(observacionesAcumuladas);
            
            // El grupo sanguíneo se REEMPLAZA (siempre usar el más reciente)
            
            // Actualizar en la base de datos
            boolean actualizado = dao.actualizar(entidad);
            if (!actualizado) {
                throw new ValidacionException("No se pudo actualizar la historia clínica");
            }
            
            return entidad;
            
        } else {
            // Crear nueva HC
            validarNroHistoriaUnico(entidad.getNroHistoria(), null);
            HistoriaClinica resultado = dao.crear(entidad);
            return resultado;
        }
    }

    /**
     * Valida los datos de una historia clínica
     */
    private void validar(HistoriaClinica hc) {
        if (hc == null) {
            throw new ValidacionException("La historia clínica no puede ser nula");
        }

        // Validar campos obligatorios
        Validador.validarNoVacio(hc.getNroHistoria(), "Número de historia");
        Validador.validarNoNulo(hc.getGrupoSanguineo(), "Grupo sanguíneo");
        Validador.validarNoVacio(hc.getAntecedentes(), "Antecedentes");
        Validador.validarNoVacio(hc.getObservaciones(), "Observaciones");

        // Validar longitudes
        Validador.validarLongitudMaxima(hc.getNroHistoria(), 20, "Número de historia");
        Validador.validarLongitudMaxima(hc.getMedicacionActual(), 255, "Medicación actual");
    }

    /**
     * Valida que el número de historia sea único
     */
    private void validarNroHistoriaUnico(String nroHistoria, Long idExcluir) {
        Optional<HistoriaClinica> existente = dao.buscarPorNroHistoria(nroHistoria);
        
        if (existente.isPresent()) {
            // Si estamos actualizando, verificar que no sea el mismo registro
            if (idExcluir == null || !existente.get().getId().equals(idExcluir)) {
                throw new ValidacionException("Ya existe una historia clínica con el número: " + nroHistoria);
            }
        }
    }
}

