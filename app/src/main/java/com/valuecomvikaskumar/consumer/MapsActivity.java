package com.valuecomvikaskumar.consumer;

import android.*;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.test.mock.MockPackageManager;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private GPSTracker gps;
    private double latitude;
    private double longitude;
    private static final int REQUEST_CODE_PERMISSION=2;
    private String mPermission= android.Manifest.permission.ACCESS_FINE_LOCATION;
    private String lat,lng;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        lat=getIntent().getStringExtra("lat");
        lng=getIntent().getStringExtra("lng");

        try{
            if (ActivityCompat.checkSelfPermission(this,mPermission)!= MockPackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this,new String[]{mPermission},REQUEST_CODE_PERMISSION);
            }

        }catch (Exception e){
            e.getStackTrace();
        }

        gps=new GPSTracker(MapsActivity.this);
        if (gps.canGetLocation()){
            latitude=gps.getLatitude();
            longitude=gps.getLongitude();
        }
        else {
            gps.showSettingAlert();
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
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng yourPosition = new LatLng(latitude, longitude);
        LatLng address=new LatLng(Double.parseDouble(lat),Double.parseDouble(lng));
        mMap.addMarker(new MarkerOptions().position(address).title("Shop"));
        mMap.addMarker(new MarkerOptions().position(yourPosition).title("Your position"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(yourPosition));

        mMap.moveCamera(CameraUpdateFactory.zoomTo(16));
    }
}
