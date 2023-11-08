package com.example.sicuraapp.Entities;

import java.io.Serializable;
import java.util.ArrayList;

public class Usuario implements Serializable {

    private String id;
    private String correo;
    private String nombreApellidos;
    private String celular;
    private String fotoUrl;
    private ArrayList<String> contactosID;

    public ArrayList<String> getContactosID() {
        return contactosID;
    }

    public void setContactosID(ArrayList<String> contactosID) {
        this.contactosID = contactosID;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getNombreApellidos() {
        return nombreApellidos;
    }

    public void setNombreApellidos(String nombreApellidos) {
        this.nombreApellidos = nombreApellidos;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getFotoUrl() {
        return fotoUrl;
    }

    public void setFotoUrl(String fotoUrl) {
        this.fotoUrl = fotoUrl;
    }
}
