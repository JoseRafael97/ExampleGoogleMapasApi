package br.edu.ifpb.ajudemais.ajudemaismaps.googledirectionapi;

import java.util.List;

import br.edu.ifpb.ajudemais.ajudemaismaps.entities.Rota;

/**
 * Created by rafaelfeitosa on 14/02/17.
 */

public interface DirecaoMapaListener {

    void onEncontrarDirecaoOrigem();
    void onDirecaoEncontradaComSucesso(List<Rota> rota);
}
