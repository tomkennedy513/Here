package edu.villanova.tkenned8.here;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.audiofx.BassBoost;
import android.net.Uri;
import android.os.Binder;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.provider.Telephony;
import android.telephony.SmsManager;
import android.util.Log;

/**
 * Created by wildcat on 4/24/2016.
 */
public class locationNotifications extends Service{
    SmsManager mgr = null;
    private final IBinder binder = new MyBinder();
    String message;
    Boolean oneMile;
    Boolean fiveMile;
    Boolean tenMile;
    String contactName;
    String phoneNumber;
    String destinationType;
    //BroadcastReceiver receiver;
    //HandlerThread schedulr;

    @Override
    public void onCreate() {
        super.onCreate();

        mgr = SmsManager.getDefault();
        Log.d("MyApp","Got to onCreate in service");

        /*IntentFilter receiverFilter = new IntentFilter(Telephony.Sms.Intents.SMS_DELIVER_ACTION);
        //receiver = new MyReceiver();
        schedulr = new HandlerThread("ht");
        schedulr.start();
        Looper loop = schedulr.getLooper();
        Handler handler = new Handler(loop);
        registerReceiver(receiver, receiverFilter, null, handler);*/


    }


    //needed to receive intent extras
    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        contactName = intent.getStringExtra("contactName");
        phoneNumber = intent.getStringExtra("phoneNumber");
        destinationType = intent.getStringExtra("destinationType");

        Log.d("MyApp","Contactname = " + contactName + " Phonenumber = " + phoneNumber + " destinationType = " + destinationType);

        //get Settings from Shared Preferences
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        message = sharedPreferences.getString("text",null);
        oneMile = sharedPreferences.getBoolean("one",false);
        fiveMile = sharedPreferences.getBoolean("five", false);
        tenMile = sharedPreferences.getBoolean("ten", false);

        Log.d("MyApp","Got to onStartCommand in service, message = " + message + " " + oneMile.toString() + " " + fiveMile.toString() + " " + tenMile.toString());


        if(message == "")
        {
            Log.d("MyApp","No message to display");
            if(destinationType.equals("arriving"))
            {
                Log.d("MyApp","reached arrive clause");
                mgr.sendTextMessage(phoneNumber,null,contactName + " has left",null,null);
                if(tenMile == true)
                {
                    mgr.sendTextMessage(phoneNumber,null,contactName + " is ten miles away from their destination", null,null);
                }
                else if (fiveMile == true)
                {
                    mgr.sendTextMessage(phoneNumber,null,contactName + " is five miles away from their destination", null,null);
                }
                else if (oneMile == true)
                {
                    mgr.sendTextMessage(phoneNumber,null,contactName + " is one mile away from their destination", null,null);
                }

            }
            else {
                Log.d("MyApp","reached pickingUp clause");
                mgr.sendTextMessage(phoneNumber,null,contactName + " is leaving to pick you up", null,null);
                if(tenMile == true) //& location range is correct
                {
                    mgr.sendTextMessage(phoneNumber,null,contactName + " is ten miles away", null,null);
                }
                else if (fiveMile == true)//& location range is correct
                {
                    mgr.sendTextMessage(phoneNumber,null,contactName + " is five miles away", null,null);
                }
                else if (oneMile == true)//& location range is correct
                {
                    mgr.sendTextMessage(phoneNumber,null,contactName + " is one mile away", null,null);
                }
                //else {} //location arrive  mgr.sendTextMessage(phoneNumber,null,contactName + " has arrived", null,null);
            }

        }
        else {
            if(destinationType.equals("arriving"))
            {
                Log.d("MyApp","reached arrive clause");
                mgr.sendTextMessage(phoneNumber,null,message,null,null);
                if(tenMile == true)
                {
                    mgr.sendTextMessage(phoneNumber,null,message, null,null);
                }
                else if (fiveMile == true)
                {
                    mgr.sendTextMessage(phoneNumber,null,message, null,null);
                }
                else if (oneMile == true)
                {
                    mgr.sendTextMessage(phoneNumber,null,message, null,null);
                }

            }
            else {
                Log.d("MyApp","reached pickingUp clause");
                if(tenMile == true)
                {
                    mgr.sendTextMessage(phoneNumber,null,message, null,null);
                }
                else if (fiveMile == true)
                {
                    mgr.sendTextMessage(phoneNumber,null,message, null,null);
                }
                else if (oneMile == true)
                {
                    mgr.sendTextMessage(phoneNumber,null,message, null,null);
                }
            }
        }

        return START_STICKY;
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
