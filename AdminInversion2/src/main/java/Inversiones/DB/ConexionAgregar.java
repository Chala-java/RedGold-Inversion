package Inversiones.DB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.SQLException;

public class ConexionAgregar {
    public static void main(String[] args) {
        String url = "jdbc:sqlite:C:/SQL_lite/InversionesRed.db";  // Usar siempre el archivo .db

        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {
            //Esta vuelta es para cerrar la conexion con la base de datos
            stmt.execute("PRAGMA busy_timeout = 10000;");
            stmt.execute("PRAGMA journal_mode = WAL;");

            // agregar la columna 'saldo' a la tabla 'usuario' si no existe
            String sqlAgregarSaldo = "ALTER TABLE usuario ADD COLUMN saldo REAL DEFAULT 0";
            try {
                stmt.execute(sqlAgregarSaldo);
                System.out.println("Columna 'saldo' agregada con éxito.");
            } catch (SQLException e) {
                if (!e.getMessage().contains("duplicate column name")) {
                    throw e;
                }
            }

            String sqlTipoInversion = "CREATE TABLE IF NOT EXISTS tipo_inversion (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "tipo TEXT NOT NULL," +
                    "tasa_interes REAL NOT NULL);";
            stmt.execute(sqlTipoInversion);

            System.out.println("mejora  completada con éxito.");
        } catch (SQLException e) {
            System.out.println("Error al realizar la mejora: " + e.getMessage());
        }
    }
}
