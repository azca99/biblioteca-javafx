package com.daniel.dao;

import com.daniel.db.DB;
import com.daniel.model.Libro;
import com.daniel.util.AppLog;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LibroDAO {

    // READ: libros de la BD
    public List<Libro> findAll() {
        String sql = """
                SELECT l.id_libro, l.titulo, l.fecha_publicacion, l.idioma, l.numero_paginas,
                        l.isbn, l.disponible, l.id_editorial, l.id_categoria,
                        e.nombre AS editorial_nombre,
                        c.nombre AS categoria_nombre
                FROM libros l
                LEFT JOIN editoriales e ON e.id_editorial = l.id_editorial
                LEFT JOIN categorias  c ON c.id_categoria  = l.id_categoria
                """;

        List<Libro> libros = new ArrayList<>();

        try (Connection con = DB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) { // rs recorre las filas
                Libro l = new Libro();

                l.setIdLibro(rs.getInt("id_libro"));
                l.setTitulo(rs.getString("titulo"));
                l.setEditorialNombre(rs.getString("editorial_nombre"));
                l.setCategoriaNombre(rs.getString("categoria_nombre"));

                // Pasar fecha SQL a LocalDate
                Date fechaSql = rs.getDate("fecha_publicacion");
                if (fechaSql != null) {
                    l.setFechaPublicacion(fechaSql.toLocalDate());
                }

                l.setIdioma(rs.getString("idioma"));
                l.setNumeroPaginas(rs.getInt("numero_paginas"));
                l.setIsbn(rs.getString("isbn"));
                l.setDisponible(rs.getBoolean("disponible"));
                l.setIdEditorial(rs.getInt("id_editorial"));
                l.setIdCategoria(rs.getInt("id_categoria"));

                libros.add(l);
            }

        } catch (SQLException e) {
            System.err.println("Error al leer libros");
            e.printStackTrace();
        }

        return libros;
    }

    // CREATE libro: devuelve id_libro o -1 (error)
    public int insert(Libro l) {

        // Validaciones (mínimas)
        if (l == null) return -1;
        if (l.getTitulo() == null || l.getTitulo().trim().isEmpty()) return -1;

        String sql = """
                INSERT INTO libros (titulo, fecha_publicacion, idioma, numero_paginas, isbn,
                                    disponible, id_editorial, id_categoria)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection con = DB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            // 2) Rellenar los ? en orden
            ps.setString(1, l.getTitulo());

            // LocalDate -> fecha sql
            if (l.getFechaPublicacion() != null) {
                ps.setDate(2, java.sql.Date.valueOf(l.getFechaPublicacion()));
            } else {
                ps.setDate(2, null);
            }

            ps.setString(3, l.getIdioma());
            ps.setInt(4, l.getNumeroPaginas());
            ps.setString(5, l.getIsbn());
            ps.setBoolean(6, l.isDisponible());
            ps.setInt(7, l.getIdEditorial());
            ps.setInt(8, l.getIdCategoria());

            // 3) Ejecutar INSERT
            int filas = ps.executeUpdate();

            // 4) Recuperar id generado
            if (filas == 1) {
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) {
                        return keys.getInt(1); // id_libro generado
                    }
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al insertar libro");
            AppLog.error(
                    "LIBROS/INSERT",
                    "Error al insertar libro (ISBN=" + (l != null ? l.getIsbn() : "null") +
                            ", Titulo=" + (l != null ? l.getTitulo() : "null") + "): " + e.getMessage()
            );
            e.printStackTrace();
        }

        return -1;
    }

    // DELETE libro por id_libro
    public boolean deleteById(int idLibro) {

        String sql = "DELETE FROM libros WHERE id_libro = ?";

        try (Connection con = DB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idLibro);

            int filas = ps.executeUpdate();

            return filas == 1;

        } catch (SQLException e) {
            System.err.println("Error al borrar libro con id " + idLibro);
            AppLog.error(
                    "LIBROS/DELETE",
                    "Error al borrar libro (ID=" + idLibro + "): " + e.getMessage()
            );
            e.printStackTrace();
            return false;
        }
    }

    // UPDATE libro
    public boolean update(Libro l) {

        // 1) Validaciones mínimas
        if (l == null) return false;
        if (l.getIdLibro() <= 0) return false;

        String sql = """
                UPDATE libros
                SET titulo = ?, fecha_publicacion = ?, idioma = ?, numero_paginas = ?, isbn = ?,
                    disponible = ?, id_editorial = ?, id_categoria = ?
                WHERE id_libro = ?
                """;

        try (Connection con = DB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            // 2) Rellenar los ? del SET (en el mismo orden)
            ps.setString(1, l.getTitulo());

            if (l.getFechaPublicacion() != null) {
                ps.setDate(2, java.sql.Date.valueOf(l.getFechaPublicacion()));
            } else {
                ps.setDate(2, null);
            }

            ps.setString(3, l.getIdioma());
            ps.setInt(4, l.getNumeroPaginas());
            ps.setString(5, l.getIsbn());
            ps.setBoolean(6, l.isDisponible());
            ps.setInt(7, l.getIdEditorial());
            ps.setInt(8, l.getIdCategoria());

            // 4) El último ? es el id del WHERE
            ps.setInt(9, l.getIdLibro());

            // 5) Ejecutar UPDATE
            int filas = ps.executeUpdate();

            // 6) true si actualizó 1 fila
            return filas == 1;

        } catch (SQLException e) {
            System.err.println("Error al actualizar libro con id " + (l != null ? l.getIdLibro() : "?"));
            AppLog.error(
                    "LIBROS/UPDATE",
                    "Error al actualizar libro (ID=" + (l != null ? l.getIdLibro() : "null") +
                            ", ISBN=" + (l != null ? l.getIsbn() : "null") + "): " + e.getMessage()
            );
            e.printStackTrace();
            return false;
        }
    }

    // Buscar id_editorial a partir del nombre
    public Integer findEditorialIdByNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) return null;

        String sql = "SELECT id_editorial FROM editoriales WHERE nombre = ?";

        try (Connection con = DB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, nombre.trim());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt("id_editorial");
            }

        } catch (SQLException e) {
            System.err.println("Error al buscar editorial por nombre");
            e.printStackTrace();
        }

        return null;
    }

    // Buscar id_categoria a partir del nombre
    public Integer findCategoriaIdByNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) return null;

        String sql = "SELECT id_categoria FROM categorias WHERE nombre = ?";

        try (Connection con = DB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, nombre.trim());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt("id_categoria");
            }

        } catch (SQLException e) {
            System.err.println("Error al buscar categoría por nombre");
            e.printStackTrace();
        }

        return null;
    }

    // Buscar si existe ISBN en la BD
    public boolean existsIsbn(String isbn) {
        String sql = "SELECT 1 FROM libros WHERE isbn = ? LIMIT 1";

        try (Connection con = DB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, isbn);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next(); // true si encontró al menos una fila
            }

        } catch (SQLException e) {
            System.err.println("Error comprobando ISBN existente");
            e.printStackTrace();
            return false;
        }
    }

    public List<Libro> findByFilters(String titulo, String textoAutor) {

        StringBuilder sql = new StringBuilder("""
        SELECT DISTINCT
            l.id_libro,
            l.titulo,
            l.fecha_publicacion,
            l.idioma,
            l.numero_paginas,
            l.isbn,
            l.disponible,
            l.id_editorial,
            l.id_categoria
        FROM libros l
        JOIN libro_autor la ON l.id_libro = la.id_libro
        JOIN autores a ON la.id_autor = a.id_autor
        WHERE 1=1
        """);

        List<Object> params = new ArrayList<>();

        // Filtro 1: título
        if (titulo != null && !titulo.isBlank()) {
            sql.append(" AND l.titulo LIKE ?");
            params.add("%" + titulo.trim() + "%"); // añade el valor al ?
        }

        // Filtro 2: autor (nombre o apellidos)
        if (textoAutor != null && !textoAutor.isBlank()) {
            sql.append(" AND (a.nombre LIKE ? OR a.apellidos LIKE ?)");
            String t = "%" + textoAutor.trim() + "%";
            params.add(t);
            params.add(t);
        }

        sql.append(" ORDER BY l.titulo");

        List<Libro> out = new ArrayList<>();

        try (Connection con = DB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Libro l = new Libro();
                    l.setIdLibro(rs.getInt("id_libro"));
                    l.setTitulo(rs.getString("titulo"));
                    java.sql.Date fp = rs.getDate("fecha_publicacion");
                    if (fp != null) {
                        l.setFechaPublicacion(fp.toLocalDate());
                    }
                    l.setIdioma(rs.getString("idioma"));
                    l.setNumeroPaginas(rs.getInt("numero_paginas"));
                    l.setIsbn(rs.getString("isbn"));
                    l.setDisponible(rs.getBoolean("disponible"));
                    l.setIdEditorial(rs.getInt("id_editorial"));
                    l.setIdCategoria(rs.getInt("id_categoria"));
                    out.add(l);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return out;
    }

}

