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
import android.telephony.TelephonyManager;
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
    String mPhoneNumber;
    int countText = 0;
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
        TelephonyManager tMgr = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        mPhoneNumber = tMgr.getLine1Number();

        //Retrieve intent information
        contactName = intent.getStringExtra("contactName");
        phoneNumber = intent.getStringExtra("phoneNumber");
        destinationType = intent.getStringExtra("destinationType");
        Bundle bundle = intent.getParcelableExtra("destination");
        LatLng destinationCoord = bundle.getParcelable("destination");

        assert destinationCoord != null;
        Log.i("Location", "destinationCoord: " + destinationCoord.toString());
        double lat = destinationCoord.latitude;
        double lon = destinationCoord.longitude;
        destination.setLatitude(lat);
        destination.setLongitude(lon);
        Log.i("Location", "destination:  " + destination.toString());

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
                locationCheck(distanceInMiles, distanceInMeters);
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

        public void locationCheck(float distanceInMiles, float distanceInMeters) {

            if (message == "") {
                Log.d("MyApp", "No message to display");
                if (destinationType.equals("arriving")) {
                    Log.d("MyApp", "reached arrive clause");
                    if (countText == 0) {
                        mgr.sendTextMessage(phoneNumber, null, contactName + " has left", null, null);
                        countText++;
                    }
                    if (tenMile && (distanceInMiles < 10.5) && (distanceInMiles > 9.5)) {
                        if (countText == 1) {
                            mgr.sendTextMessage(phoneNumber, null, contactName + " is ten miles away from their destination", null, null);
                            countText++;
                        }
                    } else if (fiveMile && (distanceInMiles < 5.5) && (distanceInMiles > 4.5)) {
                        if (countText == 1 || (countText == 2 && tenMile)) {
                            mgr.sendTextMessage(phoneNumber, null, contactName + " is five miles away from their destination", null, null);
                            countText++;
                        }
                    } else if (oneMile && (distanceInMiles < 1.5) && (distanceInMiles > .5)) {
                        if (countText == 1 || (countText == 2 && tenMile) || (countText == 3 && fiveMile)) {
                            mgr.sendTextMessage(phoneNumber, null, contactName + " is one mile away from their destination", null, null);
                            countText++;
                        }
                    }
                    else if (distanceInMeters < 200) {
                        if (countText == 1 || (countText == 2 && tenMile) || (countText == 3 && fiveMile) || (countText == 4 && oneMile)) {
                            mgr.sendTextMessage(phoneNumber, null, contactName + " has arrived at their destination", null, null);
                            countText = 0;
                            stopService(new Intent (locationNotifications.this,MainActivity.class));
                        }
                    }

                } else {
                    Log.d("MyApp", "reached pickingUp clause");
                    if (countText == 0) {
                        mgr.sendTextMessage(phoneNumber, null, mPhoneNumber + " is leaving to pick you up", null, null);
                        countText++;
                    }
                    if (tenMile && (distanceInMiles <= 10))
                    {
                        if (countText == 1) {
                            mgr.sendTextMessage(phoneNumber, null, mPhoneNumber + " is ten miles away from picking you up", null, null);
                            countText++;
                        }
                    } else if (fiveMile && (distanceInMiles <= 5))//& location range is correct
                    {
                        if (countText == 1 || (countText == 2 && tenMile)) {
                            mgr.sendTextMessage(phoneNumber, null, mPhoneNumber + " is five miles away from picking you up", null, null);
                            countText++;
                        }
                    } else if (oneMile && (distanceInMiles <= 1))//& location range is correct
                    {
                        if (countText == 1 || (countText == 2 && tenMile) || (countText == 3 && fiveMile)) {
                            mgr.sendTextMessage(phoneNumber, null, mPhoneNumber + " is one mile away picking you up", null, null);
                            countText++;
                        }
                    }
                    else if (distanceInMeters < 200) {
                        if (countText == 1 || (countText == 2 && tenMile) || (countText == 3 && fiveMile) || (countText == 4 && oneMile)) {
                            mgr.sendTextMessage(phoneNumber, null, mPhoneNumber + " is here to pick you up", null, null);
                            countText = 0;
                            stopService(new Intent (locationNotifications.this,MainActivity.class));
                        }
                    }
                }

            } else {
                if (destinationType.equals("arriving")) {
                    Log.d("MyApp", "reached arrive clause");
                    if (countText == 0) {
                        mgr.sendTextMessage(phoneNumber, null, message, null, null);
                        countText++;
                    }
                    if (tenMile && (distanceInMiles < 10.5) && (distanceInMiles > 9.5)) {
                        if (countText == 1) {
                            mgr.sendTextMessage(phoneNumber, null, message, null, null);
                            countText++;
                        }
                    } else if (fiveMile && (distanceInMiles < 5.5) && (distanceInMiles > 4.5)) {
                        if (countText == 1 || (countText ==2 && tenMile)) {
                            mgr.sendTextMessage(phoneNumber, null, message, null, null);
                            countText++;
                        }
                    }
                    else if (oneMile && (distanceInMiles < 1.5) && (distanceInMiles > 4.5)) {
                        if (countText == 1 || (countText == 2 && tenMile) || (countText == 3 && fiveMile)) {
                            mgr.sendTextMessage(phoneNumber, null, message, null, null);
                            countText++;
                        }
                    }
                    else if (distanceInMeters < 200) {
                        if (countText == 1 || (countText == 2 && tenMile) || (countText == 3 && fiveMile) || (countText == 4 && oneMile)) {
                            mgr.sendTextMessage(phoneNumber, null, message, null, null);
                            countText = 0;
                            stopService(new Intent (locationNotifications.this,MainActivity.class));
                        }
                    }


                } else {
                    Log.d("MyApp", "reached pickingUp clause");
                    if (countText == 0) {
                        mgr.sendTextMessage(phoneNumber, null, message, null, null);
                        countText++;
                    }
                    if (tenMile && (distanceInMiles < 10.5) && (distanceInMiles > 9.5)) {
                        if (countText == 1) {
                            mgr.sendTextMessage(phoneNumber, null, message, null, null);
                            countText++;
                        }
                    } else if (fiveMile && (distanceInMiles < 5.5) && distanceInMiles > 4.5) {
                        if (countText == 1 || (countText == 2 && tenMile)) {
                            mgr.sendTextMessage(phoneNumber, null, message, null, null);
                            countText++;
                        }
                    } else if (oneMile && (distanceInMiles < 1.5) && (distanceInMiles > .5)) {
                        if (countText == 1 || (countText == 2 && tenMile) || (countText == 3 && fiveMile)) {
                            mgr.sendTextMessage(phoneNumber, null, message, null, null);
                            countText++;
                        }
                    }
                    else if (distanceInMeters < 200) {
                        if (countText == 1 || (countText == 2 && tenMile) || (countText == 3 && fiveMile) || (countText == 4 && oneMile)) {
                            mgr.sendTextMessage(phoneNumber, null, message, null, null);
                            countText = 0;
                            stopService(new Intent (locationNotifications.this,MainActivity.class));
                        }
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
