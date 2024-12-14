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
                System.out.printf("ID: %d, Cliente ID: %d, Monto: %.2f, Tipo: %s, Fecha: %s%n",
                        rs.getInt("id"), rs.getInt("cliente_id"), rs.getDouble("monto"),
                        rs.getString("tipo_inversion"), rs.getString("fecha_inversion"));
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener las inversiones: " + e.getMessage());
        }
    }

    // Método para eliminar una inversión enumerada
    public static void mostrarYEliminarInversiones(Connection conn) {
        String sql = "SELECT * FROM inversion";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            int index = 1;
            while (rs.next()) {
                System.out.printf("%d. Monto: %.2f, Tipo: %s, Fecha: %s%n",
                        index, rs.getDouble("monto"), rs.getString("tipo_inversion"),
                        rs.getString("fecha_inversion"));
                index++;
            }

            if (index == 1) {
                System.out.println("No hay inversiones para eliminar.");
                return;
            }

            Scanner scanner = new Scanner(System.in);
            System.out.print("Seleccione el número de la inversión a eliminar: ");
            int opcion = scanner.nextInt();
            scanner.nextLine(); // Limpiar el buffer

            if (opcion < 1 || opcion >= index) {
                System.out.println("Opción no válida.");
                return;
            }

            rs.beforeFirst();
            for (int i = 0; i < opcion; i++) {
                rs.next();
            }
            eliminarInversion(conn, rs.getInt("id"));

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
            System.out.println(rowsAffected > 0 ? "Inversión eliminada con éxito." : "No se encontró la inversión con el ID especificado.");
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
            System.out.println(rowsAffected > 0 ? "Tipo de inversión eliminado con éxito." : "No se encontró el tipo de inversión con el nombre especificado.");
        } catch (SQLException e) {
            System.out.println("Error al eliminar el tipo de inversión: " + e.getMessage());
        }
    }

    // Método para eliminar un usuario
    public static void eliminarUsuario(Connection conn, int usuarioId) {
        String sql = "DELETE FROM usuario WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, usuarioId);
            int rowsAffected = pstmt.executeUpdate();
            System.out.println(rowsAffected > 0 ? "Usuario eliminado con éxito." : "No se encontró el usuario con el ID especificado.");
        } catch (SQLException e) {
            System.out.println("Error al eliminar el usuario: " + e.getMessage());
        }
    }

    // Método para mostrar todos los usuarios
    public static void mostrarUsuarios(Connection conn) {
        String sql = "SELECT * FROM usuario";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                System.out.printf("ID: %d, Username: %s, Tipo: %s, Saldo: %.2f%n",
                        rs.getInt("id"), rs.getString("username"),
                        rs.getString("tipo"), rs.getDouble("saldo"));
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener los usuarios: " + e.getMessage());
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
            System.out.println("5. Eliminar usuario");
            System.out.println("6. Salir");
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
                    mostrarYEliminarInversiones(conn);
                    break;
                case 5:
                    mostrarUsuarios(conn);
                    System.out.print("Ingrese el ID del usuario a eliminar: ");
                    int usuarioId = scanner.nextInt();
                    scanner.nextLine(); // Limpiar el buffer
                    eliminarUsuario(conn, usuarioId);
                    break;
                case 6:
                    System.out.println("Saliendo del menú Admin...");
                    break;
                default:
                    System.out.println("Opción no válida.");
            }
        } while (opcionAdmin != 6);
    }
}
