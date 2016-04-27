package edu.villanova.tkenned8.here;

import android.Manifest;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener {

    LocationManager locationManager;
    LatLng destination = null;
    double Lat;
    double Lon;
    GoogleMap mMap;
    boolean isBound = false;
    private Service service;

    @Override
    //initialize view and buttons
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        ImageButton imageButton = (ImageButton) findViewById(R.id.imageButton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MapsActivity.this, locationSearchActivity.class);
                startActivityForResult(intent, 1);
            }
        });

        //start location manager
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, this, null);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        ImageButton startButton = (ImageButton) findViewById(R.id.startButton);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Retrieve previous intent information
                Intent intent = getIntent();
                String contactName = intent.getStringExtra("contactName");
                String phoneNumber = intent.getStringExtra("phoneNumber");
                String destinationType = intent.getStringExtra("destinationType");

                //Set up toast message
                Context context = getApplicationContext();
                CharSequence text = "Have a safe trip";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();

                //Start service with passed information from previous activity
                Intent service = new Intent(MapsActivity.this, locationNotifications.class);
                service.putExtra("contactName",contactName);
                service.putExtra("phoneNumber",phoneNumber);
                service.putExtra("destinationType",destinationType);
                Log.i("MyApp", "destination before intent: " + destination.toString());
                service.putExtra("destinationLat", destination.latitude);
                service.putExtra("destinationLon", destination.longitude);

                //Bundle bundle = new Bundle();
                //bundle.putParcelable("destination", destination);
                //service.putExtra("destination", bundle);
                Log.d("MyApp","Got to startButton");

                bindService(service, con, Context.BIND_AUTO_CREATE);
                startService(service);


            }
        });
    }
    //adds marker for location if it is available
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, this, null);
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if(location != null) {
            Lat = location.getLatitude();
            Lon = location.getLongitude();
            mMap.addMarker(new MarkerOptions().position(new LatLng(Lat,Lon)));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(Lat,Lon)));
        }
    }
//recieves destination info and generates a marker
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Double latitude = data.getDoubleExtra("latitude", 0.00);
                Double longitude = data.getDoubleExtra("longitude", 0.00);
                mMap.clear();
                destination = new LatLng(latitude, longitude);
                Log.i("MyApp", "destination onActivityResult: " + destination.toString());
                mMap.addMarker(new MarkerOptions().position(destination));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(destination));
                mMap.moveCamera(CameraUpdateFactory.zoomTo(15));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(destination));

            } else {
                Log.i("MyActivity", "Result not ok");
            }
        } else {
            Log.i("MyActivity", "no result code");
        }
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    private ServiceConnection con = new ServiceConnection()
    {
        @Override
        public void onServiceConnected(ComponentName name, IBinder ibind) {
            service= ((locationNotifications.MyBinder)ibind).getService();
            isBound = true;
        }
        @Override
        public void onServiceDisconnected(ComponentName name) {
            service = null;
            isBound = false;
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("MyApp", "onResume() triggered");
        if (isBound) {
            unbindService(con);
        }
    }
}
