import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Dao {
    // La dirección de la base de datos en el servidor
    // junto con el usuario de pleno acceso y su respectiva contraseña
    private static String url = "jdbc:mysql://localhost:3306/gestor_reservas";
    private static String user = "administrador";
    private static String pass = "admin123";

    // La conexción con la BBDD
    public static Connection con = openConnection();

    // metodo abrir conexion
    public static Connection openConnection() {
        try {
            Connection connection = DriverManager.getConnection(url, user, pass);
            return connection;
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // cerrar conexion
    public static void closeConnection() {
        try {
            if (con != null && !con.isClosed()) {
                con.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
