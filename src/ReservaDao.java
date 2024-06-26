
import java.sql.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class ReservaDao {
    private static final String insertarReservaNueva = "insert into reserva (id_cliente, dia_reserva, hora_reserva, comensales, comentario) VALUES (?, ?, ?, ?, ?)";
    private static final String insertar_reserva_mesa = "insert into reservamesa (id_reserva, numero_mesa) values (?, ?)";
    private static final String mostrar_todas_reservas = "select reserva.*, cliente.nombre_cliente from reserva inner join cliente on reserva.id_cliente = cliente.id_cliente";
    private static final String mostrar_reserva_por_nombre = "select reserva.*, cliente.nombre_cliente from reserva inner join cliente on reserva.id_cliente = cliente.id_cliente where nombre_cliente = ?";
    private static final String eliminar_reserva = "Delete from reserva where id_reserva=?";
    private static final String modificar_reserva = "UPDATE reserva SET dia_reserva = ?, hora_reserva = ?, comensales = ?, comentario = ? WHERE id_reserva = ?";
    private static final String modificar_reserva_mesa = " update reservamesa set numero_mesa=?";
    private static final String modificar_estado_mesa = "update mesa set estado_mesa = 'disponible' where numero_mesa IN (select numero_mesa from reservamesa where id_reserva = ?)";
    private static final String obtener_reserva_id = "select reserva.* from reserva where id_reserva = ?";
    private static final String ultimo_id_reserva = "select MAX(id_reserva) AS id_reserva from reserva";
    private static final String mostrar_reservas_por_nombre = "select reserva.id_reserva, cliente.id_cliente AS cliente_id, cliente.nombre_cliente, "
            +
            "reserva.dia_reserva, reserva.hora_reserva, reserva.comensales, reserva.comentario " +
            "from reserva " +
            "INNER JOIN cliente ON reserva.id_cliente = cliente.id_cliente " +
            "where cliente.nombre_cliente = ?";

    private static final String obtener_mesas_por_reserva = "select numero_mesa FROM reservamesa WHERE id_reserva = ?";
    private static final String eliminar_reserva_por_id = "delete FROM reserva where id_reserva = ?";
    private static final String marcar_mesa_disponible = "update mesa SET estado_mesa = 'disponible' where numero_mesa = ?";
    private static final String obtener_reservas_hoy = "select * from reserva where dia_reserva = ? order by hora_reserva";

    // metodo insertar tabla reserva mesa
    public int insertaReservaNueva(Reserva nuevaReserva, List<Integer> numerosMesa) throws SQLException {
        Connection c = Dao.openConnection();
        PreparedStatement pstmt = c.prepareStatement(insertarReservaNueva, Statement.RETURN_GENERATED_KEYS);

        if (!nuevaReserva.esHoraPermitida()) {
            System.out.println("Lo sentimos, no se pueden hacer reservas fuera del horario permitido.");
            return -1;
        }

        pstmt.setInt(1, nuevaReserva.getId_cliente());
        pstmt.setDate(2, new java.sql.Date(nuevaReserva.getDiaReserva().getTime()));
        pstmt.setTime(3, Time.valueOf(nuevaReserva.getHoraReserva()));
        pstmt.setInt(4, nuevaReserva.getNumero_comensales());
        pstmt.setString(5, nuevaReserva.getDescripcion());

        int filafectada = pstmt.executeUpdate();

        if (filafectada == 0) {
            // son print para saber donde falla
            throw new SQLException("El insert de la reserva falló, no se modificaron filas (no se creó la reserva).");
        }

        ResultSet generatedKeys = pstmt.getGeneratedKeys();
        int id_reserva = 0;
        if (generatedKeys.next()) {
            id_reserva = generatedKeys.getInt(1);
        } else {
            throw new SQLException("El insert de la reserva no se pudo realizar (porque no se generó el ID)");
        }
        pstmt.close();

        PreparedStatement mesaStmt = c.prepareStatement(insertar_reserva_mesa);

        for (int numero_mesa : numerosMesa) {
            mesaStmt.setInt(1, id_reserva);
            mesaStmt.setInt(2, numero_mesa);
            mesaStmt.addBatch();
        }

        mesaStmt.executeBatch();
        mesaStmt.close();

        c.close();
        return id_reserva;
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

    public List<Reserva> obtenerReservasPorNombre(String nombreCliente) throws SQLException {
        List<Reserva> reservas = new ArrayList<>();

        try {
            Connection c = Dao.openConnection();
            PreparedStatement pstmt = c.prepareStatement(mostrar_reservas_por_nombre);
            pstmt.setString(1, nombreCliente);
            ResultSet rset = pstmt.executeQuery();

            while (rset.next()) {
                int idReserva = rset.getInt("id_reserva");
                int idCliente = rset.getInt("cliente_id");
                Date diaReserva = rset.getDate("dia_reserva");
                LocalTime horaReserva = rset.getTime("hora_reserva").toLocalTime();
                int comensales = rset.getInt("comensales");
                String comentario = rset.getString("comentario");

                reservas.add(new Reserva(idReserva, idCliente, diaReserva, horaReserva, comensales, comentario));
            }
            pstmt.close();
            c.close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Error al obtener las reservas para el cliente: " + nombreCliente);
        }

        return reservas;
    }

    // borrado en cascada de la base de datos, deberia borrarse el id de reserva y
    // fila desde reservamesa, luego marcar la mesa como disponible.(no se está
    // usando)
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

    public void eliminarReservaPorId(int idReserva) throws SQLException {
        List<Integer> mesasReservadas = new ArrayList<>();
        try {
            Connection c = Dao.openConnection();

            // retorna las reservas/mesas que tengan el id asociado.
            PreparedStatement pstmt = c.prepareStatement(obtener_mesas_por_reserva);
            pstmt.setInt(1, idReserva);
            ResultSet rset = pstmt.executeQuery();

            while (rset.next()) {
                mesasReservadas.add(rset.getInt("numero_mesa"));
            }

            // elimina la reserva
            pstmt = c.prepareStatement(eliminar_reserva_por_id);
            pstmt.setInt(1, idReserva);
            pstmt.executeUpdate();

            // vuelve a cambiar las mesas a disponibles
            pstmt = c.prepareStatement(marcar_mesa_disponible);
            for (int numeroMesa : mesasReservadas) {
                pstmt.setInt(1, numeroMesa);
                pstmt.executeUpdate();
            }
            pstmt.close();
            c.close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Error al eliminar la reserva con ID: " + idReserva);
        }
    }

    // modificar una reserva, no lo he probado ni lo quiero tocar.

    public void modificarReserva(int id_reserva, Reserva reservaModificada, List<Integer> numerosMesa)
            throws SQLException {
        Connection c = Dao.openConnection();

        try {

            PreparedStatement pstmtReserva = c.prepareStatement(modificar_reserva);
            pstmtReserva.setDate(1, new java.sql.Date(reservaModificada.getDiaReserva().getTime()));
            pstmtReserva.setTime(2, Time.valueOf(reservaModificada.getHoraReserva()));
            pstmtReserva.setInt(3, reservaModificada.getNumero_comensales());
            pstmtReserva.setString(4, reservaModificada.getDescripcion());
            pstmtReserva.setInt(5, id_reserva);

            pstmtReserva.executeUpdate();

            PreparedStatement pstmtMarcarMesaDisponible = c.prepareStatement(modificar_estado_mesa);
            pstmtMarcarMesaDisponible.setInt(1, id_reserva);
            pstmtMarcarMesaDisponible.executeUpdate();

            PreparedStatement pstmtReservaMesa = c.prepareStatement("DELETE FROM reservamesa WHERE id_reserva = ?");
            pstmtReservaMesa.setInt(1, id_reserva);
            pstmtReservaMesa.executeUpdate();

            PreparedStatement pstmtInsertarReservaMesa = c.prepareStatement(insertar_reserva_mesa);
            for (int numero_mesa : numerosMesa) {
                pstmtInsertarReservaMesa.setInt(1, id_reserva);
                pstmtInsertarReservaMesa.setInt(2, numero_mesa);
                pstmtInsertarReservaMesa.addBatch();
            }
            pstmtInsertarReservaMesa.executeBatch();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Error al modificar la reserva con ID: " + id_reserva);
        }
    }

    // obtener reserva por id. -> (no lo estoy usando(creo))

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

    // lee el ultimo id insertado en reserva
    public int obtenerUltimoIdReserva() throws SQLException {
        int idReserva = -1;
        Connection c = Dao.openConnection();
        PreparedStatement pstmt = c.prepareStatement(ultimo_id_reserva);
        ResultSet rset = pstmt.executeQuery();

        if (rset.next()) {
            idReserva = rset.getInt("id_reserva");
        }

        pstmt.close();
        c.close();
        return idReserva;
    }

    // mostrar reservas por dia

    public List<Reserva> obtenerReservasHoyPorHorario(Date fechaActual) throws SQLException {

        try {
            Connection c = Dao.openConnection();
            PreparedStatement pstmt = c.prepareStatement(obtener_reservas_hoy);
            pstmt.setDate(1, fechaActual);
            ResultSet rset = pstmt.executeQuery();

            List<Reserva> reservas = new ArrayList<>();

            while (rset.next()) {
                int idReserva = rset.getInt("id_reserva");
                int idCliente = rset.getInt("id_cliente");
                Date diaReserva = rset.getDate("dia_reserva");
                Time time = rset.getTime("hora_reserva");
                LocalTime horaReserva = time.toLocalTime();
                int comensales = rset.getInt("comensales");
                String descripcion = rset.getString("comentario");

                Reserva reserva = new Reserva(idReserva, idCliente, diaReserva, horaReserva, comensales, descripcion);
                reservas.add(reserva);
            }

            return reservas;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Error al obtener las reservas de hoy por horario.");
        }
    }

}
