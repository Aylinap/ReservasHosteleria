import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalTime;
import java.util.*;

public class MesaDao {
    private static final String obtener_mesas_disponible = "SELECT * FROM mesa WHERE estado_mesa = 'disponible'";
    private static final String obtener_mesa_por_id = "SELECT * FROM mesa WHERE id = ?";
    private static final String obtener_mesa_capacidad = "SELECT * FROM mesa WHERE capacidad >= ? AND estado_mesa = 'disponible'";

    public List<Mesa> obtenerMesas() throws SQLException {
        List<Mesa> mesas = new ArrayList<>();
        Connection c = Dao.openConnection();
        PreparedStatement pstmt = c.prepareStatement(obtener_mesa_por_id);
        ResultSet rset = pstmt.executeQuery();

        while (rset.next()) {
            
            Mesa mesa = mapearMesa(rset);
            mesas.add(mesa);
        }

        pstmt.executeUpdate();
        pstmt.close();
        c.close();

        return mesas;
    }

    public List<Mesa> obtenerMesasDisponibles() throws SQLException {
        Connection c = Dao.openConnection();
        PreparedStatement pstmt = c.prepareStatement(obtener_mesas_disponible);
        ResultSet rset = pstmt.executeQuery();
        List<Mesa> mesasDisponibles = new ArrayList<>();

        while (rset.next()) {
            Mesa mesa = mapearMesa(rset);
            mesasDisponibles.add(mesa);
        }

        return mesasDisponibles;
    }

    // Método para mapear un resultado de consulta a un objeto Mesa
    private Mesa mapearMesa(ResultSet resultSet) throws SQLException {
        Mesa mesa = new Mesa();
        mesa.setNumero_mesa(resultSet.getInt("numero_mesa"));
        mesa.setCapacidad(resultSet.getInt("capacidad"));

        return mesa;
    }


   
}
