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
    private String nombre_cliente;
    private Date diaReserva;
    private LocalTime horaReserva;
    private int numero_comensales;
    private String descripcion;
    private Mesa mesa;

    // no estoy pasando mesa en el constructor, solo la uso para usar la clase mesa
    // dentro de reserva.
    public Reserva(String nombre_cliente, Date diaReserva, LocalTime horaReserva,
            int numero_comensales, String descripcion) {
        this.nombre_cliente = nombre_cliente;
        this.diaReserva = diaReserva;
        this.horaReserva = horaReserva;
        this.numero_comensales = numero_comensales;
        this.descripcion = descripcion;

    }

    public LocalTime getHoraReserva() {
        return horaReserva;
    }

    public void setHoraReserva(LocalTime horaReserva) {
        this.horaReserva = horaReserva;
    }

    public String getNombre_cliente() {
        return nombre_cliente;
    }

    public void setNombre_cliente(String nombre_cliente) {
        this.nombre_cliente = nombre_cliente;
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

    public Mesa getMesa() {
        return mesa;
    }

    public void setMesa(Mesa mesa) {
        this.mesa = mesa;
    }

}
