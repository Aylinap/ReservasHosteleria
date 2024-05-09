import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ReservaDao {

    private static final String insertarReservaNueva = "INSERT INTO reserva (nombre_cliente, numero_mesa, numero_sala, dia_reserva, horaReserva, comensales) VALUES (?, ?, ?, ?, ?,?)";
    private static final String mostrar_todas_reservas = "selecto * from reserva";

    public void insertarReserva(Reserva reservaNueva) throws SQLException {

        Connection c = Dao.openConnection();
        PreparedStatement pstmt = c.prepareStatement(insertarReservaNueva);

        if (!reservaNueva.esHoraPermitida()) {
            System.out.println("Lo sentimos, no se pueden hacer reservas fuera del horario permitido.");
            return;
        }
        pstmt.setString(1, reservaNueva.getNombre_cliente());
        pstmt.setInt(2, reservaNueva.getNumero_mesa());
        pstmt.setInt(3, reservaNueva.getSala());
        pstmt.setDate(4, new java.sql.Date(reservaNueva.getDiaReserva().getTime()));
        pstmt.setTime(5, java.sql.Time.valueOf(reservaNueva.getHoraReserva()));
        pstmt.setInt(6, reservaNueva.getNumero_comensales());

        pstmt.executeUpdate();
        System.out.println("Sentencia SQL para productos: " + pstmt.toString());
        System.out.println("Reserva creada correctamente.");

        pstmt.close();
        c.close();
    }
}
