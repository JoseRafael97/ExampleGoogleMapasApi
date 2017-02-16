package br.edu.ifpb.ajudemais.ajudemaismaps.googledirectionapi;

import android.os.AsyncTask;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import br.edu.ifpb.ajudemais.ajudemaismaps.entities.Distancia;
import br.edu.ifpb.ajudemais.ajudemaismaps.entities.Duracao;
import br.edu.ifpb.ajudemais.ajudemaismaps.entities.Rota;

/**
 * Created by rafaelfeitosa on 14/02/17.
 */

public class DirecaoMapa {

    private static final String DIRECTION_URL_API = "https://maps.googleapis.com/maps/api/directions/json?";
    private static final String GOOGLE_API_KEY = "AIzaSyAMV2tOqh18Y0G4whvFRg3jpkTPI7v9Q6U";
    private DirecaoMapaListener listener;
    private String origem;
    private String destino;

    public DirecaoMapa(DirecaoMapaListener listener, String origem, String destino){
        this.destino = destino;
        this.origem = origem;
        this.listener = listener;
    }

    public void executar() throws UnsupportedEncodingException {
        listener.onEncontrarDirecaoOrigem();
        new DownloadRawData().execute(criarUrl());
    }

    private String criarUrl() throws UnsupportedEncodingException {
        String urlOrigin = URLEncoder.encode(origem, "utf-8");
        String urlDestination = URLEncoder.encode(destino, "utf-8");

        return DIRECTION_URL_API + "origin=" + urlOrigin + "&destination=" + urlDestination + "&key=" + GOOGLE_API_KEY;
    }


    private class DownloadRawData extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String link = params[0];
            try {
                URL url = new URL(link);
                InputStream is = url.openConnection().getInputStream();
                StringBuffer buffer = new StringBuffer();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                return buffer.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String res) {
            try {
                parseJSon(res);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void parseJSon(String data) throws JSONException {
        if (data == null)
            return;

        System.out.println("Dados : "+ data);

        List<Rota> rotas = new ArrayList<Rota>();
        JSONObject jsonData = new JSONObject(data);
        JSONArray jsonRoutes = jsonData.getJSONArray("routes");
        for (int i = 0; i < jsonRoutes.length(); i++) {
            JSONObject jsonRoute = jsonRoutes.getJSONObject(i);
            Rota rota = new Rota();
            JSONObject overview_polylineJson = jsonRoute.getJSONObject("overview_polyline");
            JSONArray jsonLegs = jsonRoute.getJSONArray("legs");
            JSONObject jsonLeg = jsonLegs.getJSONObject(0);
            JSONObject jsonDistancia = jsonLeg.getJSONObject("distance");
            JSONObject jsonDuracao = jsonLeg.getJSONObject("duration");
            JSONObject jsonLocalizacaoDestino = jsonLeg.getJSONObject("end_location");
            JSONObject jsonLocalizacaoOrigem = jsonLeg.getJSONObject("start_location");

            rota.distancia = new Distancia(jsonDistancia.getString("text"), jsonDistancia.getInt("value"));
            rota.duracao = new Duracao(jsonDuracao.getString("text"), jsonDuracao.getInt("value"));
            rota.enderecoDestino = jsonLeg.getString("end_address");
            rota.enderecoOrigem = jsonLeg.getString("start_address");
            rota.localizacaoOrigem = new LatLng(jsonLocalizacaoOrigem.getDouble("lat"), jsonLocalizacaoOrigem.getDouble("lng"));
            rota.localizacaoDestino = new LatLng(jsonLocalizacaoDestino.getDouble("lat"), jsonLocalizacaoDestino.getDouble("lng"));
            rota.points = decodePolyLine(overview_polylineJson.getString("points"));

            rotas.add(rota);
        }

        listener.onDirecaoEncontradaComSucesso(rotas);
    }

    private List<LatLng> decodePolyLine(final String poly) {
        int len = poly.length();
        int index = 0;
        List<LatLng> decoded = new ArrayList<LatLng>();
        int lat = 0;
        int lng = 0;

        while (index < len) {
            int b;
            int shift = 0;
            int result = 0;
            do {
                b = poly.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = poly.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            decoded.add(new LatLng(
                    lat / 100000d, lng / 100000d
            ));
        }

        return decoded;
    }

}


