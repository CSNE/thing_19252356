package com.chancorp.tabactivity;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class Service_Lockscreen extends Service {
    String WIFI1 = new String();
    String WIFI2 = new String();
    NotificationManager ntm = null;
    Notification ntf;

    private Receiver_Lockscreen mreceiver = null;
    // here, we right all final Intent receivers and add intent.
    IntentFilter intent_filter;
    //
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onDestroy() {
        ntf = null;
        ntm = null;
        super.onDestroy();
        unregisterReceiver(mreceiver);
        return;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        if(intent != null) {
            if(intent.getAction() == null) {
                if(mreceiver == null) {
                    mreceiver = new Receiver_Lockscreen();
                    intent_filter = new IntentFilter();
                    intent_filter.addAction(WIFI1);
                    intent_filter.addAction(WIFI2);
                    Log.d("tag", "Also this part is running!");
                    registerReceiver(mreceiver, intent_filter);
                }
            }
        }
        version_check_and_startForeground();
        return START_REDELIVER_INTENT;
    }

    public void version_check_and_startForeground() {
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            startForeground(1, new Notification());
        } else {
            startForeground(1, new Notification());
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        WIFI1 = getString(R.string.Wifi_change1);
        WIFI2 = getString(R.string.Wifi_change2);
        mreceiver = new Receiver_Lockscreen();
        intent_filter = new IntentFilter();
        intent_filter.addAction(WIFI1);
        intent_filter.addAction(WIFI2);
        registerReceiver(mreceiver, intent_filter);
    }
}
