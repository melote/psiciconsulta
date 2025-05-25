package com.psicicultura;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import com.psicicultura.views.MenuLoginView;
import com.formdev.flatlaf.FlatLightLaf;



public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                // Configurar FlatLaf como Look and Feel
                UIManager.setLookAndFeel(new FlatLightLaf());

                MenuLoginView ventana = new MenuLoginView();
                ventana.setVisible(true); // ✅ Muestra tu GUI personalizada
                System.out.println("Ventana personalizada iniciada.");
            } catch (Exception e) {
                System.err.println("Error al iniciar MenuLoginView:");
                e.printStackTrace();
            }
        });
    }
}



        // FUNCION PARA COMPROBAR LA CONEXION A LA BASE DE DATOS
        /* 
        try {
            Connection connection = Conexion.getConexion();
            if (connection != null) {
                System.out.println("Conexión exitosa a la base de datos.");
            } else {
                System.out.println("Error: no se pudo establecer la conexión.");
            }
        } catch (SQLException e) {
            System.out.println("Error al conectar a la base de datos: " + e.getMessage());
        } 
        */
        
    