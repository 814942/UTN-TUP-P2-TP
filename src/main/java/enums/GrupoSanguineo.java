package enums;

/**
 * Enumeración que representa los grupos sanguíneos posibles
 */
public enum GrupoSanguineo {
    A_POSITIVO("A+"),
    A_NEGATIVO("A-"),
    B_POSITIVO("B+"),
    B_NEGATIVO("B-"),
    AB_POSITIVO("AB+"),
    AB_NEGATIVO("AB-"),
    O_POSITIVO("O+"),
    O_NEGATIVO("O-");

    private final String valor;

    GrupoSanguineo(String valor) {
        this.valor = valor;
    }

    public String getValor() {
        return valor;
    }

    /**
     * Convierte una cadena a un GrupoSanguineo
     * @param texto el valor del grupo sanguíneo
     * @return el enum correspondiente
     * @throws IllegalArgumentException si el valor no es válido
     */
    public static GrupoSanguineo fromString(String texto) {
        for (GrupoSanguineo gs : GrupoSanguineo.values()) {
            if (gs.valor.equalsIgnoreCase(texto)) {
                return gs;
            }
        }
        throw new IllegalArgumentException("Grupo sanguíneo inválido: " + texto);
    }

    @Override
    public String toString() {
        return valor;
    }
}

