package entities;

import java.time.LocalDate;

/**
 * Entidad que representa un Paciente (Clase A en la relación 1->1)
 * Contiene la referencia unidireccional a HistoriaClinica
 */
public class Paciente {
    private Long id;
    private boolean eliminado;
    private String apellido;
    private String nombre;
    private String dni;
    private LocalDate fechaNacimiento;
    private HistoriaClinica historiaClinica;

    // Constructor vacío
    public Paciente() {
    }

    // Constructor completo
    public Paciente(Long id, boolean eliminado, String apellido, String nombre, 
                   String dni, LocalDate fechaNacimiento, HistoriaClinica historiaClinica) {
        this.id = id;
        this.eliminado = eliminado;
        this.apellido = apellido;
        this.nombre = nombre;
        this.dni = dni;
        this.fechaNacimiento = fechaNacimiento;
        this.historiaClinica = historiaClinica;
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

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public HistoriaClinica getHistoriaClinica() {
        return historiaClinica;
    }

    public void setHistoriaClinica(HistoriaClinica historiaClinica) {
        this.historiaClinica = historiaClinica;
    }

    @Override
    public String toString() {
        return "Paciente{" +
                "id=" + id +
                ", eliminado=" + eliminado +
                ", apellido='" + apellido + '\'' +
                ", nombre='" + nombre + '\'' +
                ", dni='" + dni + '\'' +
                ", fechaNacimiento=" + fechaNacimiento +
                ", historiaClinica=" + (historiaClinica != null ? historiaClinica.getNroHistoria() : "Sin HC") +
                '}';
    }
}

