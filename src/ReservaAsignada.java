import java.util.List;

public class ReservaAsignada {
    // clase creada para pasar la informacion entre las clases, poder usarlas como
    // parametro en mesaGestor en asignar mesa y crear reserva con la mesa asignada.
    private Reserva reserva;
    private List<Integer> numerosMesa;

    public ReservaAsignada(Reserva reserva, List<Integer> numerosMesa) {
        this.reserva = reserva;
        this.numerosMesa = numerosMesa;
    }

    public Reserva getReserva() {
        return reserva;
    }

    public List<Integer> getNumerosMesa() {
        return numerosMesa;
    }
}
