package edu.villanova.tkenned8.here;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.HandlerThread;
import android.os.IBinder;
import android.telephony.SmsManager;

/**
 * Created by wildcat on 4/24/2016.
 */
public class locationNotifcations extends Service{
    SmsManager mgr = null;
    private final IBinder binder = new MyBinder();

    @Override
    public void onCreate() {
        super.onCreate();
        mgr = SmsManager.getDefault();

    }

    //needed to receive intent extras
    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        //languageReceive =(String) intent.getExtras().get("receiveLanguage");
        //languageSend = (String) intent.getExtras().get("sendLanguage");

        return START_STICKY;
    }

    public class MyBinder extends Binder {
        //return an instance of the service
        locationNotifcations getService() {return locationNotifcations.this;}
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
