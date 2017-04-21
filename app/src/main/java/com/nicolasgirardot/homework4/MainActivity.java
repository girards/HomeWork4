package com.nicolasgirardot.homework4;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback, LoaderManager.LoaderCallbacks {

    private GoogleMap mMap;
    private Button buttonCity, buttonUniversity, buttonECS;
    private LatLng LOCATION_UNIV = new LatLng(33.783768, -118.114336);
    private LatLng LOCATION_ECS = new LatLng(33.782777, -118.111868);

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        LocationsDB db = LocationsDB.getInstance(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonCity = (Button) findViewById(R.id.button_city);
        buttonUniversity = (Button) findViewById(R.id.button_university);
        buttonECS = (Button) findViewById(R.id.button_ecs);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;



        LocationsDB db = LocationsDB.getInstance(this);
        List<Location> locations = db.getAllPosts();

        for (int i = 0; i < locations.size(); i++) {
            mMap.addMarker(new MarkerOptions().position(locations.get(i).getPosition()));
        }

        buttonCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                CameraUpdate update = CameraUpdateFactory.newLatLngZoom(LOCATION_UNIV, 9);
                mMap.animateCamera(update);
            }
        });

        buttonUniversity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                CameraUpdate update = CameraUpdateFactory.newLatLngZoom(LOCATION_UNIV, 16);
                mMap.animateCamera(update);
            }
        });

        buttonECS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                CameraUpdate update = CameraUpdateFactory.newLatLngZoom(LOCATION_ECS, 19);
                mMap.animateCamera(update);
            }
        });

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                new LocationInsertTask().execute(new Location(latLng, mMap.getCameraPosition().zoom));
                mMap.addMarker(new MarkerOptions().position(latLng));
            }
        });

        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                mMap.clear();
                new LocationDeleteTask().execute();
                Toast.makeText(getBaseContext(), "All Markers are remove", Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader loader, Object data) {

    }

    @Override
    public void onLoaderReset(Loader loader) {

    }
}

class LocationInsertTask extends AsyncTask<Location, Void, Void> {

    @Override
    protected Void doInBackground(Location... params) {
        LocationsDB db = LocationsDB.getInstance();
        db.addLocation(params[0]);
        return null;
    }
}

class LocationDeleteTask extends AsyncTask<Void, Void, Void> {
    @Override
    protected Void doInBackground(Void... params) {
        LocationsDB db = LocationsDB.getInstance();
        db.deleteAllLocations();
        return null;
    }
}