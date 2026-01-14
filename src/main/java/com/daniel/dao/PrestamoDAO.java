package com.daniel.dao;

import com.daniel.db.DB;
import com.daniel.model.Prestamo;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PrestamoDAO {

    // READ: préstamos con nombre socio y título libro (1 libro max)
    public List<Prestamo> findAll() {

        String sql = """
            SELECT
              p.id_prestamo,
              p.estado,
              p.fecha_prestamo,
              p.fecha_prevista_devolucion,
              p.fecha_devolucion,
              p.penalizacion_euros,
              p.observaciones,
              p.id_socio,
              CONCAT(s.nombre, ' ', s.apellidos) AS socio_nombre,
              pl.id_libro,
              l.titulo AS libro_titulo
            FROM prestamos p
            JOIN socios s ON s.id_socio = p.id_socio
            LEFT JOIN prestamos_libro pl ON pl.id_prestamo = p.id_prestamo
            LEFT JOIN libros l ON l.id_libro = pl.id_libro
            ORDER BY p.id_prestamo DESC
        """;
        List<Prestamo> lista = new ArrayList<>();

        try (Connection con = DB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) { // Mientras rs recorre las filas
                Prestamo p = new Prestamo();

                // Setters prestamo rellenado con info de la BD
                p.setIdPrestamo(rs.getInt("id_prestamo"));
                p.setEstado(rs.getString("estado"));

                Date fp = rs.getDate("fecha_prestamo");
                p.setFechaPrestamo(fp != null ? fp.toLocalDate() : null);

                Date fprev = rs.getDate("fecha_prevista_devolucion");
                p.setFechaPrevistaDevolucion(fprev != null ? fprev.toLocalDate() : null);

                Date fdev = rs.getDate("fecha_devolucion");
                p.setFechaDevolucion(fdev != null ? fdev.toLocalDate() : null);

                BigDecimal pen = rs.getBigDecimal("penalizacion_euros");
                p.setPenalizacionEuros(pen != null ? pen : BigDecimal.ZERO);

                p.setObservaciones(rs.getString("observaciones"));

                p.setIdSocio(rs.getInt("id_socio"));
                p.setSocioNombreCompleto(rs.getString("socio_nombre"));

                p.setIdLibro(rs.getInt("id_libro")); // si no hay, será 0
                p.setTituloLibro(rs.getString("libro_titulo"));

                lista.add(p); //Añadir préstamo a la lista
            }

        } catch (SQLException e) {
            System.err.println("Error al leer préstamos");
            e.printStackTrace();
        }

        return lista;
    }

    // CREATE prestamo: devuelve id_prestamo o -1
    // Inserta en prestamos + prestamos_libro (transacción)
    public int insert(Prestamo p) {

        // Validaciones
        if (p == null) return -1;
        if (p.getIdSocio() <= 0) return -1;
        if (p.getIdLibro() <= 0) return -1;
        // Obligatorios: fecha_prevista_devolucion (en tu SQL es NOT NULL)
        if (p.getFechaPrevistaDevolucion() == null) return -1;

        // SQL para el PreparedStatement
        String sqlPrestamo = """
            INSERT INTO prestamos (estado, fecha_prestamo, fecha_prevista_devolucion, fecha_devolucion,
                                  penalizacion_euros, observaciones, id_socio)
            VALUES (?, ?, ?, ?, ?, ?, ?)
        """;

        String sqlPrestamoLibro = """
            INSERT INTO prestamos_libro (id_prestamo, id_libro)
            VALUES (?, ?)
        """;

        // Probar conexión
        try (Connection con = DB.getConnection()) {

            con.setAutoCommit(false); // Transacción prestamos y prestamos_libro

            int idGenerado;

            // Tabla prestamos
            try (PreparedStatement ps = con.prepareStatement(sqlPrestamo, Statement.RETURN_GENERATED_KEYS)) {

                // Rellenar parámetros
                ps.setString(1, (p.getEstado() == null || p.getEstado().isBlank()) ? "ABIERTO" : p.getEstado());
                // Si no te pasan fecha_prestamo, usamos hoy (tu BD ya lo hace por defecto, pero así lo controlas tú)
                LocalDate fechaPrestamo = (p.getFechaPrestamo() != null) ? p.getFechaPrestamo() : LocalDate.now();
                //Convertir LocalDate a SQL Date
                ps.setDate(2, Date.valueOf(fechaPrestamo));
                ps.setDate(3, Date.valueOf(p.getFechaPrevistaDevolucion()));
                if (p.getFechaDevolucion() != null) ps.setDate(4, Date.valueOf(p.getFechaDevolucion()));
                else ps.setNull(4, Types.DATE);
                BigDecimal pen = (p.getPenalizacionEuros() != null) ? p.getPenalizacionEuros() : BigDecimal.ZERO;
                ps.setBigDecimal(5, pen);
                ps.setString(6, p.getObservaciones());
                ps.setInt(7, p.getIdSocio());

                // Ejecutar INSERT
                int filas = ps.executeUpdate();
                if (filas != 1) {
                    con.rollback();
                    return -1;
                }

                // Obtener el idGenerado
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (!keys.next()) {
                        con.rollback();
                        return -1;
                    }
                    idGenerado = keys.getInt(1);
                }
            }

            // insertar relación préstamo-libro
            try (PreparedStatement ps2 = con.prepareStatement(sqlPrestamoLibro)) { // Misma conexión
                ps2.setInt(1, idGenerado);
                ps2.setInt(2, p.getIdLibro());

                // Ejecutar INSERT
                int filas2 = ps2.executeUpdate();
                if (filas2 != 1) {
                    con.rollback();
                    return -1;
                }
            }

            // Confirmar transacción
            con.commit();
            return idGenerado;

        } catch (SQLException e) {
            System.err.println("Error al insertar préstamo");
            e.printStackTrace();
            return -1;
        }
    }

    // UPDATE: actualiza datos del préstamo (no toca la tabla intermedia)
    public boolean update(Prestamo p) {

        // Validaciones iniciales
        if (p == null) return false;
        if (p.getIdPrestamo() <= 0) return false;
        if (p.getIdSocio() <= 0) return false;
        if (p.getFechaPrevistaDevolucion() == null) return false;

        String sql = """
            UPDATE prestamos
            SET estado = ?, fecha_prestamo = ?, fecha_prevista_devolucion = ?, fecha_devolucion = ?,
                penalizacion_euros = ?, observaciones = ?, id_socio = ?
            WHERE id_prestamo = ?
        """;

        try (Connection con = DB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            // Rellenar parámetros
            ps.setString(1, (p.getEstado() == null || p.getEstado().isBlank()) ? "ABIERTO" : p.getEstado());
            LocalDate fechaPrestamo = (p.getFechaPrestamo() != null) ? p.getFechaPrestamo() : LocalDate.now();
            ps.setDate(2, Date.valueOf(fechaPrestamo));
            ps.setDate(3, Date.valueOf(p.getFechaPrevistaDevolucion()));
            if (p.getFechaDevolucion() != null) ps.setDate(4, Date.valueOf(p.getFechaDevolucion()));
            else ps.setNull(4, Types.DATE);
            BigDecimal pen = (p.getPenalizacionEuros() != null) ? p.getPenalizacionEuros() : BigDecimal.ZERO;
            ps.setBigDecimal(5, pen);
            ps.setString(6, p.getObservaciones());
            ps.setInt(7, p.getIdSocio());
            ps.setInt(8, p.getIdPrestamo());

            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
            System.err.println("Error al actualizar préstamo");
            e.printStackTrace();
            return false;
        }
    }

    // DELETE: borra préstamo (la tabla intermedia cae en cascada)
    public boolean deleteById(int idPrestamo) {

        String sql = "DELETE FROM prestamos WHERE id_prestamo = ?";

        try (Connection con = DB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idPrestamo);
            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
            System.err.println("Error al borrar préstamo");
            e.printStackTrace();
            return false;
        }
    }

    // Métode búsqueda
    public List<Prestamo> findByFilters(String textoSocio, String textoLibro) {

        StringBuilder sql = new StringBuilder("""
        SELECT
          p.id_prestamo,
          p.estado,
          p.fecha_prestamo,
          p.fecha_prevista_devolucion,
          p.fecha_devolucion,
          p.penalizacion_euros,
          p.observaciones,
          p.id_socio,
          CONCAT(s.nombre, ' ', s.apellidos) AS socio_nombre,
          pl.id_libro,
          l.titulo AS libro_titulo
        FROM prestamos p
        JOIN socios s ON s.id_socio = p.id_socio
        LEFT JOIN prestamos_libro pl ON pl.id_prestamo = p.id_prestamo
        LEFT JOIN libros l ON l.id_libro = pl.id_libro
        WHERE 1=1
        """);

        List<Object> params = new ArrayList<>();

        // Filtro 1: socio (nombre o apellidos)
        if (textoSocio != null && !textoSocio.isBlank()) {
            sql.append(" AND (s.nombre LIKE ? OR s.apellidos LIKE ?)");
            String t = "%" + textoSocio.trim() + "%";
            params.add(t);
            params.add(t);
        }

        // Filtro 2: libro (título)
        if (textoLibro != null && !textoLibro.isBlank()) {
            sql.append(" AND l.titulo LIKE ?");
            params.add("%" + textoLibro.trim() + "%");
        }

        sql.append(" ORDER BY p.id_prestamo DESC");

        List<Prestamo> lista = new ArrayList<>();

        try (Connection con = DB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Prestamo p = new Prestamo();

                    p.setIdPrestamo(rs.getInt("id_prestamo"));
                    p.setEstado(rs.getString("estado"));

                    Date fp = rs.getDate("fecha_prestamo");
                    p.setFechaPrestamo(fp != null ? fp.toLocalDate() : null);

                    Date fprev = rs.getDate("fecha_prevista_devolucion");
                    p.setFechaPrevistaDevolucion(fprev != null ? fprev.toLocalDate() : null);

                    Date fdev = rs.getDate("fecha_devolucion");
                    p.setFechaDevolucion(fdev != null ? fdev.toLocalDate() : null);

                    BigDecimal pen = rs.getBigDecimal("penalizacion_euros");
                    p.setPenalizacionEuros(pen != null ? pen : BigDecimal.ZERO);

                    p.setObservaciones(rs.getString("observaciones"));

                    p.setIdSocio(rs.getInt("id_socio"));
                    p.setSocioNombreCompleto(rs.getString("socio_nombre"));

                    p.setIdLibro(rs.getInt("id_libro"));
                    p.setTituloLibro(rs.getString("libro_titulo"));

                    lista.add(p);
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al buscar préstamos");
            e.printStackTrace();
        }

        return lista;
    }

}