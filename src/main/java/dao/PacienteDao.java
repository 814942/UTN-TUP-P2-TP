package dao;

import config.DatabaseConnection;
import entities.HistoriaClinica;
import entities.Paciente;
import enums.GrupoSanguineo;
import exceptions.DatabaseException;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * DAO para la entidad Paciente
 */
public class PacienteDao implements GenericDao<Paciente> {

    private static final String SQL_INSERT = 
        "INSERT INTO paciente (eliminado, apellido, nombre, dni, fecha_nac) VALUES (?, ?, ?, ?, ?)";
    
    private static final String SQL_SELECT_BY_ID = 
        "SELECT p.*, hc.id as hc_id, hc.eliminado as hc_eliminado, hc.nro_historia, " +
        "hc.grupo_sangre, hc.antecedentes, hc.medicacionActual, hc.observaciones " +
        "FROM paciente p " +
        "LEFT JOIN historiaClinica hc ON p.id = hc.id_paciente AND hc.eliminado = false " +
        "WHERE p.id = ? AND p.eliminado = false";
    
    private static final String SQL_SELECT_ALL = 
        "SELECT p.*, hc.id as hc_id, hc.eliminado as hc_eliminado, hc.nro_historia, " +
        "hc.grupo_sangre, hc.antecedentes, hc.medicacionActual, hc.observaciones " +
        "FROM paciente p " +
        "LEFT JOIN historiaClinica hc ON p.id = hc.id_paciente AND hc.eliminado = false " +
        "WHERE p.eliminado = false";
    
    private static final String SQL_UPDATE = 
        "UPDATE paciente SET apellido = ?, nombre = ?, dni = ?, fecha_nac = ? WHERE id = ?";
    
    private static final String SQL_DELETE = 
        "UPDATE paciente SET eliminado = true WHERE id = ?";
    
    private static final String SQL_SELECT_BY_DNI = 
        "SELECT p.*, hc.id as hc_id, hc.eliminado as hc_eliminado, hc.nro_historia, " +
        "hc.grupo_sangre, hc.antecedentes, hc.medicacionActual, hc.observaciones " +
        "FROM paciente p " +
        "LEFT JOIN historiaClinica hc ON p.id = hc.id_paciente AND hc.eliminado = false " +
        "WHERE p.dni = ? AND p.eliminado = false";

    @Override
    public Paciente crear(Paciente entidad) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            return crear(conn, entidad);
        } catch (SQLException e) {
            throw new DatabaseException("Error al crear paciente", e);
        }
    }

    @Override
    public Paciente crear(Connection conexion, Paciente entidad) {
        try (PreparedStatement stmt = conexion.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setBoolean(1, entidad.isEliminado());
            stmt.setString(2, entidad.getApellido());
            stmt.setString(3, entidad.getNombre());
            stmt.setString(4, entidad.getDni());
            stmt.setDate(5, Date.valueOf(entidad.getFechaNacimiento()));
            
            int filasAfectadas = stmt.executeUpdate();
            
            if (filasAfectadas == 0) {
                throw new DatabaseException("Error al crear paciente, ninguna fila afectada");
            }
            
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    entidad.setId(generatedKeys.getLong(1));
                } else {
                    throw new DatabaseException("Error al crear paciente, no se obtuvo el ID");
                }
            }
            
            return entidad;
            
        } catch (SQLException e) {
            throw new DatabaseException("Error al crear paciente", e);
        }
    }

    @Override
    public Optional<Paciente> leer(Long id) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            return leer(conn, id);
        } catch (SQLException e) {
            throw new DatabaseException("Error al leer paciente", e);
        }
    }

    @Override
    public Optional<Paciente> leer(Connection conexion, Long id) {
        try (PreparedStatement stmt = conexion.prepareStatement(SQL_SELECT_BY_ID)) {
            
            stmt.setLong(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapearResultSet(rs));
                }
            }
            
            return Optional.empty();
            
        } catch (SQLException e) {
            throw new DatabaseException("Error al leer paciente", e);
        }
    }

    @Override
    public List<Paciente> leerTodos() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            return leerTodos(conn);
        } catch (SQLException e) {
            throw new DatabaseException("Error al leer pacientes", e);
        }
    }

    @Override
    public List<Paciente> leerTodos(Connection conexion) {
        List<Paciente> pacientes = new ArrayList<>();
        
        try (PreparedStatement stmt = conexion.prepareStatement(SQL_SELECT_ALL);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                pacientes.add(mapearResultSet(rs));
            }
            
            return pacientes;
            
        } catch (SQLException e) {
            throw new DatabaseException("Error al leer pacientes", e);
        }
    }

    @Override
    public boolean actualizar(Paciente entidad) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            return actualizar(conn, entidad);
        } catch (SQLException e) {
            throw new DatabaseException("Error al actualizar paciente", e);
        }
    }

    @Override
    public boolean actualizar(Connection conexion, Paciente entidad) {
        try (PreparedStatement stmt = conexion.prepareStatement(SQL_UPDATE)) {
            
            stmt.setString(1, entidad.getApellido());
            stmt.setString(2, entidad.getNombre());
            stmt.setString(3, entidad.getDni());
            stmt.setDate(4, Date.valueOf(entidad.getFechaNacimiento()));
            stmt.setLong(5, entidad.getId());
            
            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0;
            
        } catch (SQLException e) {
            throw new DatabaseException("Error al actualizar paciente", e);
        }
    }

    @Override
    public boolean eliminar(Long id) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            return eliminar(conn, id);
        } catch (SQLException e) {
            throw new DatabaseException("Error al eliminar paciente", e);
        }
    }

    @Override
    public boolean eliminar(Connection conexion, Long id) {
        try (PreparedStatement stmt = conexion.prepareStatement(SQL_DELETE)) {
            
            stmt.setLong(1, id);
            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0;
            
        } catch (SQLException e) {
            throw new DatabaseException("Error al eliminar paciente", e);
        }
    }

    /**
     * Busca un paciente por su DNI
     */
    public Optional<Paciente> buscarPorDni(String dni) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_SELECT_BY_DNI)) {
            
            stmt.setString(1, dni);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapearResultSet(rs));
                }
            }
            
            return Optional.empty();
            
        } catch (SQLException e) {
            throw new DatabaseException("Error al buscar paciente por DNI", e);
        }
    }

    /**
     * Mapea un ResultSet a un objeto Paciente (con su HistoriaClinica si existe)
     */
    private Paciente mapearResultSet(ResultSet rs) throws SQLException {
        Paciente paciente = new Paciente();
        paciente.setId(rs.getLong("id"));
        paciente.setEliminado(rs.getBoolean("eliminado"));
        paciente.setApellido(rs.getString("apellido"));
        paciente.setNombre(rs.getString("nombre"));
        paciente.setDni(rs.getString("dni"));
        
        Date fechaNac = rs.getDate("fecha_nac");
        if (fechaNac != null) {
            paciente.setFechaNacimiento(fechaNac.toLocalDate());
        }
        
        // Mapear HistoriaClinica si existe
        long hcId = rs.getLong("hc_id");
        if (!rs.wasNull() && hcId > 0) {
            HistoriaClinica hc = new HistoriaClinica();
            hc.setId(hcId);
            hc.setEliminado(rs.getBoolean("hc_eliminado"));
            hc.setNroHistoria(rs.getString("nro_historia"));
            
            String grupoSangre = rs.getString("grupo_sangre");
            if (grupoSangre != null) {
                hc.setGrupoSanguineo(GrupoSanguineo.fromString(grupoSangre));
            }
            
            hc.setAntecedentes(rs.getString("antecedentes"));
            hc.setMedicacionActual(rs.getString("medicacionActual"));
            hc.setObservaciones(rs.getString("observaciones"));
            hc.setIdPaciente(paciente.getId());
            
            paciente.setHistoriaClinica(hc);
        }
        
        return paciente;
    }
}

