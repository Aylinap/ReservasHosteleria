import java.sql.*;
import java.time.LocalTime;
import java.util.*;

public class MesaDao {
    private static final String obtener_mesas_disponible = "select * from mesa where estado_mesa = 'disponible'";
    private static final String obtener_mesas = "select * from mesa";
    private static final String obtener_mesa_disponible_capacidad = "select * from mesa where capacidad >= ? and estado_mesa = 'disponible' order by capacidad";
    private static final String actualizar_estado_mesa = "update mesa set estado_mesa = ? where numero_mesa = ?";
    private static final String obtener_mesas_combinables = "select combinable_con from combinaciones where numero_mesa = ?";
    private static final String obtener_mesas_disponibles = "select * from mesa where estado_mesa = 'disponible'";
    private static final String obtener_mesa_numero = "select * from mesa where numero_mesa = ?";
    private static final String obtener_mesas_ocupadas_horario = "select mesa.*, reserva.*"
            +
            "from mesa " +
            "inner join reservamesa ON mesa.numero_mesa = reservamesa.numero_mesa " +
            "inner join reserva ON reservamesa.id_reserva = reserva.id_reserva " +
            "where mesa.estado_mesa = 'ocupada'";
    private static final String marcar_todas_las_mesas_disponibles = "update mesa set estado_mesa = 'disponible'";

    // devuelvo un arreglo de mesas, muestra el estado de todas las mesas.
    // List contiene el ArrayList
    public List<Mesa> obtenerTodasLasMesas() throws SQLException {
        List<Mesa> mesas = new ArrayList<>();
        Connection c = Dao.openConnection();
        PreparedStatement pstmt = c.prepareStatement(obtener_mesas);
        ResultSet rset = pstmt.executeQuery();

        // mientras lee la info de la bd la mete en variables y objeto Mesa que va
        // encontrando, lo mete a la lista de mesas.
        while (rset.next()) {
            int id = rset.getInt("numero_mesa");
            int capacidad = rset.getInt("capacidad");
            String estado = rset.getString("estado_mesa");
            int sala = rset.getInt("sala");
            String prioritaria = rset.getString("prioritaria");
            mesas.add(new Mesa(id, capacidad, estado, sala, prioritaria));
        }

        rset.close();
        pstmt.close();
        c.close();
        return mesas;
    }

    // Retorno una mesa con estado 'disponible', pasandole el numero de comensales.
    // asi busca una mesa que tenga <= numeros de comensales y que sea disponible.
    public Mesa obtenerMesaDisponibles(int numComensales) throws SQLException {
        // abro conexion
        Connection c = Dao.openConnection();
        PreparedStatement pstmt = c.prepareStatement(obtener_mesa_disponible_capacidad);

        pstmt.setInt(1, numComensales);
        ResultSet rset = pstmt.executeQuery();

        // si existe busco y obtengo la info la guardo en variables para pasarlas al
        // constructor de mesa.
        if (rset.next()) {
            int id = rset.getInt("numero_mesa");
            int capacidad = rset.getInt("capacidad");
            int sala = rset.getInt("sala");
            String prioritaria = rset.getString("prioritaria");
            // cierro la conexion
            rset.close();
            pstmt.close();
            c.close();
            // retorno un objeto mesa con la info obtenida
            return new Mesa(id, capacidad, "disponible", sala, prioritaria);
        }

        rset.close();
        pstmt.close();
        c.close();
        return null;
    }

    // metodo obtener todas las mesas disponibles sin pasarle la cantidad de
    // comensales:
    public void mostrarTodasLasMesasDisponibles() throws SQLException {
        Connection c = Dao.openConnection();
        PreparedStatement pstmt = c.prepareStatement(obtener_mesas_disponibles);
        ResultSet rset = pstmt.executeQuery();

        System.out.println("-------------Mesas Disponibles------------- ");
        while (rset.next()) {
            System.out.println(
                    "=====================================" +
                            "\n ID de la mesa: " + rset.getInt("numero_mesa") +
                            "\n Capacidad: " + rset.getInt("capacidad") +
                            "\n Sala: " + rset.getInt("sala") +
                            "\n Estado: " + rset.getString("estado_mesa") +
                            "\n =====================================");
        }

        pstmt.close();
        c.close();
    }

    // mostrar mesas ocupadas (listar)
    public void mostrarTodasLasMesasOcupadas() throws SQLException {
        Connection c = Dao.openConnection();
        PreparedStatement pstmt = c.prepareStatement(obtener_mesas_ocupadas_horario);
        ResultSet rset = pstmt.executeQuery();

        System.out.println("-------------Mesas Ocupadas------------- ");
        while (rset.next()) {
            System.out.println(
                    "=====================================" +
                            "\n ID de la mesa: " + rset.getInt("numero_mesa") +
                            "\n Capacidad: " + rset.getInt("capacidad") +
                            "\n Horario ocupada: " + rset.getTime("hora_reserva") +
                            "\n Día reserva ocupada: " + rset.getDate("dia_reserva") +
                            "\n Sala: " + rset.getInt("sala") +
                            "\n Estado: " + rset.getString("estado_mesa") +
                            "\n =====================================");
        }

        pstmt.close();
        c.close();
    }

