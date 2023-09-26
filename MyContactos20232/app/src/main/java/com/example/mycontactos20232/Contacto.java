package com.example.mycontactos20232;

public class Contacto {
    private int idcontacto;
    private String nombre;
    private String alias;
    private int tipo;
    public Contacto(int idcontacto_,String nombre_,String alias_,int tipo_){
        this.idcontacto = idcontacto_;
        this.nombre = nombre_;
        this.alias = alias_;
        this.tipo = tipo_;
    }
    public int getIdcontacto() {
        return idcontacto;
    }

    public void setIdcontacto(int idcontacto) {
        this.idcontacto = idcontacto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }
}