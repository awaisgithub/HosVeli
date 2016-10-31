package com.od.hrdf.event;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.od.hrdf.R;

public class EventLocationActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private double lat, lng;
    private String address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_location);
        lat = getIntent().getDoubleExtra("lat", 0.0);
        lng = getIntent().getDoubleExtra("lng", 0.0);
        address = getIntent().getStringExtra("address");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng eventLocation;
        CameraPosition cameraPosition;
        if(lat == 0.0 && lng == 0.0) {
            eventLocation = new LatLng(4.0891876, 100.5613175);
            cameraPosition = new CameraPosition.Builder()
                    .target(eventLocation)
                    .zoom(5)
                    .bearing(90)
                    .tilt(10)
                    .build();
        } else {
            eventLocation = new LatLng(lat, lng);
            mMap.addMarker(new MarkerOptions().position(eventLocation).title(address));
            cameraPosition = new CameraPosition.Builder()
                    .target(eventLocation)
                    .zoom(10)
                    .bearing(90)
                    .tilt(10)
                    .build();
        }
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }
}
