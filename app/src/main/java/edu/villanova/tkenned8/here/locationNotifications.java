package edu.villanova.tkenned8.here;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.telephony.SmsManager;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

public class locationNotifications extends Service {
    SmsManager mgr = null;
    private final IBinder binder = new MyBinder();
    String message;
    Boolean oneMile;
    Boolean fiveMile;
    Boolean tenMile;
    String contactName;
    String phoneNumber;
    String destinationType;
    LocationListener listener;
    Location destination;
    @Override
    public void onCreate() {
        super.onCreate();

        //Set up SMS manager
        mgr = SmsManager.getDefault();
        Log.d("MyApp", "Got to onCreate in service");

    }

    //needed to receive intent extras
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //Retrieve intent information
        contactName = intent.getStringExtra("contactName");
        phoneNumber = intent.getStringExtra("phoneNumber");
        destinationType = intent.getStringExtra("destinationType");
        Bundle bundle = intent.getParcelableExtra("destination");
        LatLng destinationCoord = bundle.getParcelable("destination");
        assert destinationCoord != null;
        double lat = destinationCoord.latitude;
        double lon = destinationCoord.longitude;
        destination.setLatitude(lat);
        destination.setLongitude(lon);



        Log.d("MyApp", "Contactname = " + contactName + " Phonenumber = " + phoneNumber + " destinationType = " + destinationType);

        //get Settings from Shared Preferences
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        message = sharedPreferences.getString("text", null);
        oneMile = sharedPreferences.getBoolean("one", false);
        fiveMile = sharedPreferences.getBoolean("five", false);
        tenMile = sharedPreferences.getBoolean("ten", false);

        Log.d("MyApp", "Got to onStartCommand in service, message = " + message + " " + oneMile.toString() + " " + fiveMile.toString() + " " + tenMile.toString());
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 0, listener);

        listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                float distanceInMeters= location.distanceTo(destination);
                float distanceInMiles = (float) (distanceInMeters/1609.344);
                locationCheck(distanceInMiles);
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
        };
        return START_STICKY;
    }

        public void locationCheck(float distanceInMiles) {

            if (message == "") {
                Log.d("MyApp", "No message to display");
                if (destinationType.equals("arriving")) {
                    Log.d("MyApp", "reached arrive clause");
                    mgr.sendTextMessage(phoneNumber, null, contactName + " has left", null, null);
                    if (tenMile) {
                        mgr.sendTextMessage(phoneNumber, null, contactName + " is ten miles away from their destination", null, null);
                    } else if (fiveMile) {
                        mgr.sendTextMessage(phoneNumber, null, contactName + " is five miles away from their destination", null, null);
                    } else if (oneMile) {
                        mgr.sendTextMessage(phoneNumber, null, contactName + " is one mile away from their destination", null, null);
                    }

                } else {
                    Log.d("MyApp", "reached pickingUp clause");
                    mgr.sendTextMessage(phoneNumber, null, "Someone is leaving to pick you up", null, null);
                    if (tenMile) //& location range is correct
                    {
                        mgr.sendTextMessage(phoneNumber, null, "Someone is ten miles away", null, null);
                    } else if (fiveMile)//& location range is correct
                    {
                        mgr.sendTextMessage(phoneNumber, null, "Someone is five miles away", null, null);
                    } else if (oneMile)//& location range is correct
                    {
                        mgr.sendTextMessage(phoneNumber, null, "Someone is one mile away", null, null);
                    }
                    //else {} //location arrive  mgr.sendTextMessage(phoneNumber,null,contactName + " has arrived", null,null);
                }

            } else {
                if (destinationType.equals("arriving")) {
                    Log.d("MyApp", "reached arrive clause");
                    mgr.sendTextMessage(phoneNumber, null, message, null, null);
                    if (tenMile) {
                        mgr.sendTextMessage(phoneNumber, null, message, null, null);
                    } else if (fiveMile) {
                        mgr.sendTextMessage(phoneNumber, null, message, null, null);
                    } else {
                        if (oneMile) {
                            mgr.sendTextMessage(phoneNumber, null, message, null, null);
                        }
                    }

                } else {
                    Log.d("MyApp", "reached pickingUp clause");
                    if (tenMile) {
                        mgr.sendTextMessage(phoneNumber, null, message, null, null);
                    } else if (fiveMile) {
                        mgr.sendTextMessage(phoneNumber, null, message, null, null);
                    } else if (oneMile) {
                        mgr.sendTextMessage(phoneNumber, null, message, null, null);
                    }
                }
            }
        }








    public class MyBinder extends Binder {
        //return an instance of the service
        locationNotifications getService() {return locationNotifications.this;}
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        return binder;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
