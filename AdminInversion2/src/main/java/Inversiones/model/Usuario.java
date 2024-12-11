package Inversiones.model;

import java.sql.*;

import java.util.Scanner;

public class Usuario {
    protected int id;
    protected String username;
    protected String password;
    protected String tipo; // 'cliente' o 'admin'

    // Variable estática para almacenar el ID del usuario logueado
    private static int usuarioLogueadoId = -1; // -1 indica que no hay usuario logueado

    // Constructor
    public Usuario(String username, String password, String tipo) {
        this.username = username;
        this.password = password;
        this.tipo = tipo;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    // Método para obtener el ID del usuario logueado
    public static int getUsuarioLogueadoId() {
        return usuarioLogueadoId;
    }

    // Método para registrar un usuario (tanto Admin como Cliente)
    public static void registrarUsuario(Connection conn, String username, String password, String tipo) {
        String sql = "INSERT INTO usuario (username, password, tipo) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.setString(3, tipo);
            pstmt.executeUpdate();
            System.out.println("Usuario registrado con éxito.");
        } catch (SQLException e) {
            System.out.println("Error al registrar usuario: " + e.getMessage());
        }
    }

    // Método para iniciar sesión
    public static Usuario iniciarSesion(Connection conn) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("** Iniciar sesión **");
        System.out.print("Ingrese el nombre de usuario: ");
        String username = scanner.nextLine();
        System.out.print("Ingrese la contraseña: ");
        String password = scanner.nextLine();

        String sql = "SELECT * FROM usuario WHERE username = ? AND password = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String tipo = rs.getString("tipo");
                Usuario usuario = tipo.equals("admin") ? new Admin(username, password, tipo) : new Usuario(username, password, tipo);
                usuario.id = rs.getInt("id");

                // Guardar el ID del usuario logueado
                usuarioLogueadoId = usuario.id;

                return usuario; // Devuelve un objeto de tipo Admin o Usuario
            } else {
                System.out.println("Credenciales incorrectas.");
                return null;
            }
        } catch (SQLException e) {
            System.out.println("Error al iniciar sesión: " + e.getMessage());
            return null;
        }
    }

    // Método para realizar una inversión
    public static void realizarInversion(Connection conn, int clienteId) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Ingrese el monto de la inversión: ");
        double monto = scanner.nextDouble();
        scanner.nextLine(); // Limpiar el buffer

        System.out.println("Seleccione el tipo de inversión (fiducuenta/plan semilla/colombia): ");
        String tipoInversion = scanner.nextLine();

        // Insertar la inversión en la base de datos
        String sql = "INSERT INTO inversion (cliente_id, monto, tipo_inversion, fecha_inversion) VALUES (?, ?, ?, DATE('now'))";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, clienteId);
            pstmt.setDouble(2, monto);
            pstmt.setString(3, tipoInversion);
            pstmt.executeUpdate();
            System.out.println("Inversión realizada con éxito.");
        } catch (SQLException e) {
            System.out.println("Error al realizar la inversión: " + e.getMessage());
        }
    }
}
