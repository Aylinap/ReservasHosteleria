public class Sala {
    private int numero_sala;

    public Sala(int numero_sala) {
        this.numero_sala = numero_sala;

    }

    public int getNumero_sala() {
        return numero_sala;
    }

    public void setNumero_sala(int numero_sala) {
        this.numero_sala = numero_sala;
    }

    @Override
    public String toString() {
        return "Sala [numero_sala=" + numero_sala + "]";
    }

}
