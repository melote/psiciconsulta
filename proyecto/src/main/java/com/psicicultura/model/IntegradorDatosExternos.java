package com.psicicultura.model;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import java.sql.Connection;
import java.sql.PreparedStatement;
import com.psicicultura.util.Conexion;

public class IntegradorDatosExternos {

    private static final OkHttpClient client = new OkHttpClient();

    /**
     * Integrar datos desde la API de Captura Pesquera 2020 (Producción y Ventas).
     */
    public static void integrarCapturaPesquera2020() {
        String baseUrl = "https://www.datos.gov.co/resource/8n9b-k5iz.json";
        String fuente = "datos.gov.co - Captura Pesquera 2020";
        int limit = 1000;
        int offset = 0;
        boolean hayDatos = true;

        while (hayDatos) {
            String url = baseUrl + "?$limit=" + limit + "&$offset=" + offset;
            Request request = new Request.Builder().url(url).build();

            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    System.out.println("Error en solicitud: " + response);
                    break;
                }

                String jsonData = response.body().string();
                JsonArray registros = JsonParser.parseString(jsonData).getAsJsonArray();

                if (registros.size() == 0) {
                    hayDatos = false;
                } else {
                    for (int i = 0; i < registros.size(); i++) {
                        JsonObject obj = registros.get(i).getAsJsonObject();

                        String especie = obj.has("especie") ? obj.get("especie").getAsString() : null;
                        double pesoKg = obj.has("peso_kg") ? obj.get("peso_kg").getAsDouble() : 0.0;
                        double valorDesembarcado = obj.has("valor_desembarcado") ? obj.get("valor_desembarcado").getAsDouble() : 0.0;

                        if (especie != null) {
                            // Producción: peso capturado
                            guardarEnBD("produccion", especie, pesoKg, "Captura de producción", fuente);

                            // Ventas: valor capturado
                            guardarEnBD("ventas", especie, valorDesembarcado, "Captura de ventas", fuente);
                        }
                    }
                    offset += limit;
                }

            } catch (Exception e) {
                System.out.println("Error al integrar datos: " + e.getMessage());
                hayDatos = false;
            }
        }
    }

    /*Datos Gastos o costos de avituallamiento 2020*/
    public static void integrarVentasGastosAcuicolas() {
        String baseUrl = "https://www.datos.gov.co/resource/7swi-ievq.json";
        String fuente = "datos.gov.co - Costos Avituallamiento 2020";
        int limit = 1000;
    int offset = 0;
   boolean hayDatos = true;

    OkHttpClient client = new OkHttpClient();

    while (hayDatos) {
        String url = baseUrl + "?$limit=" + limit + "&$offset=" + offset;
        Request request = new Request.Builder().url(url).build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                System.out.println(" Error al obtener datos: " + response);
                break;
            }

            String jsonData = response.body().string();
            JsonArray registros = JsonParser.parseString(jsonData).getAsJsonArray();

            if (registros.size() == 0) {
                hayDatos = false;
            } else {
                for (int i = 0; i < registros.size(); i++) {
                    JsonObject obj = registros.get(i).getAsJsonObject();

                    String codigo = obj.has("c_digo") ? obj.get("c_digo").getAsString() : null;
                    double avituallamiento = obj.has("avituallamiento") ? obj.get("avituallamiento").getAsDouble() : 0.0;

                    if (codigo != null && avituallamiento > 0) {
                        guardarEnBD("ventas", codigo, avituallamiento, "Gasto en avituallamiento", fuente);
                    }
                }
                offset += limit;
            }

        } catch (Exception e) {
            System.out.println(" Error al integrar datos: " + e.getMessage());
            hayDatos = false;
        }
    }
}
  

    /**
     * Método común para guardar datos en la base de datos.
     */
    private static void guardarEnBD(String tipo, String nombreVariable, double valor, String observacion, String fuente) {
        if (valor <= 0) return; // Ignorar registros inválidos

        try (Connection conn = Conexion.getConexion()) {

            // Insertar Variable si no existe
            String insertVariable = "INSERT INTO variable (tipo, nombre) VALUES (?, ?) ON CONFLICT (nombre) DO NOTHING";
            try (PreparedStatement stmt = conn.prepareStatement(insertVariable)) {
                stmt.setString(1, tipo);
                stmt.setString(2, nombreVariable);
                stmt.executeUpdate();
            }

            // Insertar Registro
            String insertRegistro = "INSERT INTO registro (variable_id, valor, observacion, fuente, fecha, municipio_id, usuario_id) " +
                                     "SELECT id, ?, ?, ?, current_date, 1, 1 FROM variable WHERE nombre = ?";
            try (PreparedStatement stmt = conn.prepareStatement(insertRegistro)) {
                stmt.setDouble(1, valor);
                stmt.setString(2, observacion);
                stmt.setString(3, fuente);
                stmt.setString(4, nombreVariable);
                stmt.executeUpdate();
            }

        } catch (Exception e) {
            System.out.println("Error al guardar en BD: " + e.getMessage());
        }
    }
}
