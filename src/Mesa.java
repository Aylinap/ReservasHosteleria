public class Mesa {
    private int numero_mesa;
    private Sala sala;
    private boolean disponible;
    private boolean se_puede_juntar;
    private int capacidad;
    private boolean ocupada;
    private boolean reservada;
    
    public Mesa(int numero_mesa, Sala sala, boolean disponible, boolean se_puede_juntar, int capacidad){
        this.numero_mesa = numero_mesa;
        this.sala = sala;
        this.disponible = disponible;
        this.se_puede_juntar = se_puede_juntar;
        this.capacidad = capacidad;
    }
}
