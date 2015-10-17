package com.chancorp.tabactivity;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

public class Receiver_WifiStateChange extends BroadcastReceiver {

    SharedPreferences mPref = null;
    boolean electronics = false;
    Timer mTimer = null;
    TimerTask mTimerTask = null;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (mPref == null) mPref = PreferenceManager.getDefaultSharedPreferences(context);
        electronics = mPref.getBoolean("Electronics", false);
        WifiManager wifimanager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);

        String WIFI1 = new String();
        WIFI1 = context.getResources().getString(R.string.Wifi_change1);
        String WIFI2 = new String();
        WIFI2 = context.getResources().getString(R.string.Wifi_change2);

        if (intent.getAction().equals(WIFI1) || intent.getAction().equals(WIFI2)) {
            WifiInfo wifinfo = wifimanager.getConnectionInfo();
            Log.d("FamiLink", "Info:" + wifinfo.getSSID() + wifinfo.getBSSID());
            ServerComms sc = new ServerComms();
            sc.updateStatus(new RouterInformation(wifinfo.getSSID(), wifinfo.getBSSID()), electronics,context);
            // sending boolean electronics : check whether gonna do alarm.
        }
    }
}