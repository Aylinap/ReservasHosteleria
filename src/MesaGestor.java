import java.sql.SQLException;
import java.util.*;
import java.time.LocalTime;

public class MesaGestor {
    private ClienteDao clienteDao;
    private MesaDao mesaDao;
    private ReservaDao reservaDao;

    public MesaGestor(MesaDao mesaDao, ClienteDao clienteDao) {
        this.mesaDao = new MesaDao();
        this.reservaDao = new ReservaDao();
        this.clienteDao = new ClienteDao();
    }

    // mostrar estado de las mesas, desde la tabla mesa
    public void mostrarEstadoMesa() throws SQLException {
        List<Mesa> mesas_estados = mesaDao.obtenerTodasLasMesas();

        for (Mesa mesa : mesas_estados) {
            String estado = mesa.getEstado();
            System.out.println("Mesa número: " + mesa.getNumero_mesa() + "\n Capacidad: " + mesa.getCapacidad()
                    + "\n Estado: " + estado + "Sala: " + mesa.getSala());
        }
    }

    // crear una reservay mesa asignada, se le pasa como parametros los datos de
    // RESERVA
    public ReservaAsignada asignarMesaYCrearReserva(int idCliente, java.sql.Date diaReserva, LocalTime horaReserva,
            int numComensales, String descripcion) throws SQLException {
        Mesa mesa = mesaDao.obtenerMesaDisponibles(numComensales);

        if (mesa != null && mesa.getCapacidad() >= numComensales) {
            mesaDao.actualizarEstadoMesa(mesa.getNumero_mesa(), "ocupada");

            Reserva reserva = new Reserva(mesa.getNumero_mesa(), idCliente, diaReserva, horaReserva, numComensales,
                    descripcion);
            return new ReservaAsignada(reserva, mesa.getNumero_mesa());
        } else {
            return null;
        }
    }
}
