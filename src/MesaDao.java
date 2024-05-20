import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalTime;
import java.util.*;

import com.mysql.cj.xdevapi.Result;

public class MesaDao {
    private static final String obtener_mesas_disponible = "select * from mesa where estado_mesa = 'disponible'";
    private static final String obtener_mesas = "select * from mesa";
    private static final String obtener_mesa_capacidad = "select * from mesa where capacidad >= ? and estado_mesa = 'disponible'";
    private static final String obtener_mesas_disponible_capacidad = "select numero_mesa, capacidad from mesas where capacidad >= ? and estado_mesa = 'disponible' order by capacidad";
    private static final String actualizar_estado_mesa = "update mesa set estado_mesa = ? where numero_mesa = ?";

    public List<Mesa> obtenerTodasLasMesas() throws SQLException {
        List<Mesa> mesas = new ArrayList<>();
        Connection c = Dao.openConnection();
        PreparedStatement pstmt = c.prepareStatement(obtener_mesas);
        ResultSet rset = pstmt.executeQuery();

        while (rset.next()) {
            int id = rset.getInt("numero_mesa");
            int capacidad = rset.getInt("capacidad");
            String estado = rset.getString("estado_mesa");
            int sala = rset.getInt("sala");
            mesas.add(new Mesa(id, capacidad, estado, sala)); // devuelvo un objeto Meesa sacado desde la bbdd
        }

        pstmt.executeUpdate();
        pstmt.close();
        c.close();
        return mesas;
    }

    // obtener todas disponibles dependiendo del numero de comensales
    public Mesa obtenerMesasDisponibles(int numComensales) throws SQLException {
        Connection c = Dao.openConnection();
        PreparedStatement pstmt = c.prepareStatement(obtener_mesas_disponible_capacidad);
        ResultSet rset = pstmt.executeQuery();

        // verificar si funciona el metodo la verdad
        pstmt.setInt(1, numComensales);

        if (rset.next()) {
            int id = rset.getInt("numero_mesa");
            int capacidad = rset.getInt("capacidad");
            int sala = rset.getInt("sala");
            return new Mesa(id, capacidad, "disponible", sala);
        }
        pstmt.executeUpdate();
        pstmt.close();
        c.close();

        return null;
    }

    // obtener el estado de todas las mesas disponibles independientes del numero de
    // comensales
    public List<Mesa> obtenerTodasDisponibles() throws SQLException {
        List<Mesa> mesasDisponibles = new ArrayList<>();
        Connection c = Dao.openConnection();
        PreparedStatement pstmt = c.prepareStatement(obtener_mesas_disponible);
        ResultSet rset = pstmt.executeQuery();

        while (rset.next()) {
            int id = rset.getInt("numero_mesa");
            int capacidad = rset.getInt("capacidad");
            String estado = rset.getString("estado_mesa");
            int sala = rset.getInt("sala");
            mesasDisponibles.add(new Mesa(id, capacidad, estado, sala));
        }

        return mesasDisponibles;
    }

    // actualizar el estado de la mesa: 
    public void actualizarEstadoMesa(int numero_mesa, String estado) throws SQLException {
        Connection c = Dao.openConnection();
        PreparedStatement pstmt = c.prepareStatement(actualizar_estado_mesa);
        ResultSet rset = pstmt.executeQuery();

        pstmt.setInt(1, numero_mesa);
        pstmt.setString(2, estado);
        pstmt.executeUpdate();
        pstmt.close();
        c.close();
    }

}
