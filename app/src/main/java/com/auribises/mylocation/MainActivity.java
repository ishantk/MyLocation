package com.auribises.mylocation;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, LocationListener {

    TextView txtLocation;
    Button btnFetch;

    LocationManager locationManager;

    ProgressDialog progressDialog;

    void initViews() {
        txtLocation = (TextView) findViewById(R.id.textViewLocation);
        btnFetch = (Button) findViewById(R.id.buttonFetch);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        btnFetch.setOnClickListener(this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Fetching Location...");
        progressDialog.setCancelable(false);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.buttonFetch) {
            // Code here to fetch the location
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this,"Please grant permissions in Settings",Toast.LENGTH_LONG).show();
            }else {
                progressDialog.show();
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5, 100, this);
            }
        }
    }

    @Override
    public void onLocationChanged(Location location) {

        // Geocoding
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();

        txtLocation.setText(latitude+" : "+longitude);

        // Reverse Geocoding
        try {
            Geocoder geocoder = new Geocoder(this);
            List<Address> adrsList = geocoder.getFromLocation(latitude,longitude,2);
            if(adrsList!=null && adrsList.size()>0){

                StringBuffer buffer = new StringBuffer();

                Address address = adrsList.get(0);

                for(int i=0;i<address.getMaxAddressLineIndex();i++){
                    buffer.append(address.getAddressLine(i)+"\n");
                }

                txtLocation.setText(buffer.toString());

            }else{
                Toast.makeText(this,"Sorry, No Location Found",Toast.LENGTH_LONG).show();
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        locationManager.removeUpdates(this);
        progressDialog.dismiss();
    }

    @Override
    protected void onStop() {
        super.onStop();

        //locationManager.removeUpdates(this);

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
