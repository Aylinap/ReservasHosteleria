
import java.sql.*;

public class ReservaDao {
    private static final String insertarReservaNueva = "insert into reserva (id_cliente, dia_reserva, hora_reserva, comensales, comentario) VALUES (?, ?, ?, ?, ?)";
    private static final String insertar_reserva_mesa = "insert into reservamesa (id_reserva, numero_mesa) values (?, ?)";
    private static final String mostrar_todas_reservas = "select reserva.*, cliente.nombre_cliente from reserva inner join cliente on reserva.id_cliente = cliente.id_cliente";
    private static final String mostrar_reserva_por_nombre = "select reserva.*, cliente.nombre_cliente from reserva inner join cliente on reserva.id_cliente = cliente.id_cliente where nombre_cliente = ?";
    private static final String eliminar_reserva = "Delete from reserva where id_reserva=?";
    private static final String modificar_reserva = "update reserva set dia_reserva=?, hora_reserva=?, comensales=?, comentario=?";
    private static final String modificar_reserva_mesa = " update reservamesa set numero_mesa=?";
    private static final String modificar_estado_mesa = "UPDATE mesas SET estado_mesa = 'disponible' WHERE numero_mesa IN (SELECT numero_mesa FROM reservamesa WHERE id_reserva = ?)";
    private static final String obtener_reserva_id = "select reserva.* from reserva where id_reserva = ?)";

