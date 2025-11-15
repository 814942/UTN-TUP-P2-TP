package entities;

import enums.GrupoSanguineo;

/**
 * Entidad que representa una Historia Clínica (Clase B en la relación 1->1)
 */
public class HistoriaClinica {
    private Long id;
    private boolean eliminado;
    private String nroHistoria;
    private GrupoSanguineo grupoSanguineo;
    private String antecedentes;
    private String medicacionActual;
    private String observaciones;
    private Long idPaciente;

    // Constructor vacío
    public HistoriaClinica() {
    }

    // Constructor completo
    public HistoriaClinica(Long id, boolean eliminado, String nroHistoria, 
        GrupoSanguineo grupoSanguineo, String antecedentes, 
        String medicacionActual, String observaciones, Long idPaciente) {
        this.id = id;
        this.eliminado = eliminado;
        this.nroHistoria = nroHistoria;
        this.grupoSanguineo = grupoSanguineo;
        this.antecedentes = antecedentes;
        this.medicacionActual = medicacionActual;
        this.observaciones = observaciones;
        this.idPaciente = idPaciente;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isEliminado() {
        return eliminado;
    }

    public void setEliminado(boolean eliminado) {
        this.eliminado = eliminado;
    }

    public String getNroHistoria() {
        return nroHistoria;
    }

    public void setNroHistoria(String nroHistoria) {
        this.nroHistoria = nroHistoria;
    }

    public GrupoSanguineo getGrupoSanguineo() {
        return grupoSanguineo;
    }

    public void setGrupoSanguineo(GrupoSanguineo grupoSanguineo) {
        this.grupoSanguineo = grupoSanguineo;
    }

    public String getAntecedentes() {
        return antecedentes;
    }

    public void setAntecedentes(String antecedentes) {
        this.antecedentes = antecedentes;
    }

    public String getMedicacionActual() {
        return medicacionActual;
    }

    public void setMedicacionActual(String medicacionActual) {
        this.medicacionActual = medicacionActual;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Long getIdPaciente() {
        return idPaciente;
    }

    public void setIdPaciente(Long idPaciente) {
        this.idPaciente = idPaciente;
    }

    @Override
    public String toString() {
        return "HistoriaClinica{" +
                "id=" + id +
                ", eliminado=" + eliminado +
                ", nroHistoria='" + nroHistoria + '\'' +
                ", grupoSanguineo=" + grupoSanguineo +
                ", antecedentes='" + antecedentes + '\'' +
                ", medicacionActual='" + medicacionActual + '\'' +
                ", observaciones='" + observaciones + '\'' +
                ", idPaciente=" + idPaciente +
                '}';
    }
}

