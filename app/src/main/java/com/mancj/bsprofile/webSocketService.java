package com.mancj.bsprofile;

import android.Manifest;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class webSocketService extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_FOO = "com.mancj.example.action.FOO";
    private static final String ACTION_BAZ = "com.mancj.example.action.BAZ";

    // TODO: Rename parameters
    private static final String EXTRA_PARAM1 = "com.mancj.example.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "com.mancj.example.extra.PARAM2";

    private LocationManager lManager;
    private SharedPreferences pref;

    public webSocketService() {
        super("WayPointsService");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionFoo(Context context, String param1, String param2) {
        Intent intent = new Intent(context, webSocketService.class);
        intent.setAction(ACTION_FOO);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
        Log.i("Send email", "Service Started Foo");
        Toast.makeText(context, "Service Started Foo" , Toast.LENGTH_LONG).show();
        locationupdateservices();

    }

    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    public static   void locationupdateservices(){
        if(LocationUpdate.locations!=null && MainActivity.map!=null) {
            double latitude = LocationUpdate.locations.getLatitude();
            double longitude = LocationUpdate.locations.getLongitude();
            Log.d("LocatinUpdate", latitude + "  --  " + longitude);
            MainActivity.updateLocation(LocationUpdate.locations);
        }

    }

    // TODO: Customize helper method
    public static void startActionBaz(Context context, String param1, String param2) {
        Intent intent = new Intent(context, webSocketService.class);
        intent.setAction(ACTION_BAZ);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
        Log.i("Send email", "Service Started");
        Toast.makeText(context, "Service Started" , Toast.LENGTH_LONG).show();
        locationupdateservices();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i("Send email", "Service Started Foo");

        locationupdateservices();
        //Toast.makeText(getApplicationContext(), "Service Started Foo" , Toast.LENGTH_LONG).show();

        try{
            try{
                MainActivity.mWebSocketClient.close();
            }catch (Exception e){

            }
            MainActivity.socketOpen();
        }catch (Exception e){

        }


    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionFoo(String param1, String param2) {
        // TODO: Handle action Foo
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private void handleActionBaz(String param1, String param2) {
        // TODO: Handle action Baz
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
