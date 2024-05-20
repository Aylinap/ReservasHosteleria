import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class Reserva {
    private static final LocalTime[] horarios_reservas = {
            LocalTime.of(13, 30),
            LocalTime.of(13, 45),
            LocalTime.of(14, 00),
            LocalTime.of(14, 15)
    };
    private int id_reserva; // le paso un 0 en el constructor.
    private int id_cliente;
    private Date diaReserva;
    private LocalTime horaReserva;
    private int numero_comensales;
    private String descripcion;

    /*
     * int numeroMesa = reservaNueva.getMesa().getNumero_mesa(); // Ejemplo de cómo
     * obtener el número de mesa desde el
     * // objeto Mesa asociado a la reserva
     */

    public Reserva(int id_reserva, int id_cliente, Date diaReserva, LocalTime horaReserva,
            int numero_comensales, String descripcion) {
        this.id_reserva = id_reserva;
        this.id_cliente = id_cliente;
        this.diaReserva = diaReserva;
        this.horaReserva = horaReserva;
        this.numero_comensales = numero_comensales;
        this.descripcion = descripcion;

    }

    public int getId_reserva() {
        return id_reserva;
    }

    public void setId_reserva(int id_reserva) {
        this.id_reserva = id_reserva;
    }

    public int getId_cliente() {
        return id_cliente;
    }

    public void setId_cliente(int id_cliente) {
        this.id_cliente = id_cliente;
    }

    public LocalTime getHoraReserva() {
        return horaReserva;
    }

    public void setHoraReserva(LocalTime horaReserva) {
        this.horaReserva = horaReserva;
    }

    public Date getDiaReserva() {
        return diaReserva;
    }

    public void setDiaReserva(Date diaReserva) {
        this.diaReserva = diaReserva;
    }

    public int getNumero_comensales() {
        return numero_comensales;
    }

    public void setNumero_comensales(int numero_comensales) {
        this.numero_comensales = numero_comensales;
    }

    public static LocalTime[] getHorariosReservas() {
        return horarios_reservas;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public boolean esHoraPermitida() {
        LocalTime horaReserva = this.getHoraReserva();
        for (LocalTime franja : horarios_reservas) {
            if (horaReserva.truncatedTo(ChronoUnit.MINUTES).equals(franja)) {
                return true;
            }
        }
        return false;
    }

}
