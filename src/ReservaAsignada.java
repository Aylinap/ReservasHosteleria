public class ReservaAsignada {
    // clase creada para pasar la informacion entre las clases, poder usarlas como
    // parametro en mesaGestor en asignar mesa y crear reserva con la mesa asignada.
    private Reserva reserva;
    private int numeroMesa;

    public ReservaAsignada(Reserva reserva, int numeroMesa) {
        this.reserva = reserva;
        this.numeroMesa = numeroMesa;
    }

    public Reserva getReserva() {
        return reserva;
    }

    public int getNumeroMesa() {
        return numeroMesa;
    }
}
