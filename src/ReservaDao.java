import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;

public class ReservaDao {

    private static final String insertarReservaNueva = "INSERT INTO reserva (nombre_cliente, dia_reserva, horaReserva, comensales, comentario) VALUES (?, ?, ?, ?, ?,?,?)";
    private static final String mostrar_todas_reservas = "selecto * from reserva";
    // para leer el ultimo id de la reserva que se creó
    private static final String id_reserva = "Select id_reserva from reserva ORDER BY id_reserva DESC LIMIT 1";
    private static final String insertar_reserva_mesa_sala = "insert into reservaMesaSala (id_reserva, numero_mesa, numero_sala)";

    public void insertarReserva(Reserva reservaNueva) throws SQLException {

        Connection c = Dao.openConnection();
        PreparedStatement pstmt = c.prepareStatement(insertarReservaNueva);

        if (!reservaNueva.esHoraPermitida()) {
            System.out.println("Lo sentimos, no se pueden hacer reservas fuera del horario permitido.");
            return;
        }
        // de reserva tendria que borrar numero sala, numero sala.
        pstmt.setString(1, reservaNueva.getNombre_cliente());
        pstmt.setDate(2, new java.sql.Date(reservaNueva.getDiaReserva().getTime()));
        pstmt.setTime(3, java.sql.Time.valueOf(reservaNueva.getHoraReserva()));
        pstmt.setInt(4, reservaNueva.getNumero_comensales());
        pstmt.setString(5, reservaNueva.getDescripcion());

        System.out.println("Sentencia SQL para reserva nueva: " + pstmt.toString());
        System.out.println("Reserva creada correctamente.");
        pstmt.executeUpdate();
        pstmt.close();
        c.close();

        // aqui podria leer el id de la reserva entrante y abrir conexion para ingresar
        // en la tabla nueva la informacion (Rserva_Mesa_Sala)
        int id_reserva = leerIDReserva();
        int numeroMesa = reservaNueva.getMesa().getNumero_mesa(); // Ejemplo de cómo obtener el número de mesa desde el
                                                                  // objeto Mesa asociado a la reserva
        int sala = reservaNueva.getMesa().getSala().getNumero_sala();

        String numeros_mesas_combinadas; // = getMesa().getLista_combinadas(); obtener el arreglo de las mesas
                                         // combinadas o el metodo de obtener el la mesa elegida priori para tal
                                         // comensal e insertarlo//

        // logica para cambiar el estado de la mesa a reservada y luego ver como la
        // cambio a ocupada y luego ver como se vuelven disponibles
        if (reservaNueva.getMesa().isDisponible() == true) {

        }

        c = Dao.openConnection();
        pstmt = c.prepareStatement(insertar_reserva_mesa_sala);
        pstmt.setInt(1, id_reserva);
        // numero de mesa tiene que tener logica para ser otorgados de forma prioritaria
        // dependiendo del nunmero de comensales, asi que la logica siguiente hay que
        // adaptarla
        pstmt.setInt(2, numeroMesa);
        pstmt.setInt(3, sala);

        System.out.println("Sentencia SQL para reserva-mesa: " + pstmt.toString());
        pstmt.executeUpdate();
        pstmt.close();
        c.close();

    }

    // metodo para leer la id_reserva entrante y meterla en la tabla ReservaMesaSala

    public int leerIDReserva() throws SQLException {
        Connection c = Dao.openConnection();
        PreparedStatement pstmt = c.prepareStatement(id_reserva);
        ResultSet rset = pstmt.executeQuery();

        int idReserva = -1;

        if (rset.next()) {
            idReserva = rset.getInt("id_reserva");
        }

        rset.close();
        pstmt.close();
        c.close();

        return idReserva;
    }

    // mostrar todas las reservas

    public void mostrarTodasReservas() throws SQLException {
        Connection c = Dao.openConnection();
        PreparedStatement pstmt = c.prepareStatement(mostrar_todas_reservas);
        ResultSet rset = pstmt.executeQuery();

        while (rset.next()) {
            System.out.println(
                    "----- Todas las reservas ----- \n" +
                            "\n Nombre Cliente: " + rset.getString("nombre_cliente")
                            + "\n Id de la reserva: " + rset.getInt("id_reserva")
                            + "\n Fecha y hora de la reserva: " + rset.getInt("dia_reserva") + "  a las:  "
                            + rset.getString("horaReserva") + "horas.");
        }

        pstmt.close();
        c.close();
    }

}
