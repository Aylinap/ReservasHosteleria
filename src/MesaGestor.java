import java.sql.SQLException;
import java.util.*;
import java.time.LocalTime;

public class MesaGestor {
    private MesaDao mesaDao;
    private ReservaDao reservaDao;

    public MesaGestor(MesaDao mesaDao, ReservaDao reservaDao) {
        mesaDao = new MesaDao();
        reservaDao = new ReservaDao();
    }

    // print mostrar estado de las mesas
    public void mostrarEstadoMesa() throws SQLException {
        List<Mesa> mesas_estados = mesaDao.obtenerTodasLasMesas();

        for (Mesa mesa : mesas_estados) {
            String estado = mesa.getEstado();
            System.out.println("Mesa número: " + mesa.getNumero_mesa() + "\n Capacidad: " + mesa.getCapacidad()
                    + "\n Estado: " + estado + "Sala: " + mesa.getSala());
        }
    }

    // metodo asignar mesa
    public String asignarMesa(int id_cliente, Date diaReserva, LocalTime horaReserva, int numero_comensales,
            String descripcion) throws SQLException {
        Mesa mesa = mesaDao.obtenerMesasDisponibles(numero_comensales);

        if (mesa != null) {
            mesaDao.actualizarEstadoMesa(numero_comensales, "ocupada");

            Reserva reserva = new Reserva(0, id_cliente, diaReserva, horaReserva, numero_comensales, descripcion);
            reservaDao.insertarReserva(reserva, mesa.getNumero_mesa());

            return String.format("Se le ha asignado la mesa: " + mesa.getNumero_mesa() + "en Sala: " + mesa.getSala());
        }

        return String.format("Lo siento, no hay mesas disponibles para ese número de comensales");
    }


}
