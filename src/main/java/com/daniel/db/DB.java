package com.daniel.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DB {

    // 1) Datos de conexión (los ajustaremos a tu MariaDB)
    private static final String URL  = "jdbc:mariadb://localhost:3306/biblioteca_db";
    private static final String USER = "root";
    private static final String PASS = "Barcelona99)";

    // 2) Este métode devuelve una conexión lista para usar
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }

    // 3) Prueba simple: consulta a la BD y muestra un resultado por consola
    public static void testConnection() {
        String sql = "SELECT COUNT(*) AS total FROM socios";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            rs.next(); // mueve el cursor a la primera fila del resultado
            System.out.println("Conexión OK. Total socios = " + rs.getInt("total"));

        } catch (SQLException e) {
            System.err.println("Error conectando o consultando la BD");
            e.printStackTrace();
        }
    }
}