
import java.sql.*;

public class ReservaDao {
    private static final String insertarReservaNueva = "insert into reserva (id_cliente, dia_reserva, hora_reserva, comensales, comentario) VALUES (?, ?, ?, ?, ?)";
    private static final String insertar_reserva_mesa = "insert into reservamesa (id_reserva, numero_mesa) values (?, ?)";

    public void insertaReservaNueva(Reserva nuevaReserva, int numero_mesa) throws SQLException {
        Connection c = Dao.openConnection();
        PreparedStatement pstmt = c.prepareStatement(insertarReservaNueva, Statement.RETURN_GENERATED_KEYS);

        if (!nuevaReserva.esHoraPermitida()) {
            System.out.println("Lo sentimos, no se pueden hacer reservas fuera del horario permitido.");
            return;
        }

        pstmt.setInt(1, nuevaReserva.getId_cliente());
        pstmt.setDate(2, new java.sql.Date(nuevaReserva.getDiaReserva().getTime()));
        pstmt.setTime(3, Time.valueOf(nuevaReserva.getHoraReserva()));
        pstmt.setInt(4, nuevaReserva.getNumero_comensales());
        pstmt.setString(5, nuevaReserva.getDescripcion());

        int rowsAffected = pstmt.executeUpdate();

        if (rowsAffected == 0) {
            throw new SQLException("La inserción de la reserva falló, no se modificaron filas.");
        }

        ResultSet generatedKeys = pstmt.getGeneratedKeys();
        int id_reserva = 0;
        if (generatedKeys.next()) {
            id_reserva = generatedKeys.getInt(1);
        } else {
            throw new SQLException("La inserción de la reserva falló, no se generaron claves.");
        }

        pstmt.close();

        // Insertar en reservaMesa
        pstmt = c.prepareStatement(insertar_reserva_mesa);
        pstmt.setInt(1, id_reserva);
        pstmt.setInt(2, numero_mesa);
        pstmt.executeUpdate();

        pstmt.close();
        c.close();
    }

    // metodo buscar reserva por nombre

}
