package ar.edu.uner.tpi.service;

import ar.edu.uner.tpi.dao.HistoriaClinicaDao;
import ar.edu.uner.tpi.entities.HistoriaClinica;
import ar.edu.uner.tpi.exceptions.ValidacionException;
import ar.edu.uner.tpi.util.Validador;

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
        validar(entidad);
        validarNroHistoriaUnico(entidad.getNroHistoria(), null);
        return dao.crear(entidad);
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

