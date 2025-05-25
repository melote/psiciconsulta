package com.psicicultura.service;

import java.awt.*;
import javax.swing.*;
import com.formdev.flatlaf.FlatLightLaf;
import com.psicicultura.views.MenuAdministradorView;
import com.psicicultura.views.MenuPsicicultorView;

public class MenuLogin {

    // Bloque estático que se ejecuta al cargar la clase
    static {
        // Configura el look and feel FlatLaf (tema claro)
        FlatLightLaf.setup();
        
        // COLORES GENERALES (puedes modificar estos valores RGB)
        UIManager.put("OptionPane.background", new Color(240, 245, 250)); // Fondo general azul claro
        UIManager.put("Panel.background", new Color(240, 245, 250)); // Fondo de paneles
    }

    public static void mostrarMenuLoginView() {
        // -----------------------------------------------
        // CONFIGURACIÓN DEL PANEL PRINCIPAL
        // -----------------------------------------------
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(240, 245, 250)); // Mismo color que arriba
        // Espaciado interno (arriba, izquierda, abajo, derecha) - Ajusta estos valores
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        // -----------------------------------------------
        // PANEL DEL FORMULARIO (campos de texto)
        // -----------------------------------------------
        JPanel formPanel = new JPanel(new GridLayout(2, 1, 15, 20)); // 2 filas, 1 columna, espacios entre componentes
        formPanel.setBackground(new Color(240, 245, 250)); // Mismo fondo

        // -----------------------------------------------
        // CAMPO DE CORREO ELECTRÓNICO
        // -----------------------------------------------
        JPanel emailPanel = new JPanel(new BorderLayout(5, 5));
        emailPanel.setBackground(new Color(240, 245, 250));
        
        // Etiqueta "Correo electrónico"
        JLabel lblCorreo = new JLabel("Correo electrónico:");
        lblCorreo.setFont(new Font("Segoe UI", Font.BOLD, 14)); // Fuente en negrita, tamaño 14
        
        // Campo de texto para correo
        JTextField txtCorreo = new JTextField();
        txtCorreo.setPreferredSize(new Dimension(300, 30)); // Ancho: 300px, Alto: 40px
        txtCorreo.setFont(new Font("Segoe UI", Font.PLAIN, 14)); // Fuente normal, tamaño 14
        
        emailPanel.add(lblCorreo, BorderLayout.NORTH);
        emailPanel.add(txtCorreo, BorderLayout.CENTER);

        // -----------------------------------------------
        // CAMPO DE CONTRASEÑA
        // -----------------------------------------------
        JPanel passPanel = new JPanel(new BorderLayout(5, 5));
        passPanel.setBackground(new Color(240, 245, 250));
        
        // Etiqueta "Contraseña"
        JLabel lblContrasena = new JLabel("Contraseña:");
        lblContrasena.setFont(new Font("Segoe UI", Font.BOLD, 14)); // Mismo estilo que correo
        
        // Campo de contraseña
        JPasswordField txtContrasena = new JPasswordField();
        txtContrasena.setPreferredSize(new Dimension(300, 30)); // Mismo tamaño que correo
        txtContrasena.setFont(new Font("Segoe UI", Font.PLAIN, 14)); // Mismo estilo
        
        passPanel.add(lblContrasena, BorderLayout.NORTH);
        passPanel.add(txtContrasena, BorderLayout.CENTER);

        // -----------------------------------------------
        // ENSAMBLAJE DEL FORMULARIO
        // -----------------------------------------------
        formPanel.add(emailPanel);
        formPanel.add(passPanel);
        mainPanel.add(formPanel, BorderLayout.CENTER);

        // -----------------------------------------------
        // MOSTRAR EL DIÁLOGO DE LOGIN
        // -----------------------------------------------
        int opcion = JOptionPane.showOptionDialog(
            null, // Componente padre (null = centro de pantalla)
            mainPanel, // Panel a mostrar
            "Inicio de Sesión", // Título de la ventana
            JOptionPane.OK_CANCEL_OPTION, // Tipo de opciones
            JOptionPane.PLAIN_MESSAGE, // Tipo de mensaje
            null, // Icono (null = sin icono)
            new Object[]{"Iniciar Sesión", "Cancelar"}, // Texto de los botones
            "Iniciar Sesión" // Botón por defecto
        );

        // -----------------------------------------------
        // PROCESAR RESPUESTA DEL USUARIO
        // -----------------------------------------------
        if (opcion == JOptionPane.OK_OPTION) { // Si presionó "Iniciar Sesión"
            String correo = txtCorreo.getText().trim(); // Obtener correo sin espacios
            String contrasena = new String(txtContrasena.getPassword()).trim(); // Obtener contraseña

            // Autenticar al usuario
            String rol = Autenticador.autenticar(correo, contrasena);

            if (rol != null) {
                // Mensaje de éxito (fondo verde claro)
                mostrarMensajePersonalizado(
                    "¡Bienvenido!\nAutenticación exitosa como " + rol,
                    "Éxito",
                    new Color(230, 245, 230) // RGB para verde claro
                );
                
                // Redirigir según el rol
                if (rol.equalsIgnoreCase("administrador")) {
                    SwingUtilities.invokeLater(() -> new MenuAdministradorView());
                } else if (rol.equalsIgnoreCase("piscicultor")) {
                    SwingUtilities.invokeLater(() -> new MenuPsicicultorView());
                }
            } else {
                // Mensaje de error (fondo rojo claro)
                mostrarMensajePersonalizado(
                    "Credenciales incorrectas\nPor favor verifique sus datos",
                    "Error",
                    new Color(255, 230, 230) // RGB para rojo claro
                );
            }
        }
    }

    // -----------------------------------------------
    // MÉTODO PARA MOSTRAR MENSAJES PERSONALIZADOS
    // -----------------------------------------------
    private static void mostrarMensajePersonalizado(String mensaje, String titulo, Color colorFondo) {
        JPanel panel = new JPanel(new BorderLayout());
        // Espaciado interno del mensaje (ajustar según necesidad)
        panel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));
        panel.setBackground(colorFondo); // Color de fondo recibido como parámetro
        
        // Área de texto para el mensaje (permite múltiples líneas)
        JTextArea textArea = new JTextArea(mensaje);
        textArea.setFont(new Font("Segoe UI", Font.PLAIN, 14)); // Fuente del mensaje
        textArea.setEditable(false); // No editable
        textArea.setBackground(colorFondo); // Mismo color que el panel
        textArea.setLineWrap(true); // Ajuste de líneas automático
        textArea.setWrapStyleWord(true); // Ajuste por palabras completas
        textArea.setAlignmentX(JTextArea.CENTER_ALIGNMENT); // Centrado horizontal
        
        panel.add(textArea, BorderLayout.CENTER);
        
        // Mostrar el diálogo
        JOptionPane.showMessageDialog(
            null, // Componente padre
            panel, // Panel personalizado
            titulo, // Título de la ventana
            JOptionPane.PLAIN_MESSAGE // Sin icono
        );
    }
}