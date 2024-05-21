
import java.sql.*;

public class ReservaDao {
    private static final String insertarReservaNueva = "insert into reserva (id_cliente, dia_reserva, hora_reserva, comensales, comentario) VALUES (?, ?, ?, ?, ?)";
    private static final String insertar_reserva_mesa = "insert into reservamesa (id_reserva, numero_mesa) values (?, ?)";

    public void insertaReservaNueva(Reserva nuevaReserva, int numero_mesa) throws SQLException {
        // realizamos la conexion
        Connection c = Dao.openConnection();
        PreparedStatement pstmt = c.prepareStatement(insertarReservaNueva, Statement.RETURN_GENERATED_KEYS);

        // hacemos la verificacion del horario permitido, está en reserva el
        // arreglo/metodo.
        if (!nuevaReserva.esHoraPermitida()) {
            // si el horario no esta permitido, lanzamos un mensaje.
            System.out.println("Lo sentimos, no se puedes hacer reservas fuera del horario permitido.");
            return;
        }
        // creamos la insercion de la reserva :
        pstmt.setInt(1, nuevaReserva.getId_cliente());
        // parseo de fecha java para usarla en sql
        pstmt.setDate(2, new java.sql.Date(nuevaReserva.getDiaReserva().getTime()));
        // parseo de hora para introducirla en la bbdd
        pstmt.setTime(3, Time.valueOf(nuevaReserva.getHoraReserva()));
        pstmt.setInt(4, nuevaReserva.getNumero_comensales());
        pstmt.setString(5, nuevaReserva.getDescripcion());

        // desde aqui leemos las claves generadas cuando se hace la insercion de la
        // reserva
        // en este caso es la primera fila afectada, se lee el id de la resera
        // para usar ese id insertada en la tabla de mesaReserva
        int filafectada = pstmt.executeUpdate();

        if (filafectada == 0) {
            // creamos una exception por si no toma la clave generada
            throw new SQLException(
                    "El insert de la reserva falló, no se modificaron filas(no se creó la reserva).");
        }
        // https://www.ibm.com/docs/es/db2-for-zos/13?topic=applications-retrieving-auto-generated-keys-insert-statement
        ResultSet generatedKeys = pstmt.getGeneratedKeys();
        // inicializamos el id_reserva en 0
        int id_reserva = 0;
        // si la clave fue generada, guardamos la clave en id_reserva
        if (generatedKeys.next()) {
            id_reserva = generatedKeys.getInt(1);
        } else {
            // sino lanzamos una exception que nos ayuda a revisar que no se pudo obtener
            throw new SQLException("El insert de la reserva no se pudo realizar porque no se generaró el ID");
        }
        // cerramos conexion de pstmt -> prepareStatement
        pstmt.close();

        // insertar informacion en la tabla reservaMesa
        pstmt = c.prepareStatement(insertar_reserva_mesa); // la consulta sql
        pstmt.setInt(1, id_reserva); // se le pasa el id generado en reserva mesa
        pstmt.setInt(2, numero_mesa); // se le pasa el numero de mesa asignada(se pasa como parametro)
        // ejecutar update y cerrar conexion
        pstmt.executeUpdate();
        pstmt.close();
        c.close();
    }

}
