package Inversiones.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TipoInversion {
    private int id;
    private String tipo;
    private double tasaInteres;

    // Constructor
    public TipoInversion(String tipo, double tasaInteres) {
        this.tipo = tipo;
        this.tasaInteres = tasaInteres;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public double getTasaInteres() {
        return tasaInteres;
    }

    public void setTasaInteres(double tasaInteres) {
        this.tasaInteres = tasaInteres;
    }

    // Método para crear un nuevo tipo de inversión
    public static void crearTipoInversion(Connection conn, String tipo, double tasaInteres) {
        String sql = "INSERT INTO tipo_inversion (tipo, tasa_interes) VALUES (?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, tipo);
            pstmt.setDouble(2, tasaInteres);
            pstmt.executeUpdate();
            System.out.println("Tipo de inversión creado con éxito.");
        } catch (SQLException e) {
            System.out.println("Error al crear el tipo de inversión: " + e.getMessage());
        }
    }

    // Método para obtener todos los tipos de inversión
    public static List<TipoInversion> obtenerTiposInversion(Connection conn) {
        List<TipoInversion> tipos = new ArrayList<>();
        String sql = "SELECT * FROM tipo_inversion";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            int index = 1;
            while (rs.next()) {
                int id = rs.getInt("id");
                String tipo = rs.getString("tipo");
                double tasaInteres = rs.getDouble("tasa_interes");
                System.out.println(index + ". Tipo: " + tipo + ", Tasa de Interés: " + tasaInteres + "%");
                tipos.add(new TipoInversion(tipo, tasaInteres));
                tipos.get(tipos.size() - 1).setId(id);
                index++;
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener los tipos de inversión: " + e.getMessage());
        }
        return tipos;
    }

    // Método para obtener el nombre del tipo de inversión por ID
    public static String obtenerTipoInversionPorId(Connection conn, int tipoInversionId) {
        String sql = "SELECT tipo FROM tipo_inversion WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, tipoInversionId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getString("tipo");
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener el tipo de inversión: " + e.getMessage());
        }
        return null;
    }
}
