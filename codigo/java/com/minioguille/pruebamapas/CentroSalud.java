package com.minioguille.pruebamapas;

public class CentroSalud {
    private int id_centro;
    private String nombre_centro;
    private String descripcion_centro;
    private String horario_atencion_centro;
    private String contacto_centro;
    private String direccion_centro;
    private float mapa_latitud_centro;
    private float mapa_longitud_centro;

    public CentroSalud(){

    }

    public CentroSalud(int id_centro, String nombre_centro, String descripcion_centro, String horario_atencion_centro, String contacto_centro, String direccion_centro, float mapa_latitud_centro, float mapa_longitud_centro) {
        this.id_centro = id_centro;
        this.nombre_centro = nombre_centro;
        this.descripcion_centro = descripcion_centro;
        this.horario_atencion_centro = horario_atencion_centro;
        this.contacto_centro = contacto_centro;
        this.direccion_centro = direccion_centro;
        this.mapa_latitud_centro = mapa_latitud_centro;
        this.mapa_longitud_centro = mapa_longitud_centro;
    }

    public int getId_centro() {
        return id_centro;
    }

    public void setId_centro(int id_centro) {
        this.id_centro = id_centro;
    }

    public String getNombre_centro() {
        return nombre_centro;
    }

    public void setNombre_centro(String nombre_centro) {
        this.nombre_centro = nombre_centro;
    }

    public String getDescripcion_centro() {
        return descripcion_centro;
    }

    public void setDescripcion_centro(String descripcion_centro) {
        this.descripcion_centro = descripcion_centro;
    }

    public String getHorario_atencion_centro() {
        return horario_atencion_centro;
    }

    public void setHorario_atencion_centro(String horario_atencion_centro) {
        this.horario_atencion_centro = horario_atencion_centro;
    }

    public String getContacto_centro() {
        return contacto_centro;
    }

    public void setContacto_centro(String contacto_centro) {
        this.contacto_centro = contacto_centro;
    }

    public String getDireccion_centro() {
        return direccion_centro;
    }

    public void setDireccion_centro(String direccion_centro) {
        this.direccion_centro = direccion_centro;
    }

    public float getMapa_latitud_centro() {
        return mapa_latitud_centro;
    }

    public void setMapa_latitud_centro(float mapa_latitud_centro) {
        this.mapa_latitud_centro = mapa_latitud_centro;
    }

    public float getMapa_longitud_centro() {
        return mapa_longitud_centro;
    }

    public void setMapa_longitud_centro(float mapa_longitud_centro) {
        this.mapa_longitud_centro = mapa_longitud_centro;
    }

    @Override
    public String toString() {
        return "CentroSalud{" +
                "id_centro=" + id_centro +
                ", nombre_centro='" + nombre_centro + '\'' +
                ", descripcion_centro='" + descripcion_centro + '\'' +
                ", horario_atencion_centro='" + horario_atencion_centro + '\'' +
                ", contacto_centro='" + contacto_centro + '\'' +
                ", direccion_centro='" + direccion_centro + '\'' +
                ", mapa_latitud_centro=" + mapa_latitud_centro +
                ", mapa_longitud_centro=" + mapa_longitud_centro +
                '}';
    }
}
