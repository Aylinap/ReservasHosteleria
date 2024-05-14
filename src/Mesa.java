import java.util.ArrayList;

public class Mesa {
    private int numero_mesa; // quizas tenga que ser string
    private int sala; // donde está la mesa
    private boolean disponible;
    private boolean se_puede_juntar;
    private int capacidad;
    private boolean ocupada;
    private boolean reservada;
    private String tipo_mesa;
    private ArrayList<Mesa> lista_combinaciones_mesas;
    // si son 8 comensales y las mesas x-x-x estan disponibles, se da la combinacion
    // de mesas existentes dentro del arreglo.

    public Mesa(int numero_mesa, int sala, boolean disponible, boolean se_puede_juntar, int capacidad, boolean ocupada,
            boolean reservada, String tipo_mesa) {
        this.numero_mesa = numero_mesa;
        this.sala = sala;
        this.disponible = disponible;
        this.se_puede_juntar = se_puede_juntar;
        this.capacidad = capacidad;
        this.ocupada = ocupada;
        this.reservada = reservada;
        this.tipo_mesa = tipo_mesa;

    }

    public int getNumero_mesa() {
        return numero_mesa;
    }

    public void setNumero_mesa(int numero_mesa) {
        this.numero_mesa = numero_mesa;
    }

    public int getSala() {
        return sala;
    }

    public void setSala(int sala) {
        this.sala = sala;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

    public boolean isSe_puede_juntar() {
        return se_puede_juntar;
    }

    public void setSe_puede_juntar(boolean se_puede_juntar) {
        this.se_puede_juntar = se_puede_juntar;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }

    public boolean isOcupada() {
        return ocupada;
    }

    public void setOcupada(boolean ocupada) {
        this.ocupada = ocupada;
    }

    public boolean isReservada() {
        return reservada;
    }

    public void setReservada(boolean reservada) {
        this.reservada = reservada;
    }

    public String getTipo_mesa() {
        return tipo_mesa;
    }

    public void setTipo_mesa(String tipo_mesa) {
        this.tipo_mesa = tipo_mesa;
    }

}
