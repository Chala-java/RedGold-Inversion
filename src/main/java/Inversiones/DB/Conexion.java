package Inversiones.DB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {

        private static final  String URL = "jdbc:sqlite:C:/SQL_lite/InversionesRed.db";  // Usar siempre el archivo .db


        public static Connection getConnection() {
            Connection conn = null;
            try {
                conn = DriverManager.getConnection(URL);
                System.out.println("Conexión establecida exitosamente.");
            } catch (SQLException e) {
                System.out.println("Error al establecer la conexión: " + e.getMessage());
            }
            return conn;
        }

        public static void closeConnection(Connection conn) {
            if (conn != null) {
                try {
                    conn.close();
                    System.out.println("Conexión cerrada exitosamente.");
                } catch (SQLException e) {
                    System.out.println("Error al cerrar la conexión: " + e.getMessage());
                }
            }
        }
    }


