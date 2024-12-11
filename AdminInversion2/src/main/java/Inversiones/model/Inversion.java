package Inversiones.model;


import java.sql.*;

public class Inversion {
    private int id;
    private int clienteId;
    private double monto;
    private String tipoInversion;
    private String fechaInversion;

    // Constructor
    public Inversion(int clienteId, double monto, String tipoInversion, String fechaInversion) {
        this.clienteId = clienteId;
        this.monto = monto;
        this.tipoInversion = tipoInversion;
        this.fechaInversion = fechaInversion;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getClienteId() {
        return clienteId;
    }

    public void setClienteId(int clienteId) {
        this.clienteId = clienteId;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public String getTipoInversion() {
        return tipoInversion;
    }

    public void setTipoInversion(String tipoInversion) {
        this.tipoInversion = tipoInversion;
    }

    public String getFechaInversion() {
        return fechaInversion;
    }

    public void setFechaInversion(String fechaInversion) {
        this.fechaInversion = fechaInversion;
    }

    // Método para insertar una inversión en la base de datos
    public static void insertarInversion(Connection conn, int clienteId, double monto, String tipoInversion) {
        String sql = "INSERT INTO inversion (cliente_id, monto, tipo_inversion, fecha_inversion) VALUES (?, ?, ?, DATE('now'))";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, clienteId);
            pstmt.setDouble(2, monto);
            pstmt.setString(3, tipoInversion);
            pstmt.executeUpdate();
            System.out.println("Inversión realizada con éxito.");
        } catch (SQLException e) {
            System.out.println("Error al insertar la inversión: " + e.getMessage());
        }
    }

    // Método para obtener las inversiones de un cliente
    public static void obtenerInversionesPorCliente(Connection conn, int clienteId) {
        String sql = "SELECT * FROM inversion WHERE cliente_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, clienteId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                double monto = rs.getDouble("monto");
                String tipoInversion = rs.getString("tipo_inversion");
                String fechaInversion = rs.getString("fecha_inversion");

                System.out.println("ID: " + id + ", Monto: " + monto + ", Tipo: " + tipoInversion + ", Fecha: " + fechaInversion);
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener las inversiones: " + e.getMessage());
        }
    }
}
