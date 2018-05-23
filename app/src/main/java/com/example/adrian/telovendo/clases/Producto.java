package com.example.adrian.telovendo.clases;

import java.util.ArrayList;

/**
 * Created by adrian on 4/03/18.
 */

public class Producto {

    private String nombre;
    private String descripcion;
    private String marca;
    private String modelo;
    private double precio;
    private double valoracion = -1;
    private String categoria;
    private String usuario;
    private String provincia;
    private String fechaPublicado;
    private String horaPublicado;
    private ArrayList<String> fotos = new ArrayList<String>();


    // Constructores
    public Producto(String nombre, String descripcion, String marca, String modelo, double precio, String categoria, String usuario, String provincia, ArrayList<String> fotos) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.marca = marca;
        this.modelo = modelo;
        this.precio = precio;
        this.categoria = categoria;
        this.usuario = usuario;
        this.provincia = provincia;
        this.fotos = fotos;
    }
    public Producto(){

    }

    // Getters y setters
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public double getValoracion() {
        return valoracion;
    }

    public void setValoracion(double valoracion) {
        this.valoracion = valoracion;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getFechaPublicado() {
        return fechaPublicado;
    }

    public void setFechaPublicado(String fechaPublicado) {
        this.fechaPublicado = fechaPublicado;
    }

    public String getHoraPublicado() {
        return horaPublicado;
    }

    public void setHoraPublicado(String horaPublicado) {
        this.horaPublicado = horaPublicado;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public ArrayList<String> getFotos() {
        return fotos;
    }

    public void setFotos(ArrayList<String> fotos) {
        this.fotos = fotos;
    }
}
