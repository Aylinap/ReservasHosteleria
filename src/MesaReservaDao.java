import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.mysql.cj.x.protobuf.MysqlxCrud.Insert;

public class MesaReservaDao {
    private static final String insertar_reserva_mesa = "insert into reseramesasala (numero_mesa, numero_sala, mesa_combinada, estado_mesa) values (?,?,?,?)";

    public void insertar_reserva_mesa(Mesa nuevaMesa) throws SQLException {
        Connection c = Dao.openConnection();
        PreparedStatement pstmt = c.prepareStatement(insertar_reserva_mesa);

        pstmt.setString(1, insertar_reserva_mesa);
        System.out.println("Sentencia SQL para reserva_mesa_nueva: " + pstmt.toString());
        System.out.println("Reserva creada correctamente en tabla de reserva_mesa");
        pstmt.executeUpdate();
        pstmt.close();
        c.close();
    }
}
