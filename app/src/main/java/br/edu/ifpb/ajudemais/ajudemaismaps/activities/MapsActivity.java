package br.edu.ifpb.ajudemais.ajudemaismaps.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import br.edu.ifpb.ajudemais.ajudemaismaps.util.PermitirLocalizacao;
import br.edu.ifpb.ajudemais.ajudemaismaps.R;
import br.edu.ifpb.ajudemais.ajudemaismaps.googledirectionapi.DirecaoMapa;
import br.edu.ifpb.ajudemais.ajudemaismaps.googledirectionapi.DirecaoMapaListener;
import br.edu.ifpb.ajudemais.ajudemaismaps.entities.Rota;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener, ActivityCompat.OnRequestPermissionsResultCallback, DirecaoMapaListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private GoogleMap mMap;
    private LocationManager locationManager;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private boolean mPermissaoNegada = false;

    private Button btnBuscar;
    private EditText etOrigem;
    private EditText etDestino;
    private EditText etDestino2;

    private List<Marker> originMarkers = new ArrayList<>();
    private List<Marker> destinationMarkers = new ArrayList<>();
    private List<Polyline> polylinePaths = new ArrayList<>();
    private ProgressDialog progressDialog;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        btnBuscar = (Button) findViewById(R.id.btnBuscar);
        etOrigem = (EditText) findViewById(R.id.etOrigem);
        etDestino = (EditText) findViewById(R.id.etDestino);
        etDestino2 = (EditText) findViewById(R.id.etDestino2);

        // Create an instance of GoogleAPIClient.
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRequest();
            }
        });
    }

    private void sendRequest() {
        String origem = etOrigem.getText().toString();
        String destino = etDestino.getText().toString();
        String destino2 = etDestino2.getText().toString();

        if (origem.isEmpty()) {
            Toast.makeText(this, "Por favor preencha a origem", Toast.LENGTH_SHORT).show();
            return;
        }
        if (destino.isEmpty()) {
            Toast.makeText(this, "Por favor preencha o destino", Toast.LENGTH_SHORT).show();
            return;
        }

        if (destino2.isEmpty()) {
            Toast.makeText(this, "Por favor preencha o destino 2", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            new DirecaoMapa(this, origem, destino).executar();
            new DirecaoMapa(this, origem, destino2).executar();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String provider = locationManager.getBestProvider(criteria, true);
        Toast.makeText(this, "Provider: " + provider, Toast.LENGTH_LONG).show();

        mMap = googleMap;
        mMap.setOnMapClickListener(this);
        mMap.getUiSettings().setZoomControlsEnabled(true);

        enableMyLocation();

    }


    @Override
    public void onMapClick(LatLng latLng) {
        Toast.makeText(this, "coordenada" + latLng.toString(), Toast.LENGTH_LONG).show();

    }

    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            PermitirLocalizacao.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                    android.Manifest.permission.ACCESS_FINE_LOCATION, true);
        } else if (mMap != null) {
            mMap.setMyLocationEnabled(true);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return;
        }

        if (PermitirLocalizacao.isPermissionGranted(permissions, grantResults,
                android.Manifest.permission.ACCESS_FINE_LOCATION)) {
            enableMyLocation();
        } else {

            mPermissaoNegada = true;
        }
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        if (mPermissaoNegada) {
            showMissingPermissionError();
            mPermissaoNegada = false;
        }
    }

    private void showMissingPermissionError() {
        PermitirLocalizacao.PermissionDeniedDialog
                .newInstance(true).show(getSupportFragmentManager(), "dialog");
    }

    @Override
    public void onEncontrarDirecaoOrigem() {
//        progressDialog = ProgressDialog.show(this, "Por Favor aguarde!",
//                "Buscando direções..!", true);

        if (originMarkers != null) {
            for (Marker marker : originMarkers) {
                marker.remove();
            }
        }

        if (destinationMarkers != null) {
            for (Marker marker : destinationMarkers) {
                marker.remove();
            }
        }

        if (polylinePaths != null) {
            for (Polyline polyline : polylinePaths) {
                polyline.remove();
            }
        }
    }

    @Override
    public void onDirecaoEncontradaComSucesso(List<Rota> rotas) {
        polylinePaths = new ArrayList<>();
        originMarkers = new ArrayList<>();
        destinationMarkers = new ArrayList<>();

        for (Rota rota : rotas) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(rota.localizacaoOrigem, 16));
            ((TextView) findViewById(R.id.tvDuracao)).setText(rota.duracao.texto);
            ((TextView) findViewById(R.id.tvDistancia)).setText(rota.distancia.texto);

//            if(cont>0) {
//                ((TextView) findViewById(R.id.tvDuracao2)).setText("Destino 2: "+rota.duracao.texto);
//                ((TextView) findViewById(R.id.tvDistancia2)).setText("Destino 2: "+rota.distancia.texto);
//            }
            originMarkers.add(mMap.addMarker(new MarkerOptions()
                    .title(rota.enderecoOrigem)
                    .position(rota.localizacaoOrigem)));
            destinationMarkers.add(mMap.addMarker(new MarkerOptions()
                    .title(rota.enderecoDestino)
                    .position(rota.localizacaoDestino)));

            PolylineOptions polylineOptions = new PolylineOptions().
                    geodesic(true).
                    color(Color.BLUE).
                    width(10);

            for (int i = 0; i < rota.points.size(); i++)
                polylineOptions.add(rota.points.get(i));

            polylinePaths.add(mMap.addPolyline(polylineOptions));
        }
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
            etOrigem.setText(String.valueOf(mLastLocation.getLatitude()+","+mLastLocation.getLongitude()));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()), 16));
        }
    }
    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

}
