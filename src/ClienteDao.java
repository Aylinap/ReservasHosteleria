import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ClienteDao {

    private static final String insert_cliente = " insert into cliente (nombre_cliente, telefono, email) values (?,?,?)";

    // inserta nuevo cliente y obtengo el id del cliente
    public int insertarCliente(Cliente nuevoCliente) throws SQLException {
        Connection c = Dao.openConnection();
        PreparedStatement pstmt = c.prepareStatement(insert_cliente);
        ResultSet rset = pstmt.executeQuery();
        int generatedId = 0;

        pstmt.setString(1, nuevoCliente.getNombre());
        pstmt.setInt(2, nuevoCliente.getTelefono());
        pstmt.setString(3, nuevoCliente.getEmail());

        pstmt.executeUpdate();

        rset = pstmt.getGeneratedKeys(); // obtiene claves generadas

        if (rset.next()) {
            generatedId = rset.getInt(1);
        }

        return generatedId;

    }

}
