import java.util.ArrayList;

public class Mesa {
    private int numero_mesa; // quizas tenga que ser string
    private Sala sala; // donde está la mesa
    private boolean disponible;
    private boolean se_puede_juntar;
    private int capacidad;
    private boolean ocupada; // como paso un booleano a un string para insertarlo? tengo que parsearlo.
    private boolean reservada; // podria pasarlos todos a un string y poner estado de la mesa nada mas, pero
                               // sino como hare la logica dentro del programa
    // si son 8 comensales y las mesas x-x-x estan disponibles, se da la combinacion
    // de mesas existentes dentro del arreglo.

    // borre Mesa mesa del constructor, por ahora no se lo paso como parametro.
    public Mesa(int numero_mesa, Sala sala, boolean disponible, boolean se_puede_juntar, int capacidad, boolean ocupada,
            boolean reservada) {
        this.numero_mesa = numero_mesa;
        this.sala = sala;
        this.disponible = disponible;
        this.se_puede_juntar = se_puede_juntar; // igual podria sacarlo del constructor
        this.capacidad = capacidad;
        this.ocupada = ocupada;
        this.reservada = reservada;

    }

    public int getNumero_mesa() {
        return numero_mesa;
    }

    public void setNumero_mesa(int numero_mesa) {
        this.numero_mesa = numero_mesa;
    }

    public Sala getSala() {
        return sala;
    }

    public void setSala(Sala sala) {
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

}
