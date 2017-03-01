package br.edu.ifpb.ajudemais.ajudemaismaps.entities;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

/**
 * Created by rafaelfeitosa on 14/02/17.
 *
 * Representa as rotas de um trajeto
 */

public class Rota {

    private Distancia distancia;
    private Duracao duracao;
    private String enderecoDestino;
    private LatLng localizacaoDestino;
    private String enderecoOrigem;
    private LatLng localizacaoOrigem;

    private List<LatLng> points;

    public List<LatLng> getPoints() {
        return points;
    }

    public void setPoints(List<LatLng> points) {
        this.points = points;
    }

    public LatLng getLocalizacaoOrigem() {
        return localizacaoOrigem;
    }

    public void setLocalizacaoOrigem(LatLng localizacaoOrigem) {
        this.localizacaoOrigem = localizacaoOrigem;
    }

    public String getEnderecoOrigem() {
        return enderecoOrigem;
    }

    public void setEnderecoOrigem(String enderecoOrigem) {
        this.enderecoOrigem = enderecoOrigem;
    }

    public LatLng getLocalizacaoDestino() {
        return localizacaoDestino;
    }

    public void setLocalizacaoDestino(LatLng localizacaoDestino) {
        this.localizacaoDestino = localizacaoDestino;
    }

    public String getEnderecoDestino() {
        return enderecoDestino;
    }

    public void setEnderecoDestino(String enderecoDestino) {
        this.enderecoDestino = enderecoDestino;
    }

    public Duracao getDuracao() {
        return duracao;
    }

    public void setDuracao(Duracao duracao) {
        this.duracao = duracao;
    }

    public Distancia getDistancia() {
        return distancia;
    }

    public void setDistancia(Distancia distancia) {
        this.distancia = distancia;
    }

    @Override
    public String toString() {
        return "Rota{" +
                "distancia=" + distancia +
                ", duracao=" + duracao +
                ", enderecoDestino='" + enderecoDestino + '\'' +
                ", localizacaoDestino=" + localizacaoDestino +
                ", enderecoOrigem='" + enderecoOrigem + '\'' +
                ", localizacaoOrigem=" + localizacaoOrigem +
                ", points=" + points +
                '}';
    }
}
