package com.example.adrian.telovendo.clases;

import java.io.Serializable;
import java.util.Date;

public class Mensaje implements Serializable{
    private String texto;
    private Date fechaEnvio;
    private String emisor;
    private String foto;

    public Mensaje(String texto, Date fechaEnvio, String emisor, String foto) {
        this.texto = texto;
        this.fechaEnvio = fechaEnvio;
        this.emisor = emisor;
        this.foto = foto;
    }

    public Mensaje() {

    }

    public boolean isEmpty() {
        return this.emisor == null || this.fechaEnvio == null || this.texto == null;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public Date getFechaEnvio() {
        return fechaEnvio;
    }

    public void setFechaEnvio(Date fechaEnvio) {
        this.fechaEnvio = fechaEnvio;
    }

    public String getEmisor() {
        return emisor;
    }

    public void setEmisor(String emisor) {
        this.emisor = emisor;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }
}
