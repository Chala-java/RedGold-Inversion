package Inversiones.view;

import Inversiones.model.Admin;
import Inversiones.model.Usuario;
import Inversiones.model.Inversion;

import java.sql.*;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        String url = "jdbc:sqlite:C:/SQL_lite/InversionesRed.db";  // Usar siempre el archivo .db
        Scanner scanner = new Scanner(System.in);
        int opcion;

        try (Connection conn = DriverManager.getConnection(url)) {
            Usuario usuario = null; // Definimos la variable usuario aquí
            if (conn != null) {
                do {
                    System.out.println("\n** Menú Principal **");
                    System.out.println("1. Registrarse");
                    System.out.println("2. Iniciar sesión");
                    System.out.println("3. Invertir (Solo después de iniciar sesión)");
                    System.out.println("4. Menú Admin");
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
                            System.out.println("Elija el tipo de usuario: ");
                            System.out.println("1. Cliente");
                            System.out.println("2. Admin");
                            int tipoOpcion = scanner.nextInt();
                            scanner.nextLine(); // Limpiar el buffer
                            String tipo = (tipoOpcion == 1) ? "cliente" : "admin";
                            System.out.print("Ingrese el saldo inicial: ");
                            double saldo = scanner.nextDouble();
                            scanner.nextLine(); // Limpiar el buffer
                            Usuario.registrarUsuario(conn, username, password, tipo, saldo);
                            break;

                        case 2:
                            // Llamamos al método de inicio de sesión de la clase Usuario
                            usuario = Usuario.iniciarSesion(conn);
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
                                Inversion.mostrarMenuInversion(conn, usuarioLogueadoId);
                            } else {
                                System.out.println("Debe iniciar sesión antes de realizar una inversión.");
                            }
                            break;

                        case 4:
                            // Menú Admin
                            if (Usuario.getUsuarioLogueadoId() != -1 && usuario instanceof Admin) {
                                Admin.mostrarMenuAdmin(conn);
                            } else {
                                System.out.println("Debe iniciar sesión como administrador para acceder al menú Admin.");
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
