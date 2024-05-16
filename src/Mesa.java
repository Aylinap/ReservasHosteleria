import java.util.ArrayList;

public class Mesa {
    private int numero_mesa; // quizas tenga que ser string
    private Sala sala; // donde está la mesa pasarlo String
    private int capacidad;
    private Estado estadomesa; // terminara siendo string
    // como paso un booleano a un string para insertarlo? tengo que parsearlo.
    // podria pasarlos todos a un string y poner estado de la mesa nada mas, pero
    // sino como hare la logica dentro del programa
    // si son 8 comensales y las mesas x-x-x estan disponibles, se da la combinacion
    // de mesas existentes dentro del arreglo.

    // borre Mesa mesa del constructor, por ahora no se lo paso como parametro.
    public Mesa(int numero_mesa, Sala sala, int capacidad) {
        this.numero_mesa = numero_mesa;
        this.sala = sala;
        this.capacidad = capacidad;
    }

    // constructor de mesa que pasa el numero de mesa.
    public Mesa(int numero_mesa) {
        this.numero_mesa = numero_mesa;
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

    public int getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }

}
