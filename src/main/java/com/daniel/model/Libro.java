package com.daniel.model;

import java.time.LocalDate;

public class Libro {

    // Atributos de Libro
    private int idLibro;
    private String titulo;
    private LocalDate fechaPublicacion;
    private String idioma;          // en BD es ENUM, aquí String
    private int numeroPaginas;
    private String isbn;
    private boolean disponible;
    private int idEditorial;
    private int idCategoria;
    private String editorialNombre;   // <-- NUEVO
    private String categoriaNombre;

    // Constructor vacío (crea un libro “en blanco”)
    public Libro() {
    }

    // Constructor completo (crea un libro con todos sus datos)
    public Libro(int idLibro, String titulo, LocalDate fechaPublicacion, String idioma,
                 int numeroPaginas, String isbn, boolean disponible, int idEditorial, int idCategoria) {
        this.idLibro = idLibro;
        this.titulo = titulo;
        this.fechaPublicacion = fechaPublicacion;
        this.idioma = idioma;
        this.numeroPaginas = numeroPaginas;
        this.isbn = isbn;
        this.disponible = disponible;
        this.idEditorial = idEditorial;
        this.idCategoria = idCategoria;
    }

    // Getters y Setters
    public int getIdLibro() {
        return idLibro;
    }
    public void setIdLibro(int idLibro) {
        this.idLibro = idLibro;
    }

    public String getTitulo() {
        return titulo;
    }
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public LocalDate getFechaPublicacion() {
        return fechaPublicacion;
    }
    public void setFechaPublicacion(LocalDate fechaPublicacion) {
        this.fechaPublicacion = fechaPublicacion;
    }

    public String getIdioma() {
        return idioma;
    }
    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }

    public int getNumeroPaginas() {
        return numeroPaginas;
    }
    public void setNumeroPaginas(int numeroPaginas) {
        this.numeroPaginas = numeroPaginas;
    }

    public String getIsbn() {
        return isbn;
    }
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public boolean isDisponible() {
        return disponible;
    }
    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

    public int getIdEditorial() {
        return idEditorial;
    }
    public void setIdEditorial(int idEditorial) {
        this.idEditorial = idEditorial;
    }

    public int getIdCategoria() {
        return idCategoria;
    }
    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }

    //Getters y setters para mostrar el nombre y no el id de Editorial y Categoría
    public String getEditorialNombre() { return editorialNombre; }
    public void setEditorialNombre(String editorialNombre) { this.editorialNombre = editorialNombre; }

    public String getCategoriaNombre() { return categoriaNombre; }
    public void setCategoriaNombre(String categoriaNombre) { this.categoriaNombre = categoriaNombre; }

    // Imprimir un libro fácil en consola
    @Override
    public String toString() {
        return "Libro{" +
                "idLibro=" + idLibro +
                ", titulo='" + titulo + '\'' +
                ", fechaPublicacion=" + fechaPublicacion +
                ", idioma='" + idioma + '\'' +
                ", numeroPaginas=" + numeroPaginas +
                ", isbn='" + isbn + '\'' +
                ", disponible=" + disponible +
                ", idEditorial=" + idEditorial +
                ", editorialNombre='" + editorialNombre + '\'' +
                ", idCategoria=" + idCategoria +
                ", categoriaNombre='" + categoriaNombre + '\'' +
                '}';
    }
}
