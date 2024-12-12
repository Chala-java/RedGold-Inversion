package Inversiones.model;

import java.sql.*;
import java.util.List;
import java.util.Scanner;

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

            // Actualizar el saldo del cliente
            double nuevoSaldo = obtenerSaldo(conn, clienteId) - monto;
            Usuario.actualizarSaldo(conn, clienteId, nuevoSaldo);

            // Mostrar el nuevo saldo al usuario
            System.out.println("Nuevo saldo: " + nuevoSaldo);
        } catch (SQLException e) {
            System.out.println("Error al insertar la inversión: " + e.getMessage());
        }
    }

    // Método para obtener el saldo del cliente
    public static double obtenerSaldo(Connection conn, int clienteId) {
        String sql = "SELECT saldo FROM usuario WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, clienteId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("saldo");
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener saldo: " + e.getMessage());
        }
        return 0;
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

    // Método para mostrar el menú de inversión
    public static void mostrarMenuInversion(Connection conn, int clienteId) {
        Scanner scanner = new Scanner(System.in);

        double saldo = obtenerSaldo(conn, clienteId);
        if (saldo <= 0) {
            System.out.println("No tiene saldo disponible para invertir.");
            return;
        }

        System.out.println("Saldo disponible: " + saldo);
        System.out.println("Tipos de inversión disponibles:");
        List<TipoInversion> tipos = TipoInversion.obtenerTiposInversion(conn);

        System.out.print("Seleccione el tipo de inversión por número: ");
        int opcion = scanner.nextInt();
        scanner.nextLine(); // Limpiar el buffer

        if (opcion < 1 || opcion > tipos.size()) {
            System.out.println("Opción no válida.");
            return;
        }

        TipoInversion tipoSeleccionado = tipos.get(opcion - 1);

        System.out.print("Ingrese el monto a invertir: ");
        double monto = scanner.nextDouble();
        scanner.nextLine(); // Limpiar el buffer

        if (monto > saldo) {
            System.out.println("No tiene suficiente saldo para realizar esta inversión.");
            return;
        }

        insertarInversion(conn, clienteId, monto, tipoSeleccionado.getTipo());
    }
}
