package com.psicicultura.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.psicicultura.util.Conexion;

public class Autenticador {

    public static String autenticar(String correo, String contrasena) {
        String sql = "SELECT rol FROM Usuario WHERE correo = ? AND contrasena = ?";

        try (Connection conn = Conexion.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Verificar que el correo y la contraseña sean correctos
            System.out.println("Correo recibido: " + correo);
            System.out.println("Contraseña recibida: " + contrasena);

            // Hashear la contraseña recibida
            String hashContrasena = HashUtil.sha256(contrasena);
            System.out.println("Contraseña hasheada: " + hashContrasena);  // Verifica el hash generado

            // Preparar la consulta SQL
            stmt.setString(1, correo);
            stmt.setString(2, hashContrasena);

            // Ejecutar la consulta
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String rol = rs.getString("rol");
                System.out.println("Rol encontrado: " + rol); // Verifica el rol encontrado
                return rol; // Retorna el rol si la autenticación es correcta
            } else {
                System.out.println("No se encontró el usuario o la contraseña no coincide.");
                return null; // Falló la autenticación
            }

        } catch (Exception e) {
            System.err.println("Error al autenticar: " + e.getMessage());
            return null;
        }
    }
}
