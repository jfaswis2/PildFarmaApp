package com.example.pildfarmaapp.models;

//Clase con los datos del usuario
public class User {

    private String id;
    private String email;
    private String username;
    private String FechaNacimiento;
    private String Cap;
    private String NumeroTel;
    private String imageProfile;

    public User() { }

    public User(String id, String email, String username, String fechaNacimiento, String cap, String numeroTel, String imageProfile) {
        this.id = id;
        this.email = email;
        this.username = username;
        FechaNacimiento = fechaNacimiento;
        Cap = cap;
        NumeroTel = numeroTel;
        this.imageProfile = imageProfile;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFechaNacimiento() {
        return FechaNacimiento;
    }

    public void setFechaNacimiento(String fechaNacimiento) {
        FechaNacimiento = fechaNacimiento;
    }

    public String getCap() {
        return Cap;
    }

    public void setCap(String cap) {
        Cap = cap;
    }

    public String getNumeroTel() {
        return NumeroTel;
    }

    public void setNumeroTel(String numeroTel) {
        NumeroTel = numeroTel;
    }

    public String getImageProfile() {
        return imageProfile;
    }

    public void setImageProfile(String imageProfile) {
        this.imageProfile = imageProfile;
    }
}
