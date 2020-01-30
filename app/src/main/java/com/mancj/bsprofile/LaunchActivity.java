package com.mancj.bsprofile;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.Timer;
import java.util.TimerTask;

public class LaunchActivity extends AppCompatActivity {
    private Timer t;
    private String[] PERMISSIONS = {Manifest.permission.INTERNET,Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_PHONE_STATE};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);


        ActivityCompat.requestPermissions(this, PERMISSIONS, 2);
         t = new Timer();
        TimerTask task = new TimerTask() {

            @Override
            public void run() {
                ActivityCompat.requestPermissions(LaunchActivity.this, PERMISSIONS, 2);
                if (ContextCompat.checkSelfPermission(LaunchActivity.this,
                        android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        && ContextCompat.checkSelfPermission(LaunchActivity.this, Manifest.permission.READ_PHONE_STATE)
                        == PackageManager.PERMISSION_GRANTED) {
                    //locationManager.requestLocationUpdates
                    //   (locationManager.requestLocationUpdates(provider,5*60*1000,0,this);

                    Intent intent = new Intent(LaunchActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                    t.cancel();
                }else{
                    finish();
                }
            }
        };

        t.schedule(task, 5000);
    }
}
