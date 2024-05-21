import java.sql.*;
import java.util.*;

public class MesaDao {
    private static final String obtener_mesas_disponible = "select * from mesa where estado_mesa = 'disponible'";
    private static final String obtener_mesas = "select * from mesa";
    private static final String obtener_mesa_capacidad = "select * from mesa where capacidad >= ? and estado_mesa = 'disponible'";
    private static final String obtener_mesa_disponible_capacidad = "select * from mesa where capacidad >= ? and estado_mesa = 'disponible' order by capacidad";
    private static final String actualizar_estado_mesa = "update mesa set estado_mesa = ? where numero_mesa = ?";

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
            mesas.add(new Mesa(id, capacidad, estado, sala));
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

        // preguntar-->
        pstmt.setInt(1, numComensales);
        ResultSet rset = pstmt.executeQuery();

        // si existe busco y obtengo la info la guardo en variables para pasarlas al
        // constructor de mesa.
        if (rset.next()) {
            int id = rset.getInt("numero_mesa");
            int capacidad = rset.getInt("capacidad");
            int sala = rset.getInt("sala");
            // cierro la conexion
            rset.close();
            pstmt.close();
            c.close();
            // retorno un objeto mesa con la info obtenida
            return new Mesa(id, capacidad, "disponible", sala);
        }

        rset.close();
        pstmt.close();
        c.close();
        return null;
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
}
