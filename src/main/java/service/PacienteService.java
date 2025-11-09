package service;

import config.DatabaseConnection;
import dao.HistoriaClinicaDao;
import dao.PacienteDao;
import entities.HistoriaClinica;
import entities.Paciente;
import exceptions.DatabaseException;
import exceptions.ValidacionException;
import util.Validador;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Servicio para gestionar operaciones de Paciente
 * Incluye manejo de transacciones para operaciones compuestas
 */
public class PacienteService implements GenericService<Paciente> {

    private final PacienteDao pacienteDao;
    private final HistoriaClinicaDao historiaClinicaDao;

    public PacienteService() {
        this.pacienteDao = new PacienteDao();
        this.historiaClinicaDao = new HistoriaClinicaDao();
    }

    public Paciente insertar(Paciente entidad) {
        validar(entidad);
        validarDniUnico(entidad.getDni(), null);
        try { // <-- AÑADIR try
        return pacienteDao.crear(entidad);
    } catch (DatabaseException e) { // <-- CAPTURAR errores del DAO
        // Se lanza la excepción para que el menú la muestre claramente
        throw new DatabaseException("Error al guardar el paciente: " + e.getMessage(), e); 
    }
    }
        
    

    /**
     * Crea un paciente junto con su historia clínica en una transacción
     * Esto garantiza que ambas operaciones se completen o ninguna
     */
    public Paciente crearConHistoriaClinica(Paciente paciente, HistoriaClinica historiaClinica) {
        validar(paciente);
        validarDniUnico(paciente.getDni(), null);
        
        // Validar historia clínica
        if (historiaClinica == null) {
            throw new ValidacionException("La historia clínica no puede ser nula");
        }
        validarHistoriaClinica(historiaClinica);
        validarNroHistoriaUnico(historiaClinica.getNroHistoria(), null);

        Connection conexion = null;
        
        try {
            // Obtener conexión y comenzar transacción
            conexion = DatabaseConnection.getConnection();
            conexion.setAutoCommit(false);

            // 1. Crear el paciente
            Paciente pacienteCreado = pacienteDao.crear(conexion, paciente);

            // 2. Asociar la historia clínica al paciente
            historiaClinica.setIdPaciente(pacienteCreado.getId());

            // 3. Crear la historia clínica
            HistoriaClinica hcCreada = historiaClinicaDao.crear(conexion, historiaClinica);

            // 4. Asociar la HC al paciente
            pacienteCreado.setHistoriaClinica(hcCreada);

            // Commit de la transacción
            conexion.commit();

            return pacienteCreado;

        } catch (Exception e) {
            // Rollback en caso de error
            if (conexion != null) {
                try {
                    conexion.rollback();
                    System.err.println("Transacción revertida debido a error: " + e.getMessage());
                } catch (SQLException ex) {
                    System.err.println("Error al hacer rollback: " + ex.getMessage());
                }
            }
            throw new DatabaseException("Error al crear paciente con historia clínica: " + e.getMessage(), e);
            
        } finally {
            // Restablecer autoCommit y cerrar conexión
            if (conexion != null) {
                try {
                    conexion.setAutoCommit(true);
                    conexion.close();
                } catch (SQLException e) {
                    System.err.println("Error al cerrar la conexión: " + e.getMessage());
                }
            }
        }
    }

    /**
     * Asocia una historia clínica existente a un paciente
     */
    public void asociarHistoriaClinica(Long idPaciente, Long idHistoriaClinica) {
        Validador.validarNoNulo(idPaciente, "ID del paciente");
        Validador.validarNoNulo(idHistoriaClinica, "ID de la historia clínica");

        Connection conexion = null;

        try {
            conexion = DatabaseConnection.getConnection();
            conexion.setAutoCommit(false);

            // Verificar que el paciente existe
            Optional<Paciente> paciente = pacienteDao.leer(conexion, idPaciente);
            if (paciente.isEmpty()) {
                throw new ValidacionException("No existe un paciente con ID: " + idPaciente);
            }

            // Verificar que la historia clínica existe
            Optional<HistoriaClinica> hc = historiaClinicaDao.leer(conexion, idHistoriaClinica);
            if (hc.isEmpty()) {
                throw new ValidacionException("No existe una historia clínica con ID: " + idHistoriaClinica);
            }

            // Verificar que el paciente no tenga ya una historia clínica
            if (paciente.get().getHistoriaClinica() != null) {
                throw new ValidacionException(
                    "El paciente ya tiene una historia clínica asociada (relación 1->1)"
                );
            }

            // Verificar que la historia clínica no esté asociada a otro paciente
            if (hc.get().getIdPaciente() != null) {
                throw new ValidacionException(
                    "La historia clínica ya está asociada a otro paciente (relación 1->1)"
                );
            }

            // Actualizar la asociación
            HistoriaClinica historiaActualizada = hc.get();
            historiaActualizada.setIdPaciente(idPaciente);
            historiaClinicaDao.actualizar(conexion, historiaActualizada);

            conexion.commit();

        } catch (Exception e) {
            if (conexion != null) {
                try {
                    conexion.rollback();
                } catch (SQLException ex) {
                    System.err.println("Error al hacer rollback: " + ex.getMessage());
                }
            }
            throw new DatabaseException("Error al asociar historia clínica: " + e.getMessage(), e);

        } finally {
            if (conexion != null) {
                try {
                    conexion.setAutoCommit(true);
                    conexion.close();
                } catch (SQLException e) {
                    System.err.println("Error al cerrar la conexión: " + e.getMessage());
                }
            }
        }
    }

