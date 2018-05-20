package com.example.adrian.telovendo.clases;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;

public class Chat implements Serializable {
    private String chatId;
    private ArrayList<Mensaje> listaMensajes = new ArrayList<Mensaje>();
    private ArrayList<Usuario> listaParticipantes = new ArrayList<Usuario>();

    public Chat(Usuario emisor, Usuario receptor) {
        this.chatId = UUID.randomUUID().toString();
        this.listaParticipantes.add(emisor);
        this.listaParticipantes.add(receptor);
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

    public ArrayList<Usuario> getListaParticipantes() {
        return listaParticipantes;
    }

    public void setListaParticipantes(ArrayList<Usuario> listaParticipantes) {
        this.listaParticipantes = listaParticipantes;
    }
/*
    public boolean contains(String email1, String email2) {
        return this.listaParticipantes.get(0).getEmail().equals(email1) && this.listaParticipantes.get(1).getEmail().equals(email2);
    }
*/
    public boolean isFrom(Usuario emisor, Usuario receptor) {
        String email1 = this.listaParticipantes.get(0).getEmail();
        String email2 = this.listaParticipantes.get(1).getEmail();

        if ((email1.equals(emisor.getEmail()) || email1.equals(receptor.getEmail()) &&
                email2.equals(emisor.getEmail()) || email2.equals(receptor.getEmail())))
            return true;
        return false;
    }
}
