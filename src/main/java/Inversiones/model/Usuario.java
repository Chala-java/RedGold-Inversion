package Inversiones.model;

import java.sql.*;
import java.util.Scanner;

public class Usuario {
    private int id;
    private String username;
    private String password;
    private String tipo;
    private double saldo;
    private static int usuarioIniciadoId = -1; //

    // Constructor
    public Usuario(String username, String password, String tipo, double saldo) {
        this.username = username;
        this.password = password;
        this.tipo = tipo;
        this.saldo = saldo;
    }

    public Usuario(String username, String password, String tipo)
    {
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

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    // aqui se obtiene el ID del usuario que inicio sesion
    public static int getUsuarioLogueadoId() {
        return usuarioIniciadoId;
    }

    //Aqui se registra con su tipo de usuario tanto con su tipo de usuario
    public static void registrarUsuario(Connection conn, String username, String password, String tipo, double saldo) {
        String sql = "INSERT INTO usuario (username, password, tipo, saldo) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.setString(3, tipo);
            pstmt.setDouble(4, saldo);
            pstmt.executeUpdate();
            System.out.println("Usuario registrado con éxito.");
        } catch (SQLException e) {
            System.out.println("Error al registrar usuario: " + e.getMessage());
        }
    }

    // aqui se inicia sesion
    public static Usuario iniciarSesion(Connection conn) {
        // Solicita las credenciales del usuario
        Scanner scanner = new Scanner(System.in);
        System.out.println("** Iniciar sesión **");
        System.out.print("Ingrese el nombre de usuario: ");
        String username = scanner.nextLine();
        System.out.print("Ingrese la contraseña: ");
        String password = scanner.nextLine();

        // Verifica las credenciales en la base de datos
        String sql = "SELECT * FROM usuario WHERE username = ? AND password = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String tipo = rs.getString("tipo");
                double saldo = rs.getDouble("saldo");

                // Dependiendo del tipo, crea un objeto Admin o Usuario
                Usuario usuario = tipo.equals("admin") ? new Admin(username, password, tipo) : new Usuario(username, password, tipo, saldo);
                usuario.id = rs.getInt("id");

                // Guardar el ID del usuario que inició sesión
                usuarioIniciadoId = usuario.id;

                return usuario;
            } else {
                System.out.println("Credenciales incorrectas.");
                return null;
            }
        } catch (SQLException e) {
            System.out.println("Error al iniciar sesión: " + e.getMessage());
            return null;
        }
    }

    // Método para actualizar el saldo del usuario
    public static void actualizarSaldo(Connection conn, int usuarioId, double nuevoSaldo) {
        String sql = "UPDATE usuario SET saldo = ? WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDouble(1, nuevoSaldo);
            pstmt.setInt(2, usuarioId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error al actualizar saldo: " + e.getMessage());
        }
    }

    // Método para ver las inversiones de un usuario
    public static void verInversiones(Connection conn, int usuarioId) {
        String sql = "SELECT * FROM inversion WHERE cliente_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, usuarioId);
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
