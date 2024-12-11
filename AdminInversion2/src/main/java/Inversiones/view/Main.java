package Inversiones.view;

import Inversiones.model.Admin;
import Inversiones.model.Usuario;

import java.sql.*;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        String url = "jdbc:sqlite:C:/Users/Cossio/Desktop/RedGoldBank.db"; // Ruta de tu base de datos
        Scanner scanner = new Scanner(System.in);
        int opcion;

        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                do {
                    System.out.println("\n** Menú Principal **");
                    System.out.println("1. Registrarse");
                    System.out.println("2. Iniciar sesión");
                    System.out.println("3. Invertir (Solo después de iniciar sesión)");
                    System.out.println("4. Ver inversiones de clientes (Admin)");
                    System.out.println("5. Salir");
                    System.out.print("Elija una opción: ");
                    opcion = scanner.nextInt();
                    scanner.nextLine(); // Limpiar el buffer

                    switch (opcion) {
                        case 1:
                            // Llamamos al método de registro de la clase Usuario
                            System.out.print("Ingrese el nombre de usuario: ");
                            String username = scanner.nextLine();
                            System.out.print("Ingrese la contraseña: ");
                            String password = scanner.nextLine();
                            System.out.print("Ingrese el tipo de usuario (cliente/admin): ");
                            String tipo = scanner.nextLine();
                            Usuario.registrarUsuario(conn, username, password, tipo);
                            break;

                        case 2:
                            // Llamamos al método de inicio de sesión de la clase Usuario
                            Usuario usuario = Usuario.iniciarSesion(conn);
                            if (usuario != null) {
                                if (usuario instanceof Admin) {
                                    System.out.println("Bienvenido Admin.");
                                } else {
                                    System.out.println("Bienvenido Usuario.");
                                }
                            }
                            break;

                        case 3:
                            // Si el usuario ha iniciado sesión, puede invertir
                            int usuarioLogueadoId = Usuario.getUsuarioLogueadoId();
                            if (usuarioLogueadoId != -1) {
                                // Llamamos al método para realizar una inversión
                                Usuario.realizarInversion(conn, usuarioLogueadoId);
                            } else {
                                System.out.println("Debe iniciar sesión antes de realizar una inversión.");
                            }
                            break;

                        case 4:
                            // Ver inversiones (solo para admin)
                            // El admin se puede autenticar y luego ver las inversiones
                            System.out.print("Ingrese el nombre de usuario admin: ");
                            String adminUser = scanner.nextLine();
                            System.out.print("Ingrese la contraseña del admin: ");
                            String adminPass = scanner.nextLine();

                            // Verificar credenciales del admin
                            String sqlAdmin = "SELECT * FROM usuario WHERE username = ? AND password = ? AND tipo = 'admin'";
                            try (PreparedStatement pstmt = conn.prepareStatement(sqlAdmin)) {
                                pstmt.setString(1, adminUser);
                                pstmt.setString(2, adminPass);
                                ResultSet rs = pstmt.executeQuery();

                                if (rs.next()) {
                                    // Si el admin existe, mostrar las inversiones
                                    Admin.verInversiones(conn);
                                } else {
                                    System.out.println("Credenciales de administrador incorrectas.");
                                }
                            } catch (SQLException e) {
                                System.out.println("Error al verificar admin: " + e.getMessage());
                            }
                            break;

                        case 5:
                            System.out.println("Saliendo...");
                            break;

                        default:
                            System.out.println("Opción no válida.");
                    }
                } while (opcion != 5);
            }
        } catch (SQLException e) {
            System.out.println("Error de conexión: " + e.getMessage());
        }
    }
}
