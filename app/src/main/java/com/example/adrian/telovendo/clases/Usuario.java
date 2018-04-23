package com.example.adrian.telovendo.clases;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by adrian on 4/03/18.
 */

public class Usuario implements Serializable{

    private String nombre, apellidos, sexo, pais, ciudad, codigoPostal, fotoPerfil;
    private String email, contrasenya;
    private double valoracion;
    private ArrayList<String> listaBorradores = new ArrayList<String>();
    private ArrayList<String> listaProductos = new ArrayList<String>();
    private ArrayList<String> listaCompras = new ArrayList<String>();
    private ArrayList<String> listaVentas = new ArrayList<String>();

    // Constructores
    public Usuario(String nombre, String apellidos, String sexo, String pais, String ciudad, String codigoPostal,
                   String fotoPerfil) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.sexo = sexo;
        this.pais = pais;
        this.ciudad = ciudad;
        this.codigoPostal = codigoPostal;
        this.fotoPerfil = fotoPerfil;
    }

    public Usuario(String nombre, String apellidos, String email, String contrasenya){
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.email = email;
        this.contrasenya = contrasenya;
    }

    public Usuario(){

    }

    // Getters y setters
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContrasenya() {
        return contrasenya;
    }

    public void setContrasenya(String contrasenya) {
        this.contrasenya = contrasenya;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getCodigoPostal() {
        return codigoPostal;
    }

    public void setCodigoPostal(String codigoPostal) {
        this.codigoPostal = codigoPostal;
    }

    public String getFotoPerfil() {
        return fotoPerfil;
    }

    public void setFotoPerfil(String fotoPerfil) {
        this.fotoPerfil = fotoPerfil;
    }

    public double getValoracion() {
        return valoracion;
    }

    public void setValoracion(double valoracion) {
        this.valoracion = valoracion;
    }

    public ArrayList<String> getListaBorradores() {
        return listaBorradores;
    }

    public void setListaBorradores(ArrayList<String> listaBorradores) {
        this.listaBorradores = listaBorradores;
    }

    public ArrayList<String> getListaProductos() {
        return listaProductos;
    }

    public void setListaProductos(ArrayList<String> listaProductos) {
        this.listaProductos = listaProductos;
    }

    public ArrayList<String> getListaCompras() {
        return listaCompras;
    }

    public void setListaCompras(ArrayList<String> listaCompras) {
        this.listaCompras = listaCompras;
    }

    public ArrayList<String> getListaVentas() {
        return listaVentas;
    }

    public void setListaVentas(ArrayList<String> listaVentas) {
        this.listaVentas = listaVentas;
    }
}
