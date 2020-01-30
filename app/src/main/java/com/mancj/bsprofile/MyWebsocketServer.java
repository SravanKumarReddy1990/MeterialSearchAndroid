package com.mancj.bsprofile;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.TextView;

import java.net.InetSocketAddress;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.json.JSONObject;


public class MyWebsocketServer extends WebSocketServer
{
    private Context context;
    private DBM dbm;
    private LocationManager lManager;
    public MyWebsocketServer(InetSocketAddress address , Context context) {
        super(address);
        this.context=context;
        // TODO Auto-generated constructor stub
    }
    @Override
    public void onClose(WebSocket arg0, int arg1, String arg2, boolean arg3) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onError(WebSocket arg0, Exception arg1) {
        // TODO Auto-generated method stub
        Log.d("MyWebSocketServer",arg1.toString()+"Error");

    }

    @Override
    public void onMessage(WebSocket webSocket, String clientMessage) {
        // TODO Auto-generated method stub
        //MainActivity.textView.setText("From MSG: " + arg1);
        Log.d("MyWebSocketServer",clientMessage+"");
        try{
            JSONObject jsonObjectMessage=new JSONObject(clientMessage);
            if(jsonObjectMessage.getString("contenttype").equals("fulldetial")){
                String logid=jsonObjectMessage.getString("logid");
                dbm=new DBM(context);
                JSONObject fulldetails=new JSONObject();
                fulldetails.put("personaldata",dbm.getPersonalData(logid).toString());
                fulldetails.put("yourservicedata",dbm.getYourServiceData(logid).toString());
                fulldetails.put("skillsetdata",dbm.getSkillsetData(logid).toString());
                fulldetails.put("educationdata",dbm.getEducationData(logid).toString());
                fulldetails.put("workdata",dbm.getWorkData(logid).toString());
                webSocket.send(fulldetails.toString());

            }if(jsonObjectMessage.getString("contenttype").equals("location")){
                String logid=jsonObjectMessage.getString("logid");
                dbm=new DBM(context);
                JSONObject fulldetails=new JSONObject();
                fulldetails.put("locationdata",dbm.getLocationData(logid).toString());
                     webSocket.send(fulldetails.toString());


            }if(jsonObjectMessage.getString("contenttype").equals("skillsearch")){
                String logid=jsonObjectMessage.getString("logid");
                String skills=jsonObjectMessage.getString("skills");
                dbm=new DBM(context);
                JSONObject fulldetails=new JSONObject();
                fulldetails.put("personaldata",dbm.getPersonalData(logid).toString());
                fulldetails.put("yourservicedata",dbm.getYourServiceData(logid).toString());
                fulldetails.put("skillsetdata",dbm.getSkillsetData(logid).toString());
                fulldetails.put("educationdata",dbm.getEducationData(logid).toString());
                fulldetails.put("workdata",dbm.getWorkData(logid).toString());
                String[] skill=skills.split(",");
                for(int i=0;i<skill.length;i++){
                    if(fulldetails.toString().toLowerCase().indexOf(skill[i].toLowerCase())!=-1) {
                        webSocket.send(fulldetails.toString());
                    }
                }

            }if(jsonObjectMessage.getString("contenttype").equals("getmobileno")){
                String logid=jsonObjectMessage.getString("logid");
                dbm=new DBM(context);
                JSONObject fulldetails=new JSONObject();
                if (ContextCompat.checkSelfPermission(context,
                        Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {

                }
                try {
                    TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                    fulldetails.put("status",1);
                    fulldetails.put("mobileno",tm.getLine1Number());
                    webSocket.send(fulldetails.toString());
                }catch (Exception e){
                    fulldetails.put("status",0);
                    fulldetails.put("mobileno","permission denied");
                    webSocket.send(fulldetails.toString());
                }
            }else if(jsonObjectMessage.getString("contenttype").equals("presentlocation")){
                lManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
                boolean netEnabled = lManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                Log.i("Location Enabled", netEnabled+"");
                if (netEnabled) {
                    if ( ContextCompat.checkSelfPermission( context, Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {

                    }
                    Location location = lManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    //Toast.makeText(this.getApplicationContext(), "Max Satilites Found Now is "+satiliteCount, Toast.LENGTH_SHORT).show();
                    if(location!=null) {
                        Log.i("Location Service", location.getLatitude() + " " + location.getLongitude());
                        JSONObject fulldetails=new JSONObject();
                        fulldetails.put("latitude",location.getLatitude());
                        fulldetails.put("longitude",location.getLongitude());
                        webSocket.send(fulldetails.toString());
                    }else{
                        Log.i("Location Service",  " is null");

                    }
                }

            }
        }catch (Exception e){
            Log.e("MyWebSocketServer",clientMessage+"Error at onMessage");
        }
    }

    @Override
    public void onOpen(WebSocket arg0, ClientHandshake arg1) {
        // TODO Auto-generated method stub
        Log.d("MyWebSocketServerOnOpen",arg0.getRemoteSocketAddress()+"");
        //MainActivity.textView.setText("new connection to " + arg0.getRemoteSocketAddress());
        //System.out.println("new connection to " + arg0.getRemoteSocketAddress());
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("status", 0);
            jsonObject.put("message","Connected");
            arg0.send(jsonObject.toString());
        }catch (Exception e){

        }
    }
}
