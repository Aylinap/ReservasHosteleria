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
    private int numero_mesa;
    private int sala;
    private Date diaReserva;
    private LocalTime horaReserva;

    public Reserva(String nombre_cliente, int numero_mesa, int sala, Date diaReserva, LocalTime horaReserva) {
        this.nombre_cliente = nombre_cliente;
        this.numero_mesa = numero_mesa;
        this.sala = sala;
        this.diaReserva = diaReserva;
        this.horaReserva = horaReserva;
    }

    public LocalTime getHoraReserva() {
        return horaReserva;
    }

    public void setHoraReserva(LocalTime horaReserva) {
        this.horaReserva = horaReserva;
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

    public static LocalTime[] getHorariosReservas() {
        return horarios_reservas;
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
