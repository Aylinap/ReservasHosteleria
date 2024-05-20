import java.sql.*;
import java.util.*;

public class MesaDao {
    private static final String obtener_mesas_disponible = "SELECT * FROM mesa WHERE estado_mesa = 'disponible'";
    private static final String obtener_mesas = "SELECT * FROM mesa";
    private static final String obtener_mesa_capacidad = "SELECT * FROM mesa WHERE capacidad >= ? AND estado_mesa = 'disponible'";
    private static final String obtener_mesas_disponible_capacidad = "SELECT * FROM mesa WHERE capacidad >= ? AND estado_mesa = 'disponible' ORDER BY capacidad";
    private static final String actualizar_estado_mesa = "UPDATE mesa SET estado_mesa = ? WHERE numero_mesa = ?";

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
            mesas.add(new Mesa(id, capacidad, estado, sala));
        }

        rset.close();
        pstmt.close();
        c.close();
        return mesas;
    }

    public Mesa obtenerMesasDisponibles(int numComensales) throws SQLException {
        Connection c = Dao.openConnection();
        PreparedStatement pstmt = c.prepareStatement(obtener_mesas_disponible_capacidad);
        pstmt.setInt(1, numComensales);
        ResultSet rset = pstmt.executeQuery();

        if (rset.next()) {
            int id = rset.getInt("numero_mesa");
            int capacidad = rset.getInt("capacidad");
            int sala = rset.getInt("sala");
            rset.close();
            pstmt.close();
            c.close();
            return new Mesa(id, capacidad, "disponible", sala);
        }

        rset.close();
        pstmt.close();
        c.close();
        return null;
    }

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
