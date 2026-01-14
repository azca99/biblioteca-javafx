package com.daniel.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Prestamo {

    // Atributos de prestamo
    private int idPrestamo;
    private String estado; // ENUM('ABIERTO','DEVUELTO','ATRASADO') -> String
    private LocalDate fechaPrestamo;
    private LocalDate fechaPrevistaDevolucion;
    private LocalDate fechaDevolucion; // puede ser null
    private BigDecimal penalizacionEuros;
    private String observaciones;
    private int idSocio;
    private String socioNombreCompleto;
    // Para simplificar la app: 1 libro por préstamo (aunque en BD sea N:M)
    private int idLibro;
    private String tituloLibro;

    // Constructor vacío
    public Prestamo() {
    }

    // Constructor completo (crea un libro con todos sus datos)
    public Prestamo(int idPrestamo, String estado, LocalDate fechaPrestamo, LocalDate fechaPrevistaDevolucion,
                    LocalDate fechaDevolucion, BigDecimal penalizacionEuros, String observaciones,
                    int idSocio, int idLibro) {
        this.idPrestamo = idPrestamo;
        this.estado = estado;
        this.fechaPrestamo = fechaPrestamo;
        this.fechaPrevistaDevolucion = fechaPrevistaDevolucion;
        this.fechaDevolucion = fechaDevolucion;
        this.penalizacionEuros = penalizacionEuros;
        this.observaciones = observaciones;
        this.idSocio = idSocio;
        this.idLibro = idLibro;
    }

    // Getters and setters
    public int getIdPrestamo() { return idPrestamo; }
    public void setIdPrestamo(int idPrestamo) { this.idPrestamo = idPrestamo; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public LocalDate getFechaPrestamo() { return fechaPrestamo; }
    public void setFechaPrestamo(LocalDate fechaPrestamo) { this.fechaPrestamo = fechaPrestamo; }

    public LocalDate getFechaPrevistaDevolucion() { return fechaPrevistaDevolucion; }
    public void setFechaPrevistaDevolucion(LocalDate fechaPrevistaDevolucion) { this.fechaPrevistaDevolucion = fechaPrevistaDevolucion; }

    public LocalDate getFechaDevolucion() { return fechaDevolucion; }
    public void setFechaDevolucion(LocalDate fechaDevolucion) { this.fechaDevolucion = fechaDevolucion; }

    public BigDecimal getPenalizacionEuros() { return penalizacionEuros; }
    public void setPenalizacionEuros(BigDecimal penalizacionEuros) { this.penalizacionEuros = penalizacionEuros; }

    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }

    public int getIdSocio() { return idSocio; }
    public void setIdSocio(int idSocio) { this.idSocio = idSocio; }

    public String getSocioNombreCompleto() { return socioNombreCompleto; }
    public void setSocioNombreCompleto(String socioNombreCompleto) { this.socioNombreCompleto = socioNombreCompleto; }

    public int getIdLibro() { return idLibro; }
    public void setIdLibro(int idLibro) { this.idLibro = idLibro; }

    public String getTituloLibro() { return tituloLibro; }
    public void setTituloLibro(String tituloLibro) { this.tituloLibro = tituloLibro; }

    // Imprimir un préstamo fácil en consola
    @Override
    public String toString() {
        return "Prestamo{" +
                "idPrestamo=" + idPrestamo +
                ", estado='" + estado + '\'' +
                ", fechaPrestamo=" + fechaPrestamo +
                ", fechaPrevistaDevolucion=" + fechaPrevistaDevolucion +
                ", fechaDevolucion=" + fechaDevolucion +
                ", penalizacionEuros=" + penalizacionEuros +
                ", idSocio=" + idSocio +
                ", idLibro=" + idLibro +
                '}';
    }
}
