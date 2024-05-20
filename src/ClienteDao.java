import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ClienteDao {

    private static final String insert_cliente = " insert into cliente (nombre_cliente, telefono, email) values (?,?,?)";

    // inserta nuevo cliente y obtengo el id del cliente
    public int insertarCliente(Cliente nuevoCliente) throws SQLException {
        Connection c = Dao.openConnection();
        PreparedStatement pstmt = c.prepareStatement(insert_cliente, Statement.RETURN_GENERATED_KEYS);

        pstmt.setString(1, nuevoCliente.getNombre());
        pstmt.setInt(2, nuevoCliente.getTelefono());
        pstmt.setString(3, nuevoCliente.getEmail());

        int rowsAffected = pstmt.executeUpdate(); // Ejecutar la inserción

        if (rowsAffected == 0) {
            throw new SQLException("La inserción del cliente falló, no se modificaron filas.");
        }

        ResultSet generatedKeys = pstmt.getGeneratedKeys(); // Obtener las claves generadas
        int generatedId = 0;
        if (generatedKeys.next()) {
            generatedId = generatedKeys.getInt(1);
        } else {
            throw new SQLException("La inserción del cliente falló, no se generaron claves.");
        }

        pstmt.close();
        c.close();

        return generatedId;
    }

}
