package service;

import java.util.List;
import java.util.Optional;

/**
 * Interfaz genérica para servicios con operaciones CRUD
 * 
 * @param <T> el tipo de entidad que maneja este servicio
 */
public interface GenericService<T> {
    
    /**
     * Inserta una nueva entidad
     * 
     * @param entidad la entidad a insertar
     * @return la entidad insertada con su ID generado
     */
    T insertar(T entidad);

    /**
     * Actualiza una entidad existente
     * 
     * @param entidad la entidad con los datos actualizados
     * @return la entidad actualizada
     */
    T actualizar(T entidad);

    /**
     * Elimina (lógicamente) una entidad por su ID
     * 
     * @param id el ID de la entidad a eliminar
     */
    void eliminar(Long id);

    /**
     * Obtiene una entidad por su ID
     * 
     * @param id el ID de la entidad
     * @return Optional con la entidad si existe, Optional.empty() si no
     */
    Optional<T> obtenerPorId(Long id);

    /**
     * Obtiene todas las entidades no eliminadas
     * 
     * @return lista de todas las entidades activas
     */
    List<T> obtenerTodos();
}