    @Override
    public Paciente actualizar(Paciente entidad) {
        validar(entidad);
        Validador.validarNoNulo(entidad.getId(), "ID");

        // Verificar que existe
        Optional<Paciente> existente = pacienteDao.leer(entidad.getId());
        if (existente.isEmpty()) {
            throw new ValidacionException("No existe un paciente con ID: " + entidad.getId());
        }

        // Validar que el DNI sea único (excepto para el mismo registro)
        validarDniUnico(entidad.getDni(), entidad.getId());

        boolean actualizado = pacienteDao.actualizar(entidad);
        if (!actualizado) {
            throw new ValidacionException("No se pudo actualizar el paciente");
        }

        return entidad;
    }

    @Override
    public void eliminar(Long id) {
        Validador.validarNoNulo(id, "ID");

        Optional<Paciente> existente = pacienteDao.leer(id);
        if (existente.isEmpty()) {
            throw new ValidacionException("No existe un paciente con ID: " + id);
        }

        Connection conexion = null;

        try {
            conexion = DatabaseConnection.getConnection();
            conexion.setAutoCommit(false);

            // Eliminar el paciente (baja lógica)
            boolean eliminado = pacienteDao.eliminar(conexion, id);
            if (!eliminado) {
                throw new ValidacionException("No se pudo eliminar el paciente");
            }

            // Si tiene historia clínica, también eliminarla (baja lógica)
            if (existente.get().getHistoriaClinica() != null) {
                Long idHc = existente.get().getHistoriaClinica().getId();
                historiaClinicaDao.eliminar(conexion, idHc);
            }

            conexion.commit();

        } catch (Exception e) {
            if (conexion != null) {
                try {
                    conexion.rollback();
                } catch (SQLException ex) {
                    System.err.println("Error al hacer rollback: " + ex.getMessage());
                }
            }
            throw new DatabaseException("Error al eliminar paciente: " + e.getMessage(), e);

        } finally {
            if (conexion != null) {
                try {
                    conexion.setAutoCommit(true);
                    conexion.close();
                } catch (SQLException e) {
                    System.err.println("Error al cerrar la conexión: " + e.getMessage());
                }
            }
        }
    }

    @Override
    public Optional<Paciente> obtenerPorId(Long id) {
        Validador.validarNoNulo(id, "ID");
        return pacienteDao.leer(id);
    }

    @Override
    public List<Paciente> obtenerTodos() {
        return pacienteDao.leerTodos();
    }

    /**
     * Busca un paciente por su DNI
     */
    public Optional<Paciente> buscarPorDni(String dni) {
        Validador.validarDni(dni);
        return pacienteDao.buscarPorDni(dni);
    }

    /**
     * Valida los datos de un paciente
     */
    private void validar(Paciente paciente) {
        if (paciente == null) {
            throw new ValidacionException("El paciente no puede ser nulo");
        }

        // Validar campos obligatorios
        Validador.validarNoVacio(paciente.getApellido(), "Apellido");
        // ️ CORRECCIÓN: APLICAR VALIDACIÓN DE LETRAS A APELLIDO
        Validador.validarSoloLetras(paciente.getApellido(), "Apellido");
        Validador.validarNoVacio(paciente.getNombre(), "Nombre");
        // ️ CORRECCIÓN: APLICAR VALIDACIÓN DE LETRAS A NOMBRE
        Validador.validarSoloLetras(paciente.getNombre(), "Nombre");
        Validador.validarDni(paciente.getDni());
        Validador.validarFechaNacimiento(paciente.getFechaNacimiento());

        // Validar longitudes
        Validador.validarLongitudMaxima(paciente.getApellido(), 40, "Apellido");
        Validador.validarLongitudMaxima(paciente.getNombre(), 40, "Nombre");
        Validador.validarLongitudMaxima(paciente.getDni(), 15, "DNI");
    }

    /**
     * Valida los datos de una historia clínica
     */
    private void validarHistoriaClinica(HistoriaClinica hc) {
        Validador.validarNoVacio(hc.getNroHistoria(), "Número de historia");
        Validador.validarNoNulo(hc.getGrupoSanguineo(), "Grupo sanguíneo");
        Validador.validarNoVacio(hc.getAntecedentes(), "Antecedentes");
        Validador.validarNoVacio(hc.getObservaciones(), "Observaciones");

        Validador.validarLongitudMaxima(hc.getNroHistoria(), 20, "Número de historia");
        Validador.validarLongitudMaxima(hc.getMedicacionActual(), 255, "Medicación actual");
    }

    /**
     * Valida que el DNI sea único
     */
    private void validarDniUnico(String dni, Long idExcluir) {
        Optional<Paciente> existente = pacienteDao.buscarPorDni(dni);

        if (existente.isPresent()) {
            // Si estamos actualizando, verificar que no sea el mismo registro
            if (idExcluir == null || !existente.get().getId().equals(idExcluir)) {
                throw new ValidacionException("Ya existe un paciente con DNI: " + dni);
            }
        }
    }

    /**
     * Valida que el número de historia sea único
     */
    private void validarNroHistoriaUnico(String nroHistoria, Long idExcluir) {
        Optional<HistoriaClinica> existente = historiaClinicaDao.buscarPorNroHistoria(nroHistoria);

        if (existente.isPresent()) {
            if (idExcluir == null || !existente.get().getId().equals(idExcluir)) {
                throw new ValidacionException("Ya existe una historia clínica con el número: " + nroHistoria);
            }
        }
    }
}

