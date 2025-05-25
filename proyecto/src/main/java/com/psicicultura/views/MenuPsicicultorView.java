package com.psicicultura.views;

import com.psicicultura.util.Conexion;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MenuPsicicultorView {

    private JFrame frame;

    public MenuPsicicultorView() {
        frame = new JFrame("Menú Piscicultor");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(850, 500); // Ajustado al tamaño similar al del MenuAdministradorView
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(245, 250, 255)); // Color de fondo suave
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel titleLabel = new JLabel("Menú Piscicultor", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        titleLabel.setForeground(new Color(50, 50, 50)); // Color del texto
        panel.add(titleLabel, gbc);

        JButton areaInteresBtn = crearBoton("Seleccionar área de interés");
        areaInteresBtn.addActionListener(e -> seleccionarAreaDeInteres());
        panel.add(areaInteresBtn, gbc);

        JButton consultarRegistrosBtn = crearBoton("Consultar registros");
        consultarRegistrosBtn.addActionListener(e -> consultarRegistros());
        panel.add(consultarRegistrosBtn, gbc);

        JButton filtrarRegistrosBtn = crearBoton("Filtrar registros");
        filtrarRegistrosBtn.addActionListener(e -> filtrarRegistros());
        panel.add(filtrarRegistrosBtn, gbc);

        JButton filtrarUbicacionFechaBtn = crearBoton("Filtrar por ubicación y fecha");
        filtrarUbicacionFechaBtn.addActionListener(e -> filtrarPorUbicacionYFecha());
        panel.add(filtrarUbicacionFechaBtn, gbc);

        JButton logoutBtn = crearBoton("Cerrar Sesión");
        logoutBtn.addActionListener(e -> frame.dispose());
        panel.add(logoutBtn, gbc);

        frame.add(panel);
        frame.setVisible(true);
    }

    private JButton crearBoton(String texto) {
        JButton boton = new JButton(texto.toUpperCase());
        boton.setMaximumSize(new Dimension(200, 50));
        boton.setFont(new Font("SansSerif", Font.BOLD, 16));
        boton.setBackground(new Color(0, 123, 255));
        boton.setForeground(Color.WHITE);
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        return boton;
    }

    private void seleccionarAreaDeInteres() {
        try (Connection conn = Conexion.getConexion();
             PreparedStatement stmt = conn.prepareStatement("SELECT DISTINCT tipo FROM variable ORDER BY tipo");
             ResultSet rs = stmt.executeQuery()) {

            List<String> areas = new ArrayList<>();
            while (rs.next()) {
                areas.add(rs.getString("tipo"));
            }

            if (areas.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "No hay áreas de interés registradas.", "Información", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            String seleccion = (String) JOptionPane.showInputDialog(
                    frame,
                    "Seleccione un área de interés:",
                    "Áreas de Interés",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    areas.toArray(),
                    areas.get(0)
            );

            if (seleccion != null) {
                JOptionPane.showMessageDialog(frame, "Área seleccionada: " + seleccion, "Información", JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(frame, "Error al consultar áreas de interés: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void consultarRegistros() {
        try (Connection conn = Conexion.getConexion();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT r.id, v.nombre AS variable_nombre, r.valor, r.observacion, r.fecha " +
                             "FROM registro r " +
                             "JOIN variable v ON r.variable_id = v.id " +
                             "ORDER BY r.fecha DESC");
             ResultSet rs = stmt.executeQuery()) {

            StringBuilder registros = new StringBuilder();
            while (rs.next()) {
                registros.append("ID: ").append(rs.getInt("id")).append("\n")
                        .append("Variable: ").append(rs.getString("variable_nombre")).append("\n")
                        .append("Valor: ").append(rs.getObject("valor")).append("\n")
                        .append("Observación: ").append(rs.getString("observacion")).append("\n")
                        .append("Fecha: ").append(rs.getString("fecha")).append("\n")
                        .append("---------------------------\n");
            }

            if (registros.length() == 0) {
                registros.append("No hay registros disponibles.");
            }

            JTextArea textArea = new JTextArea(registros.toString());
            textArea.setEditable(false);
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(600, 400));

            JOptionPane.showMessageDialog(frame, scrollPane, "Registros", JOptionPane.INFORMATION_MESSAGE);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(frame, "Error al consultar registros: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void filtrarRegistros() {
        try (Connection conn = Conexion.getConexion();
             PreparedStatement stmtTipos = conn.prepareStatement("SELECT DISTINCT tipo FROM variable ORDER BY tipo");
             ResultSet rsTipos = stmtTipos.executeQuery()) {

            List<String> tipos = new ArrayList<>();
            while (rsTipos.next()) {
                tipos.add(rsTipos.getString("tipo"));
            }

            if (tipos.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "No hay tipos de variables registrados.", "Información", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            String seleccion = (String) JOptionPane.showInputDialog(
                    frame,
                    "Seleccione un tipo de variable:",
                    "Filtrar Registros",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    tipos.toArray(),
                    tipos.get(0)
            );

            if (seleccion != null) {
                try (PreparedStatement stmtRegistros = conn.prepareStatement(
                        "SELECT r.id, v.nombre AS variable_nombre, r.valor, r.observacion, r.fecha " +
                                "FROM registro r " +
                                "JOIN variable v ON r.variable_id = v.id " +
                                "WHERE v.tipo = ? " +
                                "ORDER BY r.fecha DESC")) {

                    stmtRegistros.setString(1, seleccion);
                    ResultSet rsRegistros = stmtRegistros.executeQuery();

                    StringBuilder registros = new StringBuilder();
                    while (rsRegistros.next()) {
                        registros.append("ID: ").append(rsRegistros.getInt("id")).append("\n")
                                .append("Variable: ").append(rsRegistros.getString("variable_nombre")).append("\n")
                                .append("Valor: ").append(rsRegistros.getObject("valor")).append("\n")
                                .append("Observación: ").append(rsRegistros.getString("observacion")).append("\n")
                                .append("Fecha: ").append(rsRegistros.getString("fecha")).append("\n")
                                .append("---------------------------\n");
                    }

                    if (registros.length() == 0) {
                        registros.append("No hay registros para este tipo.");
                    }

                    JTextArea textArea = new JTextArea(registros.toString());
                    textArea.setEditable(false);
                    JScrollPane scrollPane = new JScrollPane(textArea);
                    scrollPane.setPreferredSize(new Dimension(600, 400));

                    JOptionPane.showMessageDialog(frame, scrollPane, "Registros Filtrados", JOptionPane.INFORMATION_MESSAGE);
                }
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(frame, "Error al filtrar registros: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void filtrarPorUbicacionYFecha() {
        JOptionPane.showMessageDialog(frame, "Funcionalidad de filtrado por ubicación y fecha aún no implementada.", "Información", JOptionPane.INFORMATION_MESSAGE);
    }
}
