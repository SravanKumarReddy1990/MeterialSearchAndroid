package com.mancj.bsprofile;

import android.Manifest;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.mancj.materialsearchbar.MaterialSearchBar;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_17;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONObject;
import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.constants.OpenStreetMapTileProviderConstants;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, MaterialSearchBar.OnSearchActionListener {
    MaterialSearchBar searchBar;
    private DrawerLayout drawer;
    private String mobileno;
    private SharedPreferences pref;
    private ImageButton gpsButton;
    private AlarmManager waypointalarm;
    private PendingIntent waypointpintent;
    public static WebSocketClient mWebSocketClient;
    private View mainv;
    private View chatmsgv;
    private NavigationView navigationView;
    private DBM dbm;
    private EditText messageET;
    private ListView messagesContainer;
    private Button sendBtn;
    private ChatAdapter adapter;
    private ArrayList<ChatMessage> chatHistory;
    public static MapView map;
    public static MainActivity mainActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String[] PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_PHONE_STATE};

        ActivityCompat.requestPermissions(this, PERMISSIONS, 1);

        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

        }
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
       try {
           TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
           mobileno = tm.getLine1Number();
       }catch (Exception e){
           finish();
       }
        dbm=new DBM(MainActivity.this);

        pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        mainActivity=this;
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        View header = navigationView.getHeaderView(0);
        TextView text = (TextView) header.findViewById(R.id.navemail);
        //getActionBar().setDisplayHomeAsUpEnabled(true);
        navigationView.setNavigationItemSelectedListener(this);
        searchBar = (MaterialSearchBar) findViewById(R.id.searchBar);
        searchBar.setOnSearchActionListener(this);
        searchBar.inflateMenu(R.menu.main);
        searchBar.setText("Search Here");
        Log.d("LOG_TAG", getClass().getSimpleName() + ": text " + searchBar.getText());
        searchBar.setCardViewElevation(10);
        searchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.d("LOG_TAG", getClass().getSimpleName() + " text changed " + searchBar.getText());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }

        });

        mainv=findViewById(R.id.mainview);
        chatmsgv=findViewById(R.id.chatmessageview);
        mainv.setVisibility(View.GONE);
        chatmsgv.setVisibility(View.GONE);
        OpenStreetMapTileProviderConstants.setUserAgentValue(BuildConfig.APPLICATION_ID);
        map = (MapView) findViewById(R.id.map);
        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);

        //SharedPreferences pref = getSharedPreferences("MyPref", MODE_PRIVATE);
