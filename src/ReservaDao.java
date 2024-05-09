import java.sql.SQLException;

public class ReservaDao {

    public void insertarReserva(Reserva reservaNueva) throws SQLException {

        if (!reservaNueva.esHoraPermitida()) {
            System.out.println("Lo sentimos, no se pueden hacer reservas fuera del horario permitido.");
            return;
        }
        

    }
}
