package Inversiones.model;

import java.sql.*;
import java.util.Scanner;

public class Admin extends Usuario {

    // Constructor
    public Admin(String username, String password, String tipo) {
        super(username, password, tipo);
    }

    // Getters y Setters
    @Override
    public String getUsername() {
        return super.getUsername();
    }

    @Override
    public void setUsername(String username) {
        super.setUsername(username);
    }

    @Override
    public String getPassword() {
        return super.getPassword();
    }

    @Override
    public void setPassword(String password) {
        super.setPassword(password);
    }

    // Método para ver todas las inversiones de los clientes
    public static void verInversiones(Connection conn) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("** Ver todas las inversiones **");

        String sql = "SELECT u.username, i.monto, i.tipo_inversion, i.fecha_inversion " +
                "FROM inversion i " +
                "JOIN usuario u ON i.cliente_id = u.id";

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            System.out.println("Inversiones de los usuarios:");
            while (rs.next()) {
                String username = rs.getString("username");
                double monto = rs.getDouble("monto");
                String tipoInversion = rs.getString("tipo_inversion");
                String fechaInversion = rs.getString("fecha_inversion");
                System.out.println("Usuario: " + username + ", Monto: " + monto + ", Tipo: " + tipoInversion + ", Fecha: " + fechaInversion);
            }
        } catch (SQLException e) {
            System.out.println("Error al ver inversiones: " + e.getMessage());
        }
    }
}
