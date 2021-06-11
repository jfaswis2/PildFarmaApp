package com.example.pildfarmaapp.models;

//Clase con todos los datos de la alarma tomadas en una captura
public class PostAlarma {
    private String ID;
    private String Medicamento;
    private String Dosis;
    private String Frecuencia;
    private String ViaAdministracion;
    private String DuracionTratamiento;
    private String Imagen;
    private String IDUsuario;
    private String Estado;
    private String Broadcaster;
    private String cantidad;

    public PostAlarma(String ID, String medicamento, String dosis, String frecuencia, String viaAdministracion, String duracionTratamiento, String imagen, String IDUsuario, String estado, String Broadcaster,String Cantidad) {
        this.ID = ID;
        Medicamento = medicamento;
        Dosis = dosis;
        Frecuencia = frecuencia;
        ViaAdministracion = viaAdministracion;
        DuracionTratamiento = duracionTratamiento;
        Imagen = imagen;
        this.IDUsuario = IDUsuario;
        Estado = estado;
        this.Broadcaster = Broadcaster;
        this.cantidad = Cantidad;
    }

    public PostAlarma() {}

    public String getID() { return ID; }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getMedicamento() {
        return Medicamento;
    }

    public void setMedicamento(String medicamento) {
        Medicamento = medicamento;
    }

    public String getDosis() {
        return Dosis;
    }

    public void setDosis(String dosis) {
        Dosis = dosis;
    }

    public String getFrecuencia() {
        return Frecuencia;
    }

    public void setFrecuencia(String frecuencia) {
        Frecuencia = frecuencia;
    }

    public String getViaAdministracion() {
        return ViaAdministracion;
    }

    public void setViaAdministracion(String viaAdministracion) {
        ViaAdministracion = viaAdministracion;
    }

    public String getDuracionTratamiento() {
        return DuracionTratamiento;
    }

    public void setDuracionTratamiento(String duracionTratamiento) {
        DuracionTratamiento = duracionTratamiento;
    }

    public String getImagen() {
        return Imagen;
    }

    public void setImagen(String imagen) {
        Imagen = imagen;
    }

    public String getIDUsuario() {
        return IDUsuario;
    }

    public void setIDUsuario(String IDUsuario) {
        this.IDUsuario = IDUsuario;
    }

    public String getEstado() {
        return Estado;
    }

    public void setEstado(String estado) {
        Estado = estado;
    }

    public String getBroadcaster() {
        return Broadcaster;
    }

    public void setBroadcaster(String broadcaster) {
        Broadcaster = broadcaster;
    }

    public String getCantidad() {
        return cantidad;
    }

    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }
}
