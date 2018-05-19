package com.example.adrian.telovendo.clases;

/**
 * Created by PegasusPC on 31/03/2018.
 */

public class Categoria {
    private String nombre;
    private int foto;
    private int color;

    //constructores
    public Categoria(String nombre, int foto, int color) {
        this.nombre = nombre;
        this.foto = foto;
        this.color = color;
    }

    public Categoria() {

    }

    //getters y setters
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getFoto() {
        return foto;
    }

    public void setFoto(int foto) {
        this.foto = foto;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return "Categoria{" +
                "nombre='" + nombre + '\'' +
                ", foto=" + foto +
                ", color=" + color +
                '}';
    }
}
