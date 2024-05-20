
import java.sql.*;

public class ReservaDao {
    private static final String insertarReservaNueva = "INSERT INTO reserva (id_cliente, dia_reserva, hora_reserva, comensales, comentario) VALUES (?, ?, ?, ?, ?)";
    private static final String insertar_reserva_mesa = "INSERT INTO reservamesa (id_reserva, numero_mesa) VALUES (?, ?)";

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

}

// // metodo insertar reserva nueva en tabla reserva y tabla reservanueva
// inserta
// // id_reserva e id_cliente.
// public void insertaReservaNueva(Reserva nuevaReserva, int numero_mesa) throws
// SQLException {
// Connection c = Dao.openConnection();
// PreparedStatement pstmt = c.prepareStatement(insertarReservaNueva);

// if (!nuevaReserva.esHoraPermitida()) {
// System.out.println("Lo sentimos, no se pueden hacer reservas fuera del
// horario permitido.");
// return;
// }
// // verificar si se obtiene el id_cliente desde el cliente que se creo en la
// base
// // de datos, recordar que es autoincrement en la bbdd
// pstmt.setInt(1, nuevaReserva.getId_cliente());
// pstmt.setDate(2, new java.sql.Date(nuevaReserva.getDiaReserva().getTime()));
// pstmt.setTime(3, Time.valueOf(nuevaReserva.getHoraReserva()));
// pstmt.setInt(4, nuevaReserva.getNumero_comensales());
// pstmt.setString(5, nuevaReserva.getDescripcion());

// System.out.println("Sentencia SQL para reserva nueva: " + pstmt.toString());
// System.out.println("Reserva creada correctamente.");
// System.out.println("ID Cliente: " + nuevaReserva.getId_cliente());
// System.out.println("Día Reserva: " + nuevaReserva.getDiaReserva());
// System.out.println("Hora Reserva: " + nuevaReserva.getHoraReserva());
// System.out.println("Número Comensales: " +
// nuevaReserva.getNumero_comensales());
// System.out.println("Descripción: " + nuevaReserva.getDescripcion());

// pstmt.executeUpdate();
// pstmt.close();
// c.close();
// // aqui tendria que cerrar la conexion y abrirla para inserta la info en
// // reservaMesa
// // donde iria el numero de la mesa y el id de la reserva
// // entonces aqui leo el id de la reserva que se creo,
// int id_reserva = leerIDReserva();

// // luego abro de nuevo la conexion e inserto esa informacion en la tabla de
// // reservaMesa
// // la tabla reservaMesa solo tiene id_reserva y id_mesa
// c = Dao.openConnection();
// pstmt = c.prepareStatement(insertar_reserva_mesa);
// pstmt.setInt(1, id_reserva);
// pstmt.setInt(2, numero_mesa);

// System.out.println("Sentencia SQL para reserva nueva: " + pstmt.toString());
// System.out.println("Reserva creada correctamente.");

// pstmt.executeUpdate();
// pstmt.close();
// c.close();

// }
// // public void insertarReserva(Reserva reservaNueva, Mesa mesa) throws
// // SQLException {

// // Connection c = Dao.openConnection();
// // PreparedStatement pstmt = c.prepareStatement(insertarReservaNueva);

// // if (!reservaNueva.esHoraPermitida()) {
// // System.out.println("Lo sentimos, no se pueden hacer reservas fuera del
// // horario permitido.");
// // return;
// // }
// // // de reserva tendria que borrar numero sala, numero sala.
// // pstmt.setString(1, reservaNueva.getNombre_cliente());
// // pstmt.setDate(2, new
// java.sql.Date(reservaNueva.getDiaReserva().getTime()));
// // pstmt.setTime(3, java.sql.Time.valueOf(reservaNueva.getHoraReserva()));
// // pstmt.setInt(4, reservaNueva.getNumero_comensales());
// // pstmt.setString(5, reservaNueva.getDescripcion());

