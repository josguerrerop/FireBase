package org.itiud.appi.logica;


public class Celda {
    private boolean estado;
    private String value;
    public static final String WHITE = "-";

    public Celda(boolean estado, String value){
        this.estado= estado;
        this.value=value;
    }

    Celda(){
        this.estado= true;
        this.value=WHITE;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public boolean isEstado() {
        return estado;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
