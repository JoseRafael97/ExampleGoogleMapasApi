package br.edu.ifpb.ajudemais.ajudemaismaps.entities;

/**
 * Created by rafaelfeitosa on 14/02/17.
 */

public class Distancia {

    public String texto;
    public int valor;

    public  Distancia(String texto, int valor){
        this.texto = texto;
        this.valor = valor;
    }

    @Override
    public String toString() {
        return "Distancia{" +
                "texto='" + texto + '\'' +
                ", valor=" + valor +
                '}';
    }
}
