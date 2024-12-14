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
                            registrarUsuario(conn, scanner);
                            break;

                        case 2:
                            usuario = Usuario.iniciarSesion(conn);
                            if (usuario != null) {
                                if (usuario instanceof Admin) {
                                    System.out.println("Bienvenido Admin.");
                                    mostrarMenuAdmin(conn, usuario);
                                } else {
                                    System.out.println("Bienvenido " + usuario.getUsername() + ".");
                                    mostrarMenuUsuario(conn, scanner, usuario);
                                }
                            }
                            break;

                        case 3:
                            invertir(conn);
                            break;

                        case 4:
                            if (usuario instanceof Admin) {
                                mostrarMenuAdmin(conn, usuario);
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

    private static void registrarUsuario(Connection conn, Scanner scanner) throws SQLException {
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
        double saldo = 0;

        if ("cliente".equals(tipo)) {
            System.out.print("Ingrese el saldo inicial: ");
            saldo = scanner.nextDouble();
            scanner.nextLine(); // Limpiar el buffer
        }

        Usuario.registrarUsuario(conn, username, password, tipo, saldo);
    }

    private static void mostrarMenuUsuario(Connection conn, Scanner scanner, Usuario usuario) {
        int subOpcion;
        do {
            System.out.println("\n** Menú Usuario **");
            System.out.println("1. Invertir");
            System.out.println("2. Ver inversiones");
            System.out.println("3. Salir");
            System.out.print("Elija una opción: ");
            subOpcion = scanner.nextInt();
            scanner.nextLine(); // Limpiar el buffer

            switch (subOpcion) {
                case 1:
                    invertir(conn);
                    break;

                case 2:
                    verInversiones(conn);
                    break;

                case 3:
                    System.out.println("Saliendo...");
                    break;

                default:
                    System.out.println("Opción no válida.");
            }
        } while (subOpcion != 3);
    }

    private static void invertir(Connection conn) {
        int usuarioLogueadoId = Usuario.getUsuarioLogueadoId();
        if (usuarioLogueadoId != -1) {
            Inversion.mostrarMenuInversion(conn, usuarioLogueadoId);
        } else {
            System.out.println("Debe iniciar sesión antes de realizar una inversión.");
        }
    }

    private static void verInversiones(Connection conn) {
        int usuarioLogueadoId = Usuario.getUsuarioLogueadoId();
        if (usuarioLogueadoId != -1) {
            Inversion.obtenerInversionesPorCliente(conn, usuarioLogueadoId);
        } else {
            System.out.println("Debe iniciar sesión antes de ver las inversiones.");
        }
    }

    private static void mostrarMenuAdmin(Connection conn, Usuario usuario) {
        if (Usuario.getUsuarioLogueadoId() != -1 && usuario instanceof Admin) {
            Admin.mostrarMenuAdmin(conn);
        } else {
            System.out.println("Debe iniciar sesión como administrador para acceder al menú Admin.");
        }
    }
}
