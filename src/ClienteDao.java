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

        int filafectada = pstmt.executeUpdate();

        if (filafectada == 0) {
            throw new SQLException("La inserción del cliente falló, no se modificaron filas(no se agregó).");
        }

        ResultSet generatedKeys = pstmt.getGeneratedKeys();
        int generatedId = 0;
        if (generatedKeys.next()) {
            generatedId = generatedKeys.getInt(1);
        } else {
            throw new SQLException("La inserción del cliente falló, no se generaron las id(claves).");
        }

        pstmt.close();
        c.close();

        return generatedId;
    }

    // 
}
