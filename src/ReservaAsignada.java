public class ReservaAsignada {
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
