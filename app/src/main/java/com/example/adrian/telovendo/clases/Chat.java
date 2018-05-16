package com.example.adrian.telovendo.clases;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;

public class Chat implements Serializable {
    private String chatId;
    private ArrayList<Mensaje> listaMensajes = new ArrayList<Mensaje>();
    private ArrayList<String> listaParticipantes = new ArrayList<String>();

    public Chat(String emailEmisor, String emailReceptor) {
        this.chatId = UUID.randomUUID().toString();
        this.listaParticipantes.add(emailEmisor);
        this.listaParticipantes.add(emailReceptor);
    }

    public Chat() {
        this.chatId = UUID.randomUUID().toString();
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public ArrayList<Mensaje> getListaMensajes() {
        return listaMensajes;
    }

    public void setListaMensajes(ArrayList<Mensaje> listaMensajes) {
        this.listaMensajes = listaMensajes;
    }

    public ArrayList<String> getListaParticipantes() {
        return listaParticipantes;
    }

    public void setListaParticipantes(ArrayList<String> listaParticipantes) {
        this.listaParticipantes = listaParticipantes;
    }

    public boolean contains(String email1, String email2) {
        return this.listaParticipantes.contains(email1) && this.listaParticipantes.contains(email2);
    }
}
