package com.psicicultura.views;

import com.psicicultura.util.Conexion;
import com.psicicultura.model.IntegradorDatosExternos;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class MenuAdministradorView {

    private JFrame frame;
    private JPanel mainPanel;
    private CardLayout cardLayout;

    public MenuAdministradorView() {
        frame = new JFrame("Menú Administrador");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(850, 500);
        frame.setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        mainPanel.add(createMainMenuPanel(), "main");
        mainPanel.add(createUserManagementPanel(), "userManagement");
        mainPanel.add(createListUsersPanel(), "listUsers");
        mainPanel.add(createDeleteUserPanel(), "deleteUser");
        mainPanel.add(createModifyUserPanel(), "modifyUser");

        frame.add(mainPanel);
        frame.setVisible(true);
    }

    private JPanel createMainMenuPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(245, 250, 255));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel titleLabel = new JLabel("Menú Administrador", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        panel.add(titleLabel, gbc);

        JButton manageUsersBtn = crearBoton("Gestionar Usuarios");
        manageUsersBtn.addActionListener(e -> cardLayout.show(mainPanel, "userManagement"));
        panel.add(manageUsersBtn, gbc);

        JButton reportBtn = crearBoton("Ver Reportes");
        reportBtn.addActionListener(e -> JOptionPane.showMessageDialog(frame, "Funcionalidad de reportes (por implementar)"));
        panel.add(reportBtn, gbc);

        JButton configBtn = crearBoton("Configuraciones");
        configBtn.addActionListener(e -> JOptionPane.showMessageDialog(frame, "Funcionalidad de configuraciones (por implementar)"));
        panel.add(configBtn, gbc);

        JButton integrateCaptureBtn = crearBoton("Integrar captura pesquera 2020");
        integrateCaptureBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(frame, "Iniciando integración de datos...");
        
            SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
                private Exception error = null;

                @Override
                protected Void doInBackground() {
                    try {
                        System.out.println(">> Ejecutando integración...");
                        IntegradorDatosExternos.integrarCapturaPesquera2020();
                        System.out.println(">> Integración completada sin errores.");
                    } catch (Exception ex) {
                        System.out.println(">> Error durante la integración: " + ex.getMessage());
                        error = ex;
                    }
                    return null;
                }

                @Override
                protected void done() {
                    if (error != null) {
                        JOptionPane.showMessageDialog(frame, "Error al integrar datos: " + error.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(frame, "Integración completada exitosamente.");
                    }
                }
            };
        
            worker.execute();
        });
        
        
        panel.add(integrateCaptureBtn, gbc);

        JButton integrateSalesBtn = crearBoton("Integrar gastos acuícolas 2020");
        integrateSalesBtn.addActionListener(e -> {
            try {
                JOptionPane.showMessageDialog(frame, "Iniciando integración de gastos...");
                IntegradorDatosExternos.integrarVentasGastosAcuicolas();
                JOptionPane.showMessageDialog(frame, "Integración de gastos completada.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        panel.add(integrateSalesBtn, gbc);

        JButton logoutBtn = crearBoton("Cerrar Sesión");
        logoutBtn.addActionListener(e -> frame.dispose());
        panel.add(logoutBtn, gbc);

        return panel;
    }

    private JPanel createUserManagementPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(245, 250, 255));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel title = new JLabel("Gestión de Usuarios");
        title.setFont(new Font("SansSerif", Font.BOLD, 20));
        panel.add(title, gbc);

        JButton listBtn = crearBoton("Listar Usuarios");
        listBtn.addActionListener(e -> cardLayout.show(mainPanel, "listUsers"));
        panel.add(listBtn, gbc);

        JButton delBtn = crearBoton("Eliminar Usuario");
        delBtn.addActionListener(e -> cardLayout.show(mainPanel, "deleteUser"));
        panel.add(delBtn, gbc);

        JButton modBtn = crearBoton("Modificar Usuario");
        modBtn.addActionListener(e -> cardLayout.show(mainPanel, "modifyUser"));
        panel.add(modBtn, gbc);

        JButton backBtn = crearBoton("Volver");
        backBtn.addActionListener(e -> cardLayout.show(mainPanel, "main"));
        panel.add(backBtn, gbc);

        return panel;
    }

    private JPanel createListUsersPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(245, 250, 255));

        JTextArea usersArea = new JTextArea(listUsers());
        usersArea.setFont(new Font("SansSerif", Font.PLAIN, 14));
        panel.add(new JScrollPane(usersArea), BorderLayout.CENTER);

        JButton backBtn = crearBoton("Volver");
        backBtn.addActionListener(e -> cardLayout.show(mainPanel, "userManagement"));
        panel.add(backBtn, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createDeleteUserPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(245, 250, 255));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        panel.add(new JLabel("ID del usuario a eliminar:"), gbc);
        JTextField idField = new JTextField(15);
        panel.add(idField, gbc);

        JButton delBtn = crearBoton("Eliminar");
        delBtn.addActionListener(e -> {
            try {
                int id = Integer.parseInt(idField.getText());
                if (deleteUser(id)) {
                    JOptionPane.showMessageDialog(frame, "Usuario eliminado.");
                } else {
                    JOptionPane.showMessageDialog(frame, "Usuario no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "ID inválido.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        panel.add(delBtn, gbc);

        JButton backBtn = crearBoton("Volver");
        backBtn.addActionListener(e -> cardLayout.show(mainPanel, "userManagement"));
        panel.add(backBtn, gbc);

        return panel;
    }

    private JPanel createModifyUserPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(245, 250, 255));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        panel.add(new JLabel("ID del usuario a modificar:"), gbc);
        JTextField idField = new JTextField(15);
        panel.add(idField, gbc);

        JButton modBtn = crearBoton("Modificar");
        modBtn.addActionListener(e -> {
            try {
                int id = Integer.parseInt(idField.getText());
                String[] opciones = {"Correo", "Nombre", "Rol"};
                String campo = (String) JOptionPane.showInputDialog(
                        frame, "¿Qué desea modificar?", "Campo a modificar",
                        JOptionPane.QUESTION_MESSAGE, null, opciones, opciones[0]);

                if (campo != null) {
                    String nuevoValor = JOptionPane.showInputDialog("Nuevo valor para " + campo + ":");
                    if (nuevoValor != null && !nuevoValor.trim().isEmpty()) {
                        boolean exito = modifyUser(id, campo.toLowerCase(), nuevoValor.trim());
                        if (exito) {
                            JOptionPane.showMessageDialog(frame, "Usuario modificado.");
                        } else {
                            JOptionPane.showMessageDialog(frame, "No se pudo modificar el usuario.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "ID inválido.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        panel.add(modBtn, gbc);

        JButton backBtn = crearBoton("Volver");
        backBtn.addActionListener(e -> cardLayout.show(mainPanel, "userManagement"));
        panel.add(backBtn, gbc);

        return panel;
    }

    private boolean deleteUser(int id) {
        try (Connection conn = Conexion.getConexion();
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM usuario WHERE id = ?")) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(frame, "Error al eliminar usuario: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    private boolean modifyUser(int id, String campo, String nuevoValor) {
        String sql = "UPDATE usuario SET " + campo + " = ? WHERE id = ?";
        try (Connection conn = Conexion.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nuevoValor);
            stmt.setInt(2, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(frame, "Error al modificar usuario: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    private String listUsers() {
        StringBuilder sb = new StringBuilder();
        try (Connection conn = Conexion.getConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT id, nombre, correo, rol FROM usuario ORDER BY id")) {
            while (rs.next()) {
                sb.append("ID: ").append(rs.getInt("id")).append("\n");
                sb.append("Nombre: ").append(rs.getString("nombre")).append("\n");
                sb.append("Correo: ").append(rs.getString("correo")).append("\n");
                sb.append("Rol: ").append(rs.getString("rol")).append("\n");
                sb.append("------------------------\n");
            }
        } catch (SQLException e) {
            sb.append("Error al obtener usuarios: ").append(e.getMessage());
        }
        return sb.toString();
    }

    private JButton crearBoton(String texto) {
        JButton boton = new JButton(texto.toUpperCase());
        boton.setMaximumSize(new Dimension(200, 50));
        boton.setFont(new Font("SansSerif", Font.BOLD, 14));
        boton.setBackground(new Color(0, 123, 255));
        boton.setForeground(Color.WHITE);
        return boton;
    }

    public static void mostrarMenu() {
        SwingUtilities.invokeLater(MenuAdministradorView::new);
    }
}
