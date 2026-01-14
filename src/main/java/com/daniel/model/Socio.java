package com.daniel.model;

import java.time.LocalDate;

public class Socio {

    // Atributos (datos) del socio
    private int idSocio;
    private String dni;
    private String nombre;
    private String apellidos;
    private String email;
    private String telefono;
    private boolean activo;
    private LocalDate fechaAlta;

    // Constructor vacío (crea un socio “en blanco”)
    public Socio() {
    }

    // Constructor completo (crea un socio con todos sus datos)
    public Socio(int idSocio, String dni, String nombre, String apellidos,
                 String email, String telefono, boolean activo, LocalDate fechaAlta) {
        this.idSocio = idSocio;
        this.dni = dni;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.email = email;
        this.telefono = telefono;
        this.activo = activo;
        this.fechaAlta = fechaAlta;
    }

    // Getters y Setters
    public int getIdSocio() {
        return idSocio;
    }

    public void setIdSocio(int idSocio) {
        this.idSocio = idSocio;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public LocalDate getFechaAlta() {
        return fechaAlta;
    }

    public void setFechaAlta(LocalDate fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    // Imprimir un socio fácil en consola
    @Override
    public String toString() {
        return "Socio{" +
                "idSocio=" + idSocio +
                ", dni='" + dni + '\'' +
                ", nombre='" + nombre + '\'' +
                ", apellidos='" + apellidos + '\'' +
                ", email='" + email + '\'' +
                ", telefono='" + telefono + '\'' +
                ", activo=" + activo +
                ", fechaAlta=" + fechaAlta +
                '}';
    }
}

