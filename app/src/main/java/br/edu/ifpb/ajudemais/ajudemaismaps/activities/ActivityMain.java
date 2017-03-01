package br.edu.ifpb.ajudemais.ajudemaismaps.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.UnsupportedEncodingException;
import java.util.List;

import br.edu.ifpb.ajudemais.ajudemaismaps.R;
import br.edu.ifpb.ajudemais.ajudemaismaps.entities.Rota;
import br.edu.ifpb.ajudemais.ajudemaismaps.googledirectionapi.DirecaoMapa;
import br.edu.ifpb.ajudemais.ajudemaismaps.googledirectionapi.DirecaoMapaListener;

public class ActivityMain extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, DirecaoMapaListener {

    private Button btnMinhaLocalizacao;
    private TextView tvMinhaLocalizacao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        btnMinhaLocalizacao = (Button) findViewById(R.id.btnmylocalizacao);
        tvMinhaLocalizacao = (TextView) findViewById(R.id.tvminhalocalizacao);

        btnMinhaLocalizacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setMinhaLocalizacao();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_map1) {
            Intent intent = new Intent(this, ActivityMain.class);
            startActivity(intent);
        } else if (id == R.id.nav_map2) {
            Intent intent = new Intent(this, MapsActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Pega minha localização atual via GPS é seta em um campo para exibição
     */
    private void setMinhaLocalizacao() {

        String latitude = "";
        String longitude = "";
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        LocationManager locManager =
                (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        if (locManager.getBestProvider(criteria, true) != null) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }

            Location myLocation = locManager.getLastKnownLocation(locManager.getBestProvider(criteria, true));
            latitude = Double.toString(myLocation.getLatitude());
            longitude = Double.toString(myLocation.getLongitude());

            tvMinhaLocalizacao.setText(latitude + "," + longitude);


        }

        try {
            new DirecaoMapa(this, latitude + "," + longitude, "Centro, Monteiro").executar();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onEncontrarDirecaoOrigem() {

    }

    @Override
    public void onDirecaoEncontradaComSucesso(List<Rota> rota) {

    }
}
