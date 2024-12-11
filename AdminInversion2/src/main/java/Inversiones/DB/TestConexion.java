package Inversiones.DB;
import java.sql.Connection;

public class TestConexion {
    public static void main(String[] args) {
        Connection conn = Conexion.getConnection();

        if (conn != null) {
            System.out.println("Prueba de conexión exitosa.");
            Conexion.closeConnection(conn);
        } else {
            System.out.println("Prueba de conexión fallida.");
        }
    }
}