    // retorna una lista de todas las mesas disponibles en la bbdd
    public List<Mesa> obtenerMesasDisponibles(int numComensales) throws SQLException {
        List<Mesa> mesasDisponibles = new ArrayList<>();
        Connection c = Dao.openConnection();
        PreparedStatement pstmt = c.prepareStatement(obtener_mesa_disponible_capacidad);
        pstmt.setInt(1, numComensales);
        ResultSet rset = pstmt.executeQuery();
        // print para ver si estaba siendo bien la consulta.
        System.out.println(" Consulta SQL: " + obtener_mesa_disponible_capacidad);
        System.out.println("Número de comensales: " + numComensales);

        while (rset.next()) {
            int id = rset.getInt("numero_mesa");
            int capacidad = rset.getInt("capacidad");
            String estado = rset.getString("estado_mesa");
            int sala = rset.getInt("sala");
            String prioritaria = rset.getString("prioritaria");
            mesasDisponibles.add(new Mesa(id, capacidad, estado, sala, prioritaria));
        }
        //
        if (mesasDisponibles.isEmpty()) {
            System.out.println("No se encontraron mesas disponibles para " + numComensales + " comensales.");
        } else {
            System.out.println("Mesas disponibles para " + numComensales + " comensales:");
            for (Mesa mesa : mesasDisponibles) {
                System.out.println("Mesa " + mesa.getNumero_mesa() + " - Capacidad: " + mesa.getCapacidad());
            }
        }

        pstmt.close();
        c.close();

        return mesasDisponibles;
    }

    // obtenermesas por numero
    public Mesa obtenerMesaPorNumero(int numeroMesa) throws SQLException {
        Connection c = Dao.openConnection();
        PreparedStatement pstmt = c.prepareStatement(obtener_mesa_numero);
        pstmt.setInt(1, numeroMesa);
        ResultSet rset = pstmt.executeQuery();

        Mesa mesa = null;
        if (rset.next()) {
            int id = rset.getInt("numero_mesa");
            int capacidad = rset.getInt("capacidad");
            String estado = rset.getString("estado_mesa");
            int sala = rset.getInt("sala");
            String prioritaria = rset.getString("prioritaria");
            mesa = new Mesa(id, capacidad, estado, sala, prioritaria);
        }

        rset.close();
        pstmt.close();
        c.close();
        return mesa;
    }

    // obtiene todas las mesas combinables de la tabla combinaciones, pasandole el
    // numero de mesa.
    public List<Integer> obtenerMesasCombinables(int numeroMesa) throws SQLException {
        List<Integer> mesasCombinables = new ArrayList<>();
        Connection c = Dao.openConnection();
        PreparedStatement pstmt = c.prepareStatement(obtener_mesas_combinables);
        pstmt.setInt(1, numeroMesa);
        ResultSet rset = pstmt.executeQuery();
        while (rset.next()) {
            mesasCombinables.add(rset.getInt("combinable_con"));
        }
        pstmt.close();
        c.close();
        return mesasCombinables;
    }

    // metodo mesas disponibles sin pasarle numero comensales como parametro, al
    // final la consulta sql era la que me daba problemas.
    public List<Mesa> obtenerMesasDisponiblesSinComensales() throws SQLException {
        List<Mesa> mesasDisponibles = new ArrayList<>();
        Connection c = Dao.openConnection();
        PreparedStatement pstmt = c.prepareStatement(obtener_mesas_disponible);
        ResultSet rset = pstmt.executeQuery();

        while (rset.next()) {
            int id = rset.getInt("numero_mesa");
            int capacidad = rset.getInt("capacidad");
            String estado = rset.getString("estado_mesa");
            int sala = rset.getInt("sala");
            String prioritaria = rset.getString("prioritaria");
            mesasDisponibles.add(new Mesa(id, capacidad, estado, sala, prioritaria));
        }

        pstmt.close();
        c.close();

        return mesasDisponibles;
    }

    // actualizar el estado de las mesas, se le pasa como parametro un numero de
    // mesa y el estado.
    public void actualizarEstadoMesa(int numero_mesa, String estado) throws SQLException {
        Connection c = Dao.openConnection();
        PreparedStatement pstmt = c.prepareStatement(actualizar_estado_mesa);
        pstmt.setString(1, estado);
        pstmt.setInt(2, numero_mesa);
        pstmt.executeUpdate();
        pstmt.close();
        c.close();

    }

    // marcar todas las mesas disponibles
    public void marcarTodasLasMesasComoDisponibles() throws SQLException {
        Connection c = Dao.openConnection();
        PreparedStatement pstmt = c.prepareStatement(marcar_todas_las_mesas_disponibles);
        pstmt.executeUpdate();
        pstmt.close();
        c.close();
    }
}
