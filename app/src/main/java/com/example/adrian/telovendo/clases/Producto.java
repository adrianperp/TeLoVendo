package com.example.adrian.telovendo.clases;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;

import static com.example.adrian.telovendo.utilidades.FechasUtils.getDate;

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

    // Comparator
    public static Comparator<Producto> ordenarPorTituloAsc = new Comparator<Producto>(){
        public int compare(Producto p1, Producto p2){
            return p1.getNombre().compareTo(p2.getNombre());
        }
    };
    public static Comparator<Producto> ordenarPorTituloDes = new Comparator<Producto>(){
        public int compare(Producto p1, Producto p2){
            return p2.getNombre().compareTo(p1.getNombre());
        }
    };
    public static Comparator<Producto> ordenarPorPrecioAsc = new Comparator<Producto>(){
        public int compare(Producto p1, Producto p2){
            return Double.compare(p1.getPrecio(), p2.getPrecio());
        }
    };
    public static Comparator<Producto> ordenarPorPrecioDes = new Comparator<Producto>(){
        public int compare(Producto p1, Producto p2){
            return Double.compare(p2.getPrecio(), p1.getPrecio());
        }
    };
    public static Comparator<Producto> ordenarPorFechaAsc = new Comparator<Producto>(){
        public int compare(Producto p1, Producto p2){
            try {
                Date d1 = getDate(p1.getFechaPublicado(), p1.getHoraPublicado());
                Date d2 = getDate(p2.getFechaPublicado(), p2.getHoraPublicado());

                if (d2.before(d1)) {
                    return -1;
                } else if (d2.after(d1)) {
                    return 1;
                } else {
                    return 0;
                }
            }
            catch (ParseException e1) {
                e1.printStackTrace();
            }
            catch (Exception e2) {
                e2.printStackTrace();
            }
            return 0;
        }
    };
    public static Comparator<Producto> ordenarPorFechaDes = new Comparator<Producto>(){
        public int compare(Producto p1, Producto p2){
            try {
                Date d1 = getDate(p1.getFechaPublicado(), p1.getHoraPublicado());
                Date d2 = getDate(p2.getFechaPublicado(), p2.getHoraPublicado());

                if (d1.before(d2)) {
                    return -1;
                } else if (d1.after(d2)) {
                    return 1;
                } else {
                    return 0;
                }
            }
            catch (ParseException e1) {
                e1.printStackTrace();
            }
            catch (Exception e2) {
                e2.printStackTrace();
            }
            return 0;
        }
    };
}
