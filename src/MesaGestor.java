import java.sql.Connection;
import java.sql.PreparedStatement;
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
            System.out.println("\n Mesa número : " + mesa.getNumero_mesa() + "\n Capacidad : " + mesa.getCapacidad()
                    + "\n Estado : " + estado + "\n Sala : " + mesa.getSala());
        }
    }

    // crear una reserva y mesa asignada, se le pasa como parametros los datos de
    // RESERVA
    public ReservaAsignada asignarMesaYCrearReserva(int idCliente, java.sql.Date diaReserva, LocalTime horaReserva,
            int numComensales, String descripcion) throws SQLException {
        Mesa mesa = mesaDao.obtenerMesaDisponibles(numComensales);

        if (mesa != null && mesa.getCapacidad() >= numComensales) {
            mesaDao.actualizarEstadoMesa(mesa.getNumero_mesa(), "ocupada");

            // creo una instancia de reserva
            Reserva reserva = new Reserva(mesa.getNumero_mesa(), idCliente, diaReserva, horaReserva, numComensales,
                    descripcion);
            // y con eso creo una instancia de reservaMesa (el constructor recibe objeto de
            // reserva)
            return new ReservaAsignada(reserva, mesa.getNumero_mesa());
        } else {
            return null;
        }
    }

    // tengo un metodo en mesa que actualiza el estado en la bbdd, se puede usar ese

    // Metodo para eliminar una reserva y liberar la mesa asignada
    public boolean eliminarReserva(int idReserva) throws SQLException {
        // Primero, obtén la reserva y la mesa asociada
        Reserva reserva = reservaDao.obtenerReservaPorId(idReserva);
        // pero como sabe que la reserva es por id, tendria
        // que ser por nombre pero el nombre no es único,
        // sino tendria que ser unico pero es complicao,
        // tendria que ser nombre y fecha y hora, consultar
        // esos dos cosas mas desde la bd y asi recien
        // borrar que coincida todo, o listar y que la
        // persona confirme que es realmente esa y pasarle
        // el id internamente y borrar(tambien es otra
        // opcion)
        
        mesaDao.obtenerTodasLasMesas();
        Mesa mesa = new Mesa(idReserva);
        if (reserva != null) {
            int numeroMesa = mesa.getNumero_mesa();

            // elimina la reserva, le paso el id de la reserva y al borra
            boolean reservaEliminada = reservaDao.eliminarReserva(idReserva); // mirar que hacer con el nomnbre o el id,
                                                                              // sacar el id y que ingrese el id manual?

            // si la reserva se elimino correctamente, actualiza el estado de la mesa a
            // "disponible"
            if (reservaEliminada) {
                mesaDao.actualizarEstadoMesa(numeroMesa, "disponible");
                return true;
            } else {
                // si la reserva no se pudo eliminar, maneja el error como consideres necesario
                return false;
            }
        } else {
            // si no se encuentra la reserva, maneja el caso como consideres necesario
            return false;
        }
    }

}
