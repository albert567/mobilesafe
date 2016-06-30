package com.itheima.mobilesafe.service;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.telephony.SmsManager;

import java.util.List;

/**
 * Created by zyp on 2016/6/28.
 */
public class LocationService extends Service{
    private LocationManager lm;
    private MyLocationListener listener;
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        lm = (LocationManager)getSystemService(LOCATION_SERVICE);
        List<String> names = lm.getAllProviders();
        if(names.size()>0 && names.contains("gps")){
            listener = new MyLocationListener();
            lm.requestLocationUpdates("gps",0,0,listener);
        }
    }

    private class MyLocationListener implements LocationListener{
        @Override
        public void onLocationChanged(Location location) {
            String  latitude = "la:"+location.getLatitude()+"\n";
            String longitude = "lo:"+location.getLongitude();
            SharedPreferences sp = getSharedPreferences("config",MODE_PRIVATE);
            String safenumber = sp.getString("safenumber","");
            SmsManager.getDefault().sendTextMessage(safenumber,null,latitude+longitude,null,null);
            lm.removeUpdates(listener);
            listener = null;
            stopSelf();
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
    }
}
