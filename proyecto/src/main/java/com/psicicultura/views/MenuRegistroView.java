package com.psicicultura.views;
import java.awt.GridLayout;
import javax.swing.*;

import com.psicicultura.service.RegistradorUsuario;

public class MenuRegistroView {

    public static void mostrarMenuRegistroGUI() {
        JPanel panel = new JPanel(new GridLayout(4, 2));
        JTextField txtNombre = new JTextField();
        JTextField txtCorreo = new JTextField();
        JPasswordField txtContrasena = new JPasswordField();
        JPasswordField txtRepetirContrasena = new JPasswordField();

        panel.add(new JLabel("Nombre:"));
        panel.add(txtNombre);
        panel.add(new JLabel("Correo:"));
        panel.add(txtCorreo);
        panel.add(new JLabel("Contraseña:"));
        panel.add(txtContrasena);
        panel.add(new JLabel("Repetir contraseña:"));
        panel.add(txtRepetirContrasena);

        int opcion = JOptionPane.showConfirmDialog(
            null, panel, "Registro de Piscicultor", 
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE
        );

        if (opcion == JOptionPane.OK_OPTION) {
            String nombre = txtNombre.getText().trim();
            String correo = txtCorreo.getText().trim();
            String contrasena = new String(txtContrasena.getPassword());
            String repetirContrasena = new String(txtRepetirContrasena.getPassword());

            // Validaciones
            if (nombre.isEmpty() || correo.isEmpty() || contrasena.isEmpty()) {
                JOptionPane.showMessageDialog(null, "⚠️ Todos los campos son obligatorios.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!contrasena.equals(repetirContrasena)) {
                JOptionPane.showMessageDialog(null, "❌ Las contraseñas no coinciden.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Llamada a TU clase RegistradorUsuario
            boolean exito = RegistradorUsuario.registrar(nombre, correo, contrasena);

            if (exito) {
                JOptionPane.showMessageDialog(null, "✅ Registro exitoso. Ahora puedes iniciar sesión.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "❌ Error al registrar. ¿El correo ya existe?", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
