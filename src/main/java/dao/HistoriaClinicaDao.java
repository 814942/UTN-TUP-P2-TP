package dao;

import config.DatabaseConnection;
import entities.HistoriaClinica;
import enums.GrupoSanguineo;
import exceptions.DatabaseException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * DAO para la entidad HistoriaClinica
 */
public class HistoriaClinicaDao implements GenericDao<HistoriaClinica> {

    private static final String SQL_INSERT = 
        "INSERT INTO historiaClinica (eliminado, nro_historia, grupo_sangre, antecedentes, " +
        "medicacionActual, observaciones, id_paciente) VALUES (?, ?, ?, ?, ?, ?, ?)";
    
    private static final String SQL_SELECT_BY_ID = 
        "SELECT * FROM historiaClinica WHERE id = ? AND eliminado = false";
    
    private static final String SQL_SELECT_ALL = 
        "SELECT * FROM historiaClinica WHERE eliminado = false";
    
    private static final String SQL_UPDATE = 
        "UPDATE historiaClinica SET nro_historia = ?, grupo_sangre = ?, antecedentes = ?, " +
        "medicacionActual = ?, observaciones = ? WHERE id = ?";
    
    private static final String SQL_DELETE = 
        "UPDATE historiaClinica SET eliminado = true WHERE id = ?";
    
    private static final String SQL_SELECT_BY_NRO_HISTORIA = 
        "SELECT * FROM historiaClinica WHERE nro_historia = ? AND eliminado = false";

    private static final String SQL_SELECT_BY_PACIENTE = 
        "SELECT * FROM historiaClinica WHERE id_paciente = ? AND eliminado = false";

    @Override
    public HistoriaClinica crear(HistoriaClinica entidad) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            return crear(conn, entidad);
        } catch (SQLException e) {
            throw new DatabaseException("Error al crear historia clínica", e);
        }
    }

    @Override
    public HistoriaClinica crear(Connection conexion, HistoriaClinica entidad) {
        try (PreparedStatement stmt = conexion.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setBoolean(1, entidad.isEliminado());
            stmt.setString(2, entidad.getNroHistoria());
            stmt.setString(3, entidad.getGrupoSanguineo().getValor());
            stmt.setString(4, entidad.getAntecedentes());
            stmt.setString(5, entidad.getMedicacionActual());
            stmt.setString(6, entidad.getObservaciones());
            
            if (entidad.getIdPaciente() != null) {
                stmt.setLong(7, entidad.getIdPaciente());
            } else {
                stmt.setNull(7, Types.BIGINT);
            }
            
            int filasAfectadas = stmt.executeUpdate();
            
            if (filasAfectadas == 0) {
                throw new DatabaseException("Error al crear historia clínica, ninguna fila afectada");
            }
            
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    entidad.setId(generatedKeys.getLong(1));
                } else {
                    throw new DatabaseException("Error al crear historia clínica, no se obtuvo el ID");
                }
            }
            
            return entidad;
            
        } catch (SQLException e) {
            throw new DatabaseException("Error al crear historia clínica", e);
        }
    }

    @Override
    public Optional<HistoriaClinica> leer(Long id) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            return leer(conn, id);
        } catch (SQLException e) {
            throw new DatabaseException("Error al leer historia clínica", e);
        }
    }

    @Override
    public Optional<HistoriaClinica> leer(Connection conexion, Long id) {
        try (PreparedStatement stmt = conexion.prepareStatement(SQL_SELECT_BY_ID)) {
            
            stmt.setLong(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapearResultSet(rs));
                }
            }
            
            return Optional.empty();
            
        } catch (SQLException e) {
            throw new DatabaseException("Error al leer historia clínica", e);
        }
    }

    @Override
    public List<HistoriaClinica> leerTodos() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            return leerTodos(conn);
        } catch (SQLException e) {
            throw new DatabaseException("Error al leer historias clínicas", e);
        }
    }

    @Override
    public List<HistoriaClinica> leerTodos(Connection conexion) {
        List<HistoriaClinica> historias = new ArrayList<>();
        
        try (PreparedStatement stmt = conexion.prepareStatement(SQL_SELECT_ALL);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                historias.add(mapearResultSet(rs));
            }
            
            return historias;
            
        } catch (SQLException e) {
            throw new DatabaseException("Error al leer historias clínicas", e);
        }
    }

    @Override
    public boolean actualizar(HistoriaClinica entidad) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            return actualizar(conn, entidad);
        } catch (SQLException e) {
            throw new DatabaseException("Error al actualizar historia clínica", e);
        }
    }

    @Override
    public boolean actualizar(Connection conexion, HistoriaClinica entidad) {
        try (PreparedStatement stmt = conexion.prepareStatement(SQL_UPDATE)) {
            
            stmt.setString(1, entidad.getNroHistoria());
            stmt.setString(2, entidad.getGrupoSanguineo().getValor());
            stmt.setString(3, entidad.getAntecedentes());
            stmt.setString(4, entidad.getMedicacionActual());
            stmt.setString(5, entidad.getObservaciones());
            stmt.setLong(6, entidad.getId());
            
            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0;
            
        } catch (SQLException e) {
            throw new DatabaseException("Error al actualizar historia clínica", e);
        }
    }

    @Override
    public boolean eliminar(Long id) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            return eliminar(conn, id);
        } catch (SQLException e) {
            throw new DatabaseException("Error al eliminar historia clínica", e);
        }
    }

    @Override
    public boolean eliminar(Connection conexion, Long id) {
        try (PreparedStatement stmt = conexion.prepareStatement(SQL_DELETE)) {
            
            stmt.setLong(1, id);
            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0;
            
        } catch (SQLException e) {
            throw new DatabaseException("Error al eliminar historia clínica", e);
        }
    }

    /**
     * Busca una historia clínica por número de historia
     */
    public Optional<HistoriaClinica> buscarPorNroHistoria(String nroHistoria) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_SELECT_BY_NRO_HISTORIA)) {
            
            stmt.setString(1, nroHistoria);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapearResultSet(rs));
                }
            }
            
            return Optional.empty();
            
        } catch (SQLException e) {
            throw new DatabaseException("Error al buscar historia clínica por número", e);
        }
    }

    /**
     * Busca la historia clínica asociada a un paciente
     */
    public Optional<HistoriaClinica> buscarPorIdPaciente(Long idPaciente) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_SELECT_BY_PACIENTE)) {
            
            stmt.setLong(1, idPaciente);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapearResultSet(rs));
                }
            }
            
            return Optional.empty();
            
        } catch (SQLException e) {
            throw new DatabaseException("Error al buscar historia clínica por paciente", e);
        }
    }

    /**
     * Mapea un ResultSet a un objeto HistoriaClinica
     */
    private HistoriaClinica mapearResultSet(ResultSet rs) throws SQLException {
        HistoriaClinica hc = new HistoriaClinica();
        hc.setId(rs.getLong("id"));
        hc.setEliminado(rs.getBoolean("eliminado"));
        hc.setNroHistoria(rs.getString("nro_historia"));
        hc.setGrupoSanguineo(GrupoSanguineo.fromString(rs.getString("grupo_sangre")));
        hc.setAntecedentes(rs.getString("antecedentes"));
        hc.setMedicacionActual(rs.getString("medicacionActual"));
        hc.setObservaciones(rs.getString("observaciones"));
        
        long idPaciente = rs.getLong("id_paciente");
        if (!rs.wasNull()) {
            hc.setIdPaciente(idPaciente);
        }
        
        return hc;
    }
}

