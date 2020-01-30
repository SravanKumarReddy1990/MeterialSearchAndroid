package com.mancj.bsprofile;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

public class LocationUpdate  implements LocationListener {
    private final Context mContext;
    public static Location locations;

    double latitude;
    double longitude;
    public LocationUpdate(Context context) {
        this.mContext = context;
    }

    @Override
    public void onLocationChanged(Location location) {
       // Toast.makeText(mContext, "Location Updates" , Toast.LENGTH_LONG).show();
        this.locations=location;
        latitude=location.getLatitude();
        longitude=location.getLongitude();
        if(MainActivity.map!=null) {
            Log.d("LocatinUpdate", latitude + "  --  " + longitude);
            MainActivity.updateLocation(locations);

        }
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
