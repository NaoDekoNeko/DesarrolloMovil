package com.example.mycontactos20232;

public class Contacto {
    private int idcontacto;
    private String nombre;
    private String alias;
    private int tipo;
    private String codigo;
    public Contacto(int idcontacto,String nombre,String alias,int tipo){
        this.idcontacto = idcontacto;
        this.nombre = nombre;
        this.alias = alias;
        this.tipo = tipo;
        this.codigo = null;
    }

    public Contacto(String nombre, String alias, int tipo) {
        this.nombre = nombre;
        this.alias = alias;
        this.tipo = tipo;
        this.codigo = null;
    }

    public int getIdcontacto() {
        return idcontacto;
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
    public void setCodigo(String codigo){this.codigo = codigo;}
    public String getCodigo(){return codigo;}
}