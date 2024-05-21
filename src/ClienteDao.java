import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ClienteDao {

    // sql para insertar en la tabla de Cliente.
    private static final String insert_cliente = " insert into cliente (nombre_cliente, telefono, email) values (?,?,?)";

    // inserta nuevo cliente y obtengo el id del cliente

    public int insertarCliente(Cliente nuevoCliente) throws SQLException {

        // abrir conexion
        Connection c = Dao.openConnection();
        PreparedStatement pstmt = c.prepareStatement(insert_cliente, Statement.RETURN_GENERATED_KEYS);
        // insertar informacion de cliente
        pstmt.setString(1, nuevoCliente.getNombre());
        pstmt.setInt(2, nuevoCliente.getTelefono());
        pstmt.setString(3, nuevoCliente.getEmail());

        // desde aqui toma el id_cliente generado en la bd y la retorna para poder
        // usarla en la insercion de la informacion de Reserva

        // https://www.ibm.com/docs/es/db2-for-zos/12?topic=applications-retrieving-auto-generated-keys-insert-statement
        int filafectada = pstmt.executeUpdate();
        // si la fila es igual a 0 o no existe
        if (filafectada == 0) {

            // lanza una exception, tambien puede lanzar un print, solo es para indentificar
            // que no funcionó
            throw new SQLException("El insert del cliente falló, no se modificaron filas(no se agregó).");
        }
        
        ResultSet generatedKeys = pstmt.getGeneratedKeys();
        int idgeneradaCliente = 0;
        if (generatedKeys.next()) {
            // obtenemos la id-key generada y la guardamos en idGeneradaCliente para
            // retornarla y pasarla por la informacion de Reserva.
            idgeneradaCliente = generatedKeys.getInt(1);
        } else {
            throw new SQLException("El insert del cliente falló, no se generaron las id(claves).");
        }

        pstmt.close();
        c.close();

        return idgeneradaCliente;
    }

    //
}
