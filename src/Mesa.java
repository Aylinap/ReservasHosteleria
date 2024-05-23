import java.util.ArrayList;

public class Mesa {
    private int numero_mesa; // quizas tenga que ser string
    private int capacidad;
    private String estado; // estado de la mesa, disponible ocupada
    private int sala;
    private boolean combinable;

    public Mesa(int numero_mesa, int capacidad, String estado, int sala, boolean combinable) {
        this.numero_mesa = numero_mesa;
        this.capacidad = capacidad;
        this.estado = estado;
        this.sala = sala;
        this.combinable = combinable;

    }

    // constructor de mesa que pasa el numero de mesa.( al final no lo ocupé)
    public Mesa(int numero_mesa) {
        this.numero_mesa = numero_mesa;
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

    public int getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public boolean isCombinable() {
        return combinable;
    }

    public void setCombinable(boolean combinable) {
        this.combinable = combinable;
    }

}
