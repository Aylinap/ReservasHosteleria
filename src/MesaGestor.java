import java.sql.SQLException;
import java.util.*;
import java.time.LocalTime;

public class MesaGestor {
    private ClienteDao clienteDao;
    private MesaDao mesaDao;
    private ReservaDao reservaDao;

    public MesaGestor(ClienteDao clienteDao, MesaDao mesaDao, ReservaDao reservaDao) {
        mesaDao = new MesaDao();
        reservaDao = new ReservaDao();
        clienteDao = new ClienteDao();

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
    public String asignarMesaYCrearReserva(int idCliente, Date diaReserva, LocalTime horaReserva, int numComensales,
            String descripcion) throws SQLException {
        Mesa mesa = mesaDao.obtenerMesasDisponibles(numComensales);

        if (mesa != null) {
            mesaDao.actualizarEstadoMesa(mesa.getNumero_mesa(), "ocupada");

            Reserva reserva = new Reserva(0, idCliente, diaReserva, horaReserva, numComensales, descripcion);
            reservaDao.insertaReservaNueva(reserva, mesa.getNumero_mesa());

            return String.format("Se ha creado una reserva en la mesa %d con capacidad para %d comensales.",
                    mesa.getNumero_mesa(), mesa.getCapacidad());
        } else {
            return "Lo siento, no hay mesas disponibles para ese número de comensales en este momento.";
        }
    }

}
