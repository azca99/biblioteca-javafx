package com.daniel.dao;

import com.daniel.db.DB;
import com.daniel.model.Socio;
import com.daniel.util.AppLog;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SocioDAO {

    // READ: socios de la BD
    public List<Socio> findAll() {
        String sql = "SELECT id_socio, dni, nombre, apellidos, email, telefono, activo, fecha_alta, direccion, codigo_postal FROM socios"; // Consulta SQL
        List<Socio> socios = new ArrayList<>();

        try (Connection con = DB.getConnection(); // Conexión con BD
             PreparedStatement ps = con.prepareStatement(sql); // Prepara la consulta
             ResultSet rs = ps.executeQuery()) { // Ejecuta y obtiene resultados

            while (rs.next()) { // Mientras rs recorre las filas
                Socio s = new Socio(); // Nuevo socio

                // Setters socio rellenado con info de la BD
                s.setIdSocio(rs.getInt("id_socio"));
                s.setDni(rs.getString("dni"));
                s.setNombre(rs.getString("nombre"));
                s.setApellidos(rs.getString("apellidos"));
                s.setEmail(rs.getString("email"));
                s.setTelefono(rs.getString("telefono"));
                s.setActivo(rs.getBoolean("activo"));
                // Pasar fecha SQL a LocalDate
                Date fechaSql = rs.getDate("fecha_alta");
                if (fechaSql != null) {
                    LocalDate fechaAlta = fechaSql.toLocalDate();
                    s.setFechaAlta(fechaAlta);
                }
                s.setDireccion(rs.getString("direccion"));
                s.setCodigoPostal(rs.getString("codigo_postal"));

                // Añadir socio a la lista
                socios.add(s);
            }
        // Manejo de errores
        } catch (SQLException e) {
            System.err.println("Error al leer socios");
            e.printStackTrace();
        }

        return socios;
    }

    // CREATE socio: devuelve id_socio o -1 (error)
    public int insert(Socio s) {

        // Validaciones
        if (s == null) return -1;
        if (s.getDni() == null || s.getDni().trim().isEmpty()) return -1;
        if (s.getNombre() == null || s.getNombre().trim().isEmpty()) return -1;
        if (s.getApellidos() == null || s.getApellidos().trim().isEmpty()) return -1;
        if (s.getDireccion() == null || s.getDireccion().trim().isEmpty()) return -1;
        if (s.getCodigoPostal() == null || s.getCodigoPostal().trim().isEmpty()) return -1;

        // SQL para el PreparedStatement
        String sql = """
        INSERT INTO socios (dni, nombre, apellidos, email, telefono, activo, fecha_alta, direccion, codigo_postal)
        VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

        // Probar la conexión
        try (Connection con = DB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) { // Preparar INSERT y pedir de vuelta el ID generado

            // Rellenar los ? en orden
            ps.setString(1, s.getDni());
            ps.setString(2, s.getNombre());
            ps.setString(3, s.getApellidos());
            ps.setString(4, s.getEmail());
            ps.setString(5, s.getTelefono());
            ps.setBoolean(6, s.isActivo());
            // LocalDate -> fecha sql
            if (s.getFechaAlta() != null) {
                ps.setDate(7, java.sql.Date.valueOf(s.getFechaAlta()));
            } else {
                ps.setDate(7, null);
            }
            ps.setString(8, s.getDireccion());
            ps.setString(9, s.getCodigoPostal());

            // Ejecutar sentencia INSERT en SQL
            int filas = ps.executeUpdate();

            // Recuperar el id generado (AUTO_INCREMENT) si se generó el socio
            if (filas == 1) {
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) {
                        return keys.getInt(1); // id_socio generado
                    }
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al insertar socio");
            AppLog.error(
                    "SOCIOS/INSERT",
                    "Error al insertar socio (DNI=" + (s != null ? s.getDni() : "null") +
                            ", Email=" + (s != null ? s.getEmail() : "null") + "): " + e.getMessage()
            );
            e.printStackTrace();
        }

        return -1;
    }

    // DELETE socio por id_socio
    public boolean deleteById(int idSocio) {

        String sql = "DELETE FROM socios WHERE id_socio = ?";

        try (Connection con = DB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idSocio);

            int filas = ps.executeUpdate();

            return filas == 1; // true si borró 1 fila, false si no encontró ese id

        } catch (SQLException e) {
            System.err.println("Error al borrar socio con id " + idSocio);
            AppLog.error(
                    "SOCIOS/DELETE",
                    "Error al borrar socio (ID=" + idSocio + "): " + e.getMessage()
            );
            e.printStackTrace();
            return false;
        }
    }

    // UPDATE socio
    public boolean update(Socio s) {

        // Validaciones mínimas
        if (s == null) return false;
        if (s.getIdSocio() <= 0) return false; // sin id no sabemos qué fila actualizar

        String sql = """
        UPDATE socios
        SET dni = ?, nombre = ?, apellidos = ?, email = ?, telefono = ?, activo = ?, fecha_alta = ?, direccion = ?, codigo_postal = ?
        WHERE id_socio = ?
        """;

        try (Connection con = DB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            // Rellenar los ? del SET (en el mismo orden)
            ps.setString(1, s.getDni());
            ps.setString(2, s.getNombre());
            ps.setString(3, s.getApellidos());
            ps.setString(4, s.getEmail());
            ps.setString(5, s.getTelefono());
            ps.setBoolean(6, s.isActivo());
            // Fecha (LocalDate -> java.sql.Date)
            if (s.getFechaAlta() != null) {
                ps.setDate(7, java.sql.Date.valueOf(s.getFechaAlta()));
            } else {
                ps.setDate(7, null);
            }
            ps.setString(8, s.getDireccion());
            ps.setString(9, s.getCodigoPostal());
            // El último ? es el id del WHERE
            ps.setInt(10, s.getIdSocio());

            // Ejecutar UPDATE
            int filas = ps.executeUpdate();

            // Devuelve true si actualizó 1 fila
            return filas == 1;

        } catch (SQLException e) {
            System.err.println("Error al actualizar socio con id " + (s != null ? s.getIdSocio() : "?"));
            AppLog.error(
                    "SOCIOS/UPDATE",
                    "Error al actualizar socio (ID=" + (s != null ? s.getIdSocio() : "null") +
                            ", DNI=" + (s != null ? s.getDni() : "null") + "): " + e.getMessage()
            );
            e.printStackTrace();
            return false;
        }
    }

    // Métode búsqueda
    public List<Socio> findByFilters(String dni, String textoNombreApellidos) {

        StringBuilder sql = new StringBuilder("""
        SELECT id_socio, dni, nombre, apellidos, email, telefono, activo, fecha_alta, direccion, codigo_postal
        FROM socios
        WHERE 1=1
        """);

        List<Object> params = new ArrayList<>();

        // Filtro 1: DNI (parcial)
        if (dni != null && !dni.isBlank()) {
            sql.append(" AND dni LIKE ?");
            params.add("%" + dni.trim() + "%");
        }

        // Filtro 2: Nombre o Apellidos (parcial)
        if (textoNombreApellidos != null && !textoNombreApellidos.isBlank()) {
            sql.append(" AND (nombre LIKE ? OR apellidos LIKE ?)");
            String t = "%" + textoNombreApellidos.trim() + "%";
            params.add(t);
            params.add(t);
        }

        sql.append(" ORDER BY id_socio");

        List<Socio> out = new ArrayList<>();

        try (Connection con = DB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql.toString())) {

            // Rellenar los ? en el mismo orden en que los añadimos
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Socio s = new Socio();
                    s.setIdSocio(rs.getInt("id_socio"));
                    s.setDni(rs.getString("dni"));
                    s.setNombre(rs.getString("nombre"));
                    s.setApellidos(rs.getString("apellidos"));
                    s.setEmail(rs.getString("email"));
                    s.setTelefono(rs.getString("telefono"));
                    s.setActivo(rs.getBoolean("activo"));
                    s.setDireccion(rs.getString("direccion"));
                    s.setCodigoPostal(rs.getString("codigo_postal"));

                    java.sql.Date fecha = rs.getDate("fecha_alta");
                    if (fecha != null) {
                        s.setFechaAlta(fecha.toLocalDate());
                    }

                    out.add(s);
                }
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return out;

    }
}

