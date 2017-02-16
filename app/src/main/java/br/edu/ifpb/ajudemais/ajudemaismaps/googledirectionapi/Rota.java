package br.edu.ifpb.ajudemais.ajudemaismaps.googledirectionapi;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

/**
 * Created by rafaelfeitosa on 14/02/17.
 */

public class Rota {

    public Distancia distancia;
    public Duracao duracao;
    public String enderecoDestino;
    public LatLng localizacaoDestino;
    public String enderecoOrigem;
    public LatLng localizacaoOrigem;

    public List<LatLng> points;
}