//        String iden=pref.getString("logid", null);
//        String idtype=pref.getString("usertype", null);
//        if(iden==null){
//            /*loginv.setVisibility(View.VISIBLE);
//            mainv.setVisibility(View.GONE);
//            regv.setVisibility(View.GONE);*/
//            displayByName("loglayout");
//        }else{
//            displayByName("mainlayout");
//
//
//        }
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Settings.System.canWrite(MainActivity.this)) {
                // Do stuff here
            }
            else {
                Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_WRITE_SETTINGS);
                intent.setData(Uri.parse("package:" + MainActivity.this.getPackageName()));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        }*/

        connectWebSocket();

        //mWebSocketClient.send("");
    }
    public void onClickButton(View view){
        int id=view.getId();
        if(id==R.id.gps){
            gpsButton=  mainv.findViewById(R.id.gps);
            if (view.getTag().toString().equalsIgnoreCase("gpsoff")) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    gpsButton.setImageDrawable(getResources().getDrawable(R.drawable.gpson, getApplicationContext().getTheme()));
                } else {
                    gpsButton.setImageDrawable(getResources().getDrawable(R.drawable.gpson));
                }
                view.setTag("gpson");
                Calendar cal = Calendar.getInstance();
                Intent intent = new Intent(this, webSocketService.class);
                waypointpintent = PendingIntent.getService(this, 0, intent, 0);
                waypointalarm = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
                Log.d("Main",String.valueOf( cal.getTimeInMillis()));
                //make the alarm goes off every 10 sec (not exact help to save battery life)
                waypointalarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), 1500, waypointpintent);
               // wifiOn();
            }else{
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    gpsButton.setImageDrawable(getResources().getDrawable(R.drawable.gpsoff, getApplicationContext().getTheme()));
                } else {
                    gpsButton.setImageDrawable(getResources().getDrawable(R.drawable.gpsoff));
                }
                waypointalarm.cancel(waypointpintent);
                view.setTag("gpsoff");
               // wifiOff();
            }
        }else if(id==R.id.pointer){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                }
            });
        }else if(id==R.id.pointer){

        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        Toast.makeText(MainActivity.this, item.getTitle(), Toast.LENGTH_LONG).show();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Toast.makeText(MainActivity.this, item.getTitle(), Toast.LENGTH_LONG).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            //Intent navhome=new Intent(MainActivity.this,LoginActivity.class);
            //startActivity(navhome);
            displayByName("mainlayout");

            //map.setTileSource(TileSourceFactory.MAPNIK);
        } else if (id == R.id.nav_messages) {
            displayByName("messageslayout");
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onSearchStateChanged(boolean enabled) {
    }

    @Override
    public void onSearchConfirmed(CharSequence text) {

    }

    @Override
    public void onButtonClicked(int buttonCode) {
        switch (buttonCode){
            case MaterialSearchBar.BUTTON_NAVIGATION:
                drawer.openDrawer(Gravity.LEFT);
                break;
            case MaterialSearchBar.BUTTON_SPEECH:
                break;
            case MaterialSearchBar.BUTTON_BACK:
                searchBar.disableSearch();
                break;
        }
    }
    public static String callWebServices(String link){
        String webPage = "";
        try {
            URL url = new URL(link);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.connect();
            InputStream is = conn.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String data = "";

            while ((data = reader.readLine()) != null) {
                webPage += data + "\n";
            }
        }catch (Exception e){

        }
        return webPage;
    }
    public void displayMessage(ChatMessage message) {
        adapter.add(message);
        adapter.notifyDataSetChanged();
        scroll();
    }

    private void scroll() {
        messagesContainer.setSelection(messagesContainer.getCount() - 1);
    }

    private void loadDummyHistory(){

        chatHistory = dbm.getMessagesData(pref.getString("logid",null));

        /*ChatMessage msg = new ChatMessage();
        msg.setId(1);
        msg.setMe(false);
        msg.setMessage("Hi");
        msg.setDate(DateFormat.getDateTimeInstance().format(new Date()));
        chatHistory.add(msg);
        ChatMessage msg1 = new ChatMessage();
        msg1.setId(2);
        msg1.setMe(true);
        msg1.setMessage("How r u doing???");
        msg1.setDate(DateFormat.getDateTimeInstance().format(new Date()));
        chatHistory.add(msg1);*/

        adapter = new ChatAdapter(MainActivity.this, new ArrayList<ChatMessage>());
        messagesContainer.setAdapter(adapter);

        for(int i=0; i<chatHistory.size(); i++) {
            ChatMessage message = chatHistory.get(i);
            displayMessage(message);
        }

    }
    private void displayByName(String layoutName) {
        connectWebSocket();
        if (layoutName.equalsIgnoreCase("mainlayout")) {
            mainv.setVisibility(View.VISIBLE);
            chatmsgv.setVisibility(View.GONE);
            boolean permissionAccessCoarseLocationApproved = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;

            if (permissionAccessCoarseLocationApproved) {
            } else {

            }
            LocationManager locationManager  = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE); //<2>
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER); //<5>
            LocationListener locationUpdate=new  LocationUpdate(this);
            if (location != null) {
                locationUpdate.onLocationChanged(location); //<6>
            }
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0,locationUpdate);

            Toast.makeText(this, "Location Updation" , Toast.LENGTH_LONG).show();
        }if (layoutName.equalsIgnoreCase("messageslayout")) {
            chatmsgv.setVisibility(View.VISIBLE);
            mainv.setVisibility(View.GONE);
            messagesContainer = (ListView) findViewById(R.id.messagesContainer);
            messageET = (EditText) findViewById(R.id.messageEdit);
            sendBtn = (Button) findViewById(R.id.chatSendButton);

            TextView meLabel = (TextView) findViewById(R.id.meLbl);
            TextView companionLabel = (TextView) findViewById(R.id.friendLabel);
            RelativeLayout container = (RelativeLayout) findViewById(R.id.container);
            companionLabel.setText("Companies");

            loadDummyHistory();
            sendBtn.setEnabled(false);
            sendBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String messageText = messageET.getText().toString();
                    if (TextUtils.isEmpty(messageText)) {
                        return;
                    }

                    ChatMessage chatMessage = new ChatMessage();
                    chatMessage.setId(122);//dummy
                    chatMessage.setMessage(messageText);
                    chatMessage.setDate(DateFormat.getDateTimeInstance().format(new Date()));
                    chatMessage.setMe(true);
                    JSONObject messages=new JSONObject();
                   // String logid=jsonObjectMessage.getString("logid");
                    //messages.put("logid",logid);
                    //messages.put("from",jsonObjectMessage.getString("from"));
                    //messages.put("to",jsonObjectMessage.getString("to"));
                    //messages.put("message",jsonObjectMessage.getString("content"));
                    //Log.e("MyWebSocketClient",messages.toString());
                    //dbm.insertMessages(messages,1);
                    messageET.setText("");

                   displayMessage(chatMessage);
                }
            });
        }
    }
    //JSONObject jsonObjectServer=new JSONObject();
    public static void updateLocation(final Location location){
        mainActivity.runOnUiThread(new Runnable() {
            @Override

            public void run() {
                try {
                map.getOverlays().remove(0);
            }catch (Exception e){}
                IMapController mapController = map.getController();
                mapController.setZoom(15);
                GeoPoint point = new GeoPoint(location);
                mapController.animateTo(point);
                AccuracyOverlay accuracyOverlay = new AccuracyOverlay(point, location.getAccuracy());
                map.getOverlays().add(accuracyOverlay);
                try {

                    JSONObject jsonObjectMain = new JSONObject();
                    jsonObjectMain.put("from", "sravan");
                    jsonObjectMain.put("to", "all");
                    jsonObjectMain.put("content", location.getLongitude()+","+location.getLatitude());
                    jsonObjectMain.put("contentType", "loc");
                    jsonObjectMain.put("status", "hi");
                    mWebSocketClient.send(jsonObjectMain.toString());
                }catch (Exception e){
                    Log.e("LocationUpdate : ",e+"");
                }
            }
        });
    }

    //JSONObject jsonObjectServer=new JSONObject();
    public static void socketOpen(){
        mainActivity.connectWebSocket();
    }
    private void connectWebSocket() {
        URI uri=null;
        try {
            String isd=pref.getString("logid",null);
            if(isd!=null){
                isd=isd.trim();
            }else{

               // return;
            }
            uri = new URI("ws://part1290.herokuapp.com/chat/sravan");
        } catch (Exception e) {
            e.printStackTrace();
           // return;
        }

        mWebSocketClient = new WebSocketClient(uri,new Draft_17()) {
            @Override
            public void onOpen(ServerHandshake serverHandshake) {
                 Log.i("Websocket", "Opened");
                //mWebSocketClient.send("Hello from " + Build.MANUFACTURER + " " + Build.MODEL);
            }


            @Override
            public void onMessage(String s) {
                Log.i("WebsocketClient", s);
                try {
                    JSONObject jsonObjectMessage = new JSONObject(s);
                    String fromper=jsonObjectMessage.getString("from");
                    if(fromper.indexOf("chat")==-1){
                    try{

                    }catch (Exception e){
                        Log.e("MyWebSocketClient",e+"Error at onMessage");
                    }
                    }else{
                        //MainActivity.this.loadDummyHistory();
                    }

                }catch (Exception e){

                    Log.e("MyWebSocketClient",e+"Error at onMessage");
                }

            }

            @Override
            public void onClose(int i, String s, boolean b) {
                Log.i("Websocket", "Closed " + s);
                connectWebSocket();
            }

            @Override
            public void onError(Exception e) {
                Log.i("Websocket", "Error " + e.getMessage());
                //connectWebSocket();
            }
        };
        mWebSocketClient.connect();
    }
    public String getNetworkInterfaceIpAddress1() {

        try {
            String webString=callWebServices("https://api.ipify.org/?format=json");
            String ip=new JSONObject(webString).getString("ip");

            android.provider.Settings.System.putString(getContentResolver(), android.provider.Settings.System.WIFI_STATIC_IP, ip);
            android.provider.Settings.System.putString(getContentResolver(), android.provider.Settings.System.WIFI_USE_STATIC_IP, "1");
            return ip;
        } catch (Exception ex) {
            Log.e("IP Address", "getLocalIpAddress", ex);
        }
        return null;
    }

    public String getNetworkInterfaceIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface networkInterface = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = networkInterface.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                        String host = inetAddress.getHostAddress();
                        if (!TextUtils.isEmpty(host)) {
                            return host;
                        }
                    }
                }

            }
        } catch (Exception ex) {
            Log.e("IP Address", "getLocalIpAddress", ex);
        }
       /* try {
        String webString=callWebServices("https://api.ipify.org/?format=json");
        String ip=new JSONObject(webString).getString("ip");

            android.provider.Settings.System.putString(getContentResolver(), android.provider.Settings.System.WIFI_STATIC_IP, ip);
            android.provider.Settings.System.putString(getContentResolver(), android.provider.Settings.System.WIFI_USE_STATIC_IP, "1");
        return ip;
        } catch (Exception ex) {
            Log.e("IP Address", "getLocalIpAddress", ex);
        }*/
        return null;
    }


}
