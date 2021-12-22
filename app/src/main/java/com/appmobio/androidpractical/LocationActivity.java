package com.appmobio.androidpractical;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.widget.Toolbar;

import com.appmobio.androidpractical.model.Item;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.Serializable;

public class LocationActivity extends AppCompatActivity implements OnMapReadyCallback {
    public static final String EXTRA_DATA = "EXTRA_DATA";
    Item item;
    GoogleMap map;
    String title,address,info;
    Double lon,lat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);


        //intialized map fragment
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.google_current_map);
        mapFragment.getMapAsync(this);

        //getData form serializable
        item = (Item) getIntent().getSerializableExtra(EXTRA_DATA);
        lat = Double.parseDouble(item.getLatitude());
        lon = Double.parseDouble(item.getLongitude());
        title = item.getTitle();
        address = item.getAddress();

        //add string together to show in map
        info = String.format("%s \n %s", title, address);

        //back button
        // calling the action bar
        ActionBar actionBar = getSupportActionBar();

        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);




    }

    // this event will enable the back
    // function to the button on press
    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() == 0) {
            this.finish();
        }
        else {
            getFragmentManager().popBackStack();
        }
    }

    // this event will enable the back
    // function to the button on press
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        LatLng Location = new LatLng(lat,lon);
        Toast.makeText(this, lat + " "+ lon, Toast.LENGTH_SHORT).show();
        map.addMarker(new MarkerOptions().position(Location).title(info));
        map.moveCamera(CameraUpdateFactory.newLatLng(Location));

    }
}