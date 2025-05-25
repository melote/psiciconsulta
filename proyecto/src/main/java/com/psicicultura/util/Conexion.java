package com.psicicultura.util;

//package com.mycompany.app.proyectopiscicultura;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {
    private static final String URL = "jdbc:postgresql://localhost:5432/repositrio_piscicultura";
    private static final String USER = "postgres"; 
    private static final String PASSWORD = "Dalcagu1";

    public static Connection getConexion() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}