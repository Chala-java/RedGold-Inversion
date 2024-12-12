package Inversiones.model;

import java.sql.*;
import java.util.List;
import java.util.Scanner;

public class Admin extends Usuario {

    public Admin(String username, String password, String tipo) {
        super(username, password, tipo);
    }

    // Método para ver todas las inversiones
    public static void verInversiones(Connection conn) {
        String sql = "SELECT * FROM inversion";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                int id = rs.getInt("id");
                int clienteId = rs.getInt("cliente_id");
                double monto = rs.getDouble("monto");
                String tipoInversion = rs.getString("tipo_inversion");
                String fechaInversion = rs.getString("fecha_inversion");

                System.out.println("ID: " + id + ", Cliente ID: " + clienteId + ", Monto: " + monto + ", Tipo: " + tipoInversion + ", Fecha: " + fechaInversion);
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener las inversiones: " + e.getMessage());
        }
    }

    // Método para eliminar una inversión
    public static void eliminarInversion(Connection conn, int inversionId) {
        String sql = "DELETE FROM inversion WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, inversionId);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Inversión eliminada con éxito.");
            } else {
                System.out.println("No se encontró la inversión con el ID especificado.");
            }
        } catch (SQLException e) {
            System.out.println("Error al eliminar la inversión: " + e.getMessage());
        }
    }

    // Método para crear un tipo de inversión
    public static void crearTipoInversion(Connection conn, String tipoInversion, double tasaInteres) {
        TipoInversion.crearTipoInversion(conn, tipoInversion, tasaInteres);
    }

    // Método para eliminar un tipo de inversión usando un índice temporal
    public static void eliminarTipoInversion(Connection conn, int opcion) {
        List<TipoInversion> tipos = TipoInversion.obtenerTiposInversion(conn);
        if (opcion < 1 || opcion > tipos.size()) {
            System.out.println("Opción no válida.");
            return;
        }
        TipoInversion tipoSeleccionado = tipos.get(opcion - 1);
        String sql = "DELETE FROM tipo_inversion WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, tipoSeleccionado.getId());
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Tipo de inversión eliminado con éxito.");
            } else {
                System.out.println("No se encontró el tipo de inversión con el nombre especificado.");
            }
        } catch (SQLException e) {
            System.out.println("Error al eliminar el tipo de inversión: " + e.getMessage());
        }
    }

    // Método para mostrar el menú Admin
    public static void mostrarMenuAdmin(Connection conn) {
        Scanner scanner = new Scanner(System.in);
        int opcionAdmin;
        do {
            System.out.println("\n** Menú Admin **");
            System.out.println("1. Ver todas las inversiones");
            System.out.println("2. Crear tipo de inversión");
            System.out.println("3. Eliminar tipo de inversión");
            System.out.println("4. Eliminar inversión");
            System.out.println("5. Salir");
            System.out.print("Elija una opción: ");
            opcionAdmin = scanner.nextInt();
            scanner.nextLine(); // Limpiar el buffer
            switch (opcionAdmin) {
                case 1:
                    verInversiones(conn);
                    break;

                case 2:
                    System.out.print("Ingrese el nombre del tipo de inversión: ");
                    String tipo = scanner.nextLine();
                    System.out.print("Ingrese la tasa de interés: ");
                    double tasaInteres = scanner.nextDouble();
                    scanner.nextLine(); // Limpiar el buffer
                    crearTipoInversion(conn, tipo, tasaInteres);
                    break;

                case 3:
                    System.out.println("Tipos de inversión disponibles:");
                    List<TipoInversion> tipos = TipoInversion.obtenerTiposInversion(conn);
                    System.out.print("Seleccione el tipo de inversión a eliminar por número: ");
                    int opcion = scanner.nextInt();
                    scanner.nextLine(); // Limpiar el buffer
                    eliminarTipoInversion(conn, opcion);
                    break;

                case 4:
                    System.out.print("Ingrese el ID de la inversión a eliminar: ");
                    int inversionId = scanner.nextInt();
                    scanner.nextLine(); // Limpiar el buffer
                    eliminarInversion(conn, inversionId);
                    break;

                case 5:
                    System.out.println("Saliendo del menú Admin...");
                    break;

                default:
                    System.out.println("Opción no válida.");
            }
        } while (opcionAdmin != 5);
    }
}
