package br.edu.ifpb.ajudemais.ajudemaismaps.entities;

/**
 * Created by rafaelfeitosa on 14/02/17.
 */

public class Duracao {

    public String texto;
    public int valor;

    public  Duracao(String texto, int valor){
        this.texto = texto;
        this.valor = valor;
    }

    @Override
    public String toString() {
        return "Duracao{" +
                "texto='" + texto + '\'' +
                ", valor=" + valor +
                '}';
    }
}
