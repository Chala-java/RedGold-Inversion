package Inversiones.UsuarioDAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.SQLException;

public class Conector {
    public static void main(String[] args) {
        String url = "jdbc:sqlite:C:/SQL_lite/InversionesRed.db";  // Usar siempre el archivo .db

        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {

            // Configurar modos para acceso concurrente y bloqueos
            stmt.execute("PRAGMA busy_timeout = 10000;");
            stmt.execute("PRAGMA journal_mode = WAL;");

            // Crear tabla 'usuario'
            String sqlUsuario = "CREATE TABLE IF NOT EXISTS usuario (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "username TEXT NOT NULL," +
                    "password TEXT NOT NULL," +
                    "tipo TEXT NOT NULL," +
                    "saldo REAL DEFAULT 0);";
            stmt.execute(sqlUsuario);

            // Crear tabla 'inversion'
            String sqlInversion = "CREATE TABLE IF NOT EXISTS inversion (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "cliente_id INTEGER," +
                    "monto REAL NOT NULL," +
                    "tipo_inversion TEXT NOT NULL," +
                    "fecha_inversion TEXT NOT NULL," +
                    "FOREIGN KEY (cliente_id) REFERENCES usuario(id));";
            stmt.execute(sqlInversion);

            System.out.println("Tablas creadas con éxito.");
        } catch (SQLException e) {
            System.out.println("Error al crear las tablas: " + e.getMessage());
        }
    }
}
