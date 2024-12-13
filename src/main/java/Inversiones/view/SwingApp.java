package Inversiones.view;

import Inversiones.DB.Conexion;
import Inversiones.model.Admin;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.Connection;

public class SwingApp extends JFrame {
    private JButton btnVerInversiones;
    private JButton btnCrearTipoInversion;
    private JButton btnEliminarTipoInversion;
    private JButton btnEliminarInversion;

    private Connection conn;

    public SwingApp(Connection conn) {
        this.conn = conn;
        setTitle("Menú Admin de Inversiones");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(5, 1));

        btnVerInversiones = new JButton("Ver todas las inversiones");
        btnCrearTipoInversion = new JButton("Crear tipo de inversión");
        btnEliminarTipoInversion = new JButton("Eliminar tipo de inversión");
        btnEliminarInversion = new JButton("Eliminar inversión");

        btnVerInversiones.addActionListener(e -> Admin.verInversiones(conn));

        btnCrearTipoInversion.addActionListener(e -> crearTipoInversion());

        btnEliminarTipoInversion.addActionListener(e -> eliminarTipoInversion());

        btnEliminarInversion.addActionListener(e -> eliminarInversion());

        add(btnVerInversiones);
        add(btnCrearTipoInversion);
        add(btnEliminarTipoInversion);
        add(btnEliminarInversion);
    }

    private void crearTipoInversion() {
        try {
            String tipo = JOptionPane.showInputDialog(this, "Ingrese el nombre del tipo de inversión:");
            if (tipo == null || tipo.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "El tipo de inversión no puede estar vacío.");
                return;
            }

            String tasaInteresStr = JOptionPane.showInputDialog(this, "Ingrese la tasa de interés (número positivo):");
            double tasaInteres = Double.parseDouble(tasaInteresStr);

            if (tasaInteres <= 0) {
                JOptionPane.showMessageDialog(this, "La tasa de interés debe ser mayor a 0.");
                return;
            }

            Admin.crearTipoInversion(conn, tipo, tasaInteres);
            JOptionPane.showMessageDialog(this, "Tipo de inversión creado exitosamente.");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Por favor, ingrese un número válido para la tasa de interés.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Ocurrió un error: " + e.getMessage());
        }
    }

    private void eliminarTipoInversion() {
        try {
            String opcionStr = JOptionPane.showInputDialog(this, "Ingrese el número del tipo de inversión a eliminar:");
            int opcion = Integer.parseInt(opcionStr);

            Admin.eliminarTipoInversion(conn, opcion);
            JOptionPane.showMessageDialog(this, "Tipo de inversión eliminado exitosamente.");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Por favor, ingrese un número válido.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Ocurrió un error: " + e.getMessage());
        }
    }

    private void eliminarInversion() {
        try {
            String inversionIdStr = JOptionPane.showInputDialog(this, "Ingrese el ID de la inversión a eliminar:");
            int inversionId = Integer.parseInt(inversionIdStr);

            Admin.eliminarInversion(conn, inversionId);
            JOptionPane.showMessageDialog(this, "Inversión eliminada exitosamente.");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Por favor, ingrese un número válido.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Ocurrió un error: " + e.getMessage());
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        Conexion.closeConnection(conn);
    }

    public static void main(String[] args) {
        Connection conn = Conexion.getConnection(); // Establece la conexión

        if (conn != null) {
            SwingUtilities.invokeLater(() -> {
                SwingApp gui = new SwingApp(conn);
                gui.setVisible(true);
            });
        } else {
            System.out.println("No se pudo establecer la conexión. Verifica la base de datos.");
        }

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            Conexion.closeConnection(conn);
        }));
    }
}