// // System.out.println("Sentencia SQL para reserva nueva: " +
// pstmt.toString());
// // System.out.println("Reserva creada correctamente.");
// // pstmt.executeUpdate();
// // pstmt.close();
// // c.close();

// // // aqui podria leer el id de la reserva entrante y abrir conexion para
// // ingresar
// // // en la tabla nueva la informacion (Rserva_Mesa_Sala)
// // int id_reserva = leerIDReserva();
// // int numeroMesa = reservaNueva.getMesa().getNumero_mesa(); // Ejemplo de
// cómo
// // obtener el número de mesa desde el
// // // objeto Mesa asociado a la reserva
// // int sala = reservaNueva.getMesa().getSala().getNumero_sala();

// // String numeros_mesas_combinadas; // = getMesa().getLista_combinadas();
// // obtener el arreglo de las mesas
// // // combinadas o el metodo de obtener el la mesa elegida priori para tal
// // // comensal e insertarlo//

// // // logica para cambiar el estado de la mesa a reservada y luego ver como
// la
// // // cambio a ocupada y luego ver como se vuelven disponibles

// // c = Dao.openConnection();
// // pstmt = c.prepareStatement(insertar_reserva_mesa);
// // pstmt.setInt(1, id_reserva);
// // // numero de mesa tiene que tener logica para ser otorgados de forma
// // prioritaria
// // // dependiendo del nunmero de comensales, asi que la logica siguiente hay
// que
// // // adaptarla
// // pstmt.setInt(2, numeroMesa);
// // pstmt.setInt(3, sala);

// // System.out.println("Sentencia SQL para reserva-mesa: " +
// pstmt.toString());
// // pstmt.executeUpdate();
// // pstmt.close();
// // c.close();

// // }

// // metodo para leer la id_reserva entrante y meterla en la tabla
// ReservaMesaSala

// public int leerIDReserva() throws SQLException {
// Connection c = Dao.openConnection();
// PreparedStatement pstmt = c.prepareStatement(id_reserva);
// ResultSet rset = pstmt.executeQuery();

// int idReserva = -1;

// if (rset.next()) {
// idReserva = rset.getInt("id_reserva");
// }

// rset.close();
// pstmt.close();
// c.close();

// return idReserva;
// }

// // metodo obtener el id del cliente pero tendria que hacer la insercion de
// datos
// // del cliente que no es muy importante tampoco

// // mostrar todas las reservas

// public void mostrarTodasReservas() throws SQLException {
// Connection c = Dao.openConnection();
// PreparedStatement pstmt = c.prepareStatement(mostrar_todas_reservas);
// ResultSet rset = pstmt.executeQuery();

// while (rset.next()) {
// System.out.println(
// "----- Todas las reservas ----- \n" +
// "\n Nombre Cliente: " + rset.getString("nombre_cliente")
// + "\n Id de la reserva: " + rset.getInt("id_reserva")
// + "\n Fecha y hora de la reserva: " + rset.getInt("dia_reserva") + " a las: "
// + rset.getString("horaReserva") + "horas.");
// }

// pstmt.close();
// c.close();
// }

// // public void mostrarTodasReservas() throws SQLException {
// // try (Connection c = getConnection();
// // PreparedStatement pstmt = c.prepareStatement(mostrar_todas_reservas);
// // ResultSet rset = pstmt.executeQuery()) {

// // System.out.println("----- Todas las reservas -----");

// // while (rset.next()) {
// // String nombreCliente = rset.getString("nombre_cliente");
// // int idReserva = rset.getInt("id_reserva");
// // Date diaReserva = rset.getDate("dia_reserva");
// // Time horaReserva = rset.getTime("hora_reserva");

// // System.out.printf(
// // "\nNombre Cliente: %s" +
// // "\nId de la Reserva: %d" +
// // "\nFecha de la Reserva: %s" +
// // "\nHora de la Reserva: %s\n",
// // nombreCliente, idReserva, diaReserva.toString(), horaReserva.toString());
// // }
// // }

// // }

// }
