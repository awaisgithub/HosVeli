package com.od.mma.event;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.od.mma.R;

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

        findViewById(R.id.loc_icon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("geo:" + lat + "," + lng + "?q=(" + address + ")@" + lat + "," + lng));
                startActivity(intent);
            }
        });

        if(lat == 0.0 && lng == 0.0) {
            findViewById(R.id.loc_icon).setVisibility(View.GONE);
        }
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
                    .zoom(8)
                    .bearing(90)
                    .tilt(10)
                    .build();
        } else {
            eventLocation = new LatLng(lat, lng);
            mMap.addMarker(new MarkerOptions().position(eventLocation).title(address));
            cameraPosition = new CameraPosition.Builder()
                    .target(eventLocation)
                    .zoom(15)
                    .bearing(90)
                    .tilt(10)
                    .build();
        }
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }
}
