package dao;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;

/**
 * Interfaz genérica para operaciones CRUD en la base de datos
 * 
 * @param <T> el tipo de entidad que maneja este DAO
 */
public interface GenericDao<T> {
    
    /**
     * Crea una nueva entidad en la base de datos
     * 
     * @param entidad la entidad a crear
     * @return la entidad creada con su ID generado
     */
    T crear(T entidad);

    /**
     * Crea una nueva entidad usando una conexión existente (para transacciones)
     * 
     * @param conexion la conexión a usar
     * @param entidad la entidad a crear
     * @return la entidad creada con su ID generado
     */
    T crear(Connection conexion, T entidad);

    /**
     * Lee una entidad por su ID
     * 
     * @param id el ID de la entidad
     * @return Optional con la entidad si existe, Optional.empty() si no
     */
    Optional<T> leer(Long id);

    /**
     * Lee una entidad por su ID usando una conexión existente
     * 
     * @param conexion la conexión a usar
     * @param id el ID de la entidad
     * @return Optional con la entidad si existe, Optional.empty() si no
     */
    Optional<T> leer(Connection conexion, Long id);

    /**
     * Lee todas las entidades no eliminadas
     * 
     * @return lista de todas las entidades activas
     */
    List<T> leerTodos();

    /**
     * Lee todas las entidades no eliminadas usando una conexión existente
     * 
     * @param conexion la conexión a usar
     * @return lista de todas las entidades activas
     */
    List<T> leerTodos(Connection conexion);

    /**
     * Actualiza una entidad existente
     * 
     * @param entidad la entidad con los datos actualizados
     * @return true si se actualizó correctamente
     */
    boolean actualizar(T entidad);

    /**
     * Actualiza una entidad usando una conexión existente
     * 
     * @param conexion la conexión a usar
     * @param entidad la entidad con los datos actualizados
     * @return true si se actualizó correctamente
     */
    boolean actualizar(Connection conexion, T entidad);

    /**
     * Elimina (lógicamente) una entidad por su ID
     * 
     * @param id el ID de la entidad a eliminar
     * @return true si se eliminó correctamente
     */
    boolean eliminar(Long id);

    /**
     * Elimina (lógicamente) una entidad usando una conexión existente
     * 
     * @param conexion la conexión a usar
     * @param id el ID de la entidad a eliminar
     * @return true si se eliminó correctamente
     */
    boolean eliminar(Connection conexion, Long id);
}