    public int insertaReservaNueva(Reserva nuevaReserva, int numero_mesa) throws SQLException {
        // realizamos la conexion
        Connection c = Dao.openConnection();
        PreparedStatement pstmt = c.prepareStatement(insertarReservaNueva, Statement.RETURN_GENERATED_KEYS);

        // hacemos la verificacion del horario permitido, está en reserva el
        // arreglo/metodo.
        if (!nuevaReserva.esHoraPermitida()) {
            // si el horario no esta permitido, lanzamos un mensaje.
            System.out.println("Lo sentimos, no se puedes hacer reservas fuera del horario permitido.");
            return -1;
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

        // insertar en la tabla reserva_mesa
        insertarReservaMesa(id_reserva, numero_mesa);

        c.close();
        return id_reserva;
    }

    // metodo insertar tabla reserva mesa

    private void insertarReservaMesa(int id_reserva, int numero_mesa) throws SQLException {
        Connection c = Dao.openConnection();
        PreparedStatement pstmt = c.prepareStatement(insertar_reserva_mesa);
        pstmt.setInt(1, id_reserva);
        pstmt.setInt(2, numero_mesa);
        pstmt.executeUpdate();
        pstmt.close();
    }

    public void registrarCombinacionDeMesas(int id_reserva, int numero_mesa) throws SQLException {
        try (Connection c = Dao.openConnection()) {
            insertarReservaMesa(id_reserva, numero_mesa);
        }
    }

    // metodo mostrar todas las reservas

    public void obtenerTodasLasReservas() throws SQLException {
        Connection c = Dao.openConnection();
        PreparedStatement pstmt = c.prepareStatement(mostrar_todas_reservas);
        ResultSet rset = pstmt.executeQuery();

        System.out.println("-------------Reservas------------- ");
        while (rset.next()) {
            System.out.println(
                    "======================================" +
                            "\n ID de la reserva: " + rset.getInt("id_reserva")
                            + "\n ID cliente: " + rset.getString("id_cliente")
                            + "\n Nombre del cliente: " + rset.getString("nombre_cliente") +
                            "\n Día reserva: " + rset.getDate("dia_reserva") +
                            "\n Hora reserva:" + rset.getTime("hora_reserva")
                            + "\n Número de comensales: " + rset.getInt("comensales")
                            + "\n Comentario: " + rset.getString("comentario")
                            + "\n =====================================");

        }

        pstmt.close();
        c.close();
    }

    // metodo msotrar la reserva del mismo cliente si quiere ver la suya
    public void mostrarReservaPorNombre(String nombreCliente) throws SQLException {
        Connection c = Dao.openConnection();
        PreparedStatement pstmt = c.prepareStatement(mostrar_reserva_por_nombre);
        pstmt.setString(1, nombreCliente);
        ResultSet rset = pstmt.executeQuery();

        System.out.println("-----------Reserva-----------");

        while (rset.next()) {
            System.out.println(
                    "---------------------------" +
                            "\n Nombre del cliente: " + rset.getString("nombre_cliente")
                            + "\n Día de la reserva: " + rset.getDate("dia_reserva")
                            + "\n Hora de la reserva: " + rset.getTime("hora_reserva")
                            + "\n Número de comensales: " + rset.getString("comensales"));
            System.out.println("---------------------------");
        }

    }

    // borrado en cascada de la base de datos, deberia borrarse el id de reserva y
    // fila desde reservamesa, luego marcar la mesa como disponible.
    public boolean eliminarReservaNombre(int id_reserva) throws SQLException {
        Connection c = Dao.openConnection();
        PreparedStatement pstmtReserva = c.prepareStatement(eliminar_reserva);
        PreparedStatement pstmtMesa = c.prepareStatement(modificar_estado_mesa);
        boolean reservaEliminada = false;

        // elimina la reserva (el borrado en cascada eliminará cualquier fila
        // relacionada en reservamesa)
        pstmtReserva.setInt(1, id_reserva);
        int filaReserva = pstmtReserva.executeUpdate();

        // si se elimino la reserva se actualiza el estado de la mesa
        if (filaReserva > 0) {
            pstmtMesa.setInt(1, id_reserva);
            pstmtMesa.executeUpdate();
            reservaEliminada = true;
        }

        return reservaEliminada;
    }

    ////////////////////////// método modificar reserva ///////////////////////////

    // tendria que manejar el numero de comensales y asignar la mesa de nuevo si
    // quieren ser màs
    // pero no modifico el numero de reserva, solo la mesa si necesita más
    // comensales y los demas datos si necesita cambiarlos.
    // tendria que buscar por id de reserva, hacer la logica interna pero que la
    // persona busque por nombre

    public void modificarReserva(int id_reserva, Reserva reservaModificada, int numero_mesa) throws SQLException {
        Connection c = Dao.openConnection();
        PreparedStatement pstmt = c.prepareStatement(modificar_reserva);

        // aqui faltaria ver si le paso realmente el id_cliente creo que no deberia
        // pstmt.setInt(1, reservaModificada.getId_cliente());

        // parseo de fecha java para usarla en sql
        pstmt.setDate(2, new java.sql.Date(reservaModificada.getDiaReserva().getTime()));
        // parseo de hora para introducirla en la bbdd
        pstmt.setTime(3, Time.valueOf(reservaModificada.getHoraReserva()));
        pstmt.setInt(4, reservaModificada.getNumero_comensales());
        pstmt.setString(5, reservaModificada.getDescripcion());

        pstmt.executeUpdate();
        pstmt.close();
        c.close();

        // despues insertamos el numero de mesa en la tabla reservamesa y si es que
        // llega a cambiar

        c = Dao.openConnection();
        pstmt = c.prepareStatement(modificar_reserva_mesa);
        pstmt.setInt(1, id_reserva);
        pstmt.setInt(2, numero_mesa);

        pstmt.executeUpdate();
        pstmt.close();
        c.close();
    }

    // obtener reserva por id. -> verificar si el metodo me funciona bien

    public Reserva obtenerReservaPorId(int id_reserva) throws SQLException {
        Connection c = Dao.openConnection();
        PreparedStatement pstmt = c.prepareStatement(obtener_reserva_id);
        pstmt.setInt(1, id_reserva);
        ResultSet rset = pstmt.executeQuery();

        while (rset.next()) {
            int idreserva = rset.getInt("id_reserva");
        }
        return new Reserva(0, id_reserva, null, null, 0, "");
    }
}
