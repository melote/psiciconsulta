package com.psicicultura.controller;

import com.psicicultura.util.Conexion;
import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FiltrarRegistros {

    private JFrame frame;

    public FiltrarRegistros(JFrame frame) {
        this.frame = frame;
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

        String tipoSeleccionado = (String) JOptionPane.showInputDialog(
                frame,
                "Seleccione un tipo de variable:",
                "Filtrar por Tipo",
                JOptionPane.QUESTION_MESSAGE,
                null,
                tipos.toArray(),
                tipos.get(0)
        );

        if (tipoSeleccionado == null) return;

        // Obtener nombres de variable según el tipo seleccionado
        try (PreparedStatement stmtNombres = conn.prepareStatement("SELECT nombre FROM variable WHERE tipo = ? ORDER BY nombre")) {
            stmtNombres.setString(1, tipoSeleccionado);
            ResultSet rsNombres = stmtNombres.executeQuery();

            List<String> nombres = new ArrayList<>();
            while (rsNombres.next()) {
                nombres.add(rsNombres.getString("nombre"));
            }

            if (nombres.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "No hay variables para este tipo.", "Información", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            String nombreSeleccionado = (String) JOptionPane.showInputDialog(
                    frame,
                    "Seleccione una variable:",
                    "Filtrar por Variable",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    nombres.toArray(),
                    nombres.get(0)
            );

            if (nombreSeleccionado == null) return;

            // Consultar registros filtrados
            try (PreparedStatement stmtRegistros = conn.prepareStatement(
                    "SELECT r.id, v.nombre AS variable_nombre, r.valor, r.observacion, r.fecha " +
                            "FROM registro r " +
                            "JOIN variable v ON r.variable_id = v.id " +
                            "WHERE v.tipo = ? AND v.nombre = ? " +
                            "ORDER BY r.fecha DESC")) {

                stmtRegistros.setString(1, tipoSeleccionado);
                stmtRegistros.setString(2, nombreSeleccionado);
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
                    registros.append("No hay registros para esta variable.");
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
}
