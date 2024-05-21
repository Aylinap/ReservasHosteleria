import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class MesaReservaDao {
    private static final String insertar_reserva_mesa = "insert into reseramesa (numero_mesa, numero_sala, mesa_combinada, estado_mesa) values (?,?,?,?)";
    private static final String obtener_estado_mesas = "select estado_mesa from reservamesa ";
    private static final String cambiar_estado_mesa = "update reservamesa set estado_mesa=? where  ";

    // ================================================================
    // CLASE que todavia no se está usando o quizas no se vaya a usar nunca.
    // =================================================================

    // despues este metodo llamarlo en crear reserva aunque no se si pueden llamar
    // entre los dao.
    // public void insertar_reserva_mesa(Mesa nuevaMesa) throws SQLException {
    // Connection c = Dao.openConnection();
    // PreparedStatement pstmt = c.prepareStatement(insertar_reserva_mesa);

    // pstmt.setString(1, insertar_reserva_mesa);

    // System.out.println("Sentencia SQL para reserva_mesa_nueva: " +
    // pstmt.toString());
    // System.out.println("Reserva creada correctamente en tabla de reserva_mesa");
    // pstmt.executeUpdate();
    // pstmt.close();
    // c.close();
    // }

    // metodo para obtener un arreglo de mesas con estado disponible.
    // public ArrayList<Mesa> obtenerMesasDisponibles() throws SQLException {
    // ArrayList<Mesa> mesasDisponibles = new ArrayList<>();

    // Connection c = Dao.openConnection();
    // PreparedStatement pstmt = c.prepareStatement(obtener_estado_mesas);
    // ResultSet rset = pstmt.executeQuery();

    // while (rset.next()) {
    // String estado = rset.getString("estado_mesa");
    // int numeroMesa = rset.getInt("numero_mesa");

    // if (estado != null && estado.equals("disponible")) {

    // Mesa mesa = new Mesa(numeroMesa);
    // mesasDisponibles.add(mesa);
    // }
    // }

    // pstmt.close();
    // c.close();

    // return mesasDisponibles;
    // }

    // // actualizar estado de disponible a ocupado o reservado

    // public void actualizarEstadoMesa() throws SQLException{

    // }
}
