package com.psicicultura.views;

import javax.swing.*;
import com.psicicultura.service.MenuLogin;
import java.awt.*;

public class MenuLoginView extends JFrame {

    public MenuLoginView() {
        setTitle("PISCICONSULTA");
        setSize(1000, 650); // Tamaño más grande
        setResizable(false); // ❌ No redimensionable
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centrar ventana

        // Panel principal con BoxLayout vertical y centrado
        JPanel panelPrincipal = new JPanel();
        panelPrincipal.setLayout(new BoxLayout(panelPrincipal, BoxLayout.Y_AXIS));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(60, 60, 60, 60));
        panelPrincipal.setBackground(new Color(245, 250, 255));

        JLabel lblTitulo = new JLabel("PISCICONSULTA");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 36));
        lblTitulo.setForeground(new Color(40, 40, 40));
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton btnIniciarSesion = crearBoton("Iniciar Sesión", new Color(0, 123, 255));
        btnIniciarSesion.addActionListener(e -> {
            MenuLogin.mostrarMenuLoginView();
            mostrarAlerta("Acción Iniciar Sesión", "Has pulsado Iniciar Sesión", JOptionPane.INFORMATION_MESSAGE);
        });
        btnIniciarSesion.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton btnRegistrarse = crearBoton("Registrarse", new Color(40, 167, 69));
        btnRegistrarse.addActionListener(e -> {
            MenuRegistroView.mostrarMenuRegistroGUI();
            mostrarAlerta("Acción Registrarse", "Has pulsado Registrarse", JOptionPane.INFORMATION_MESSAGE);
        });
        btnRegistrarse.setAlignmentX(Component.CENTER_ALIGNMENT);

        panelPrincipal.add(Box.createVerticalGlue());
        panelPrincipal.add(lblTitulo);
        panelPrincipal.add(Box.createRigidArea(new Dimension(0, 50)));
        panelPrincipal.add(btnIniciarSesion);
        panelPrincipal.add(Box.createRigidArea(new Dimension(0, 20)));
        panelPrincipal.add(btnRegistrarse);
        panelPrincipal.add(Box.createVerticalGlue());

        add(panelPrincipal);
    }

    // Crear botón con color plano y estilo FlatLaf
    private JButton crearBoton(String texto, Color color) {
        JButton boton = new JButton(texto.toUpperCase());
        boton.setMaximumSize(new Dimension(220, 50));
        boton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        boton.setFocusPainted(false);
        boton.setForeground(Color.WHITE);
        boton.setBackground(color);
        boton.setOpaque(true);
        boton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        return boton;
    }

    // Método para mostrar alertas más bonitas con FlatLaf
    private void mostrarAlerta(String titulo, String mensaje, int tipoMensaje) {
        // Usamos JOptionPane que con FlatLaf ya tiene un mejor estilo visual
        JOptionPane.showMessageDialog(this, mensaje, titulo, tipoMensaje);
    }
}
