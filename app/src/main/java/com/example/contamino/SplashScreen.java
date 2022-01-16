package com.example.contamino;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Locale;

public class SplashScreen extends AppCompatActivity implements LocationListener {

    public static int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    LocationManager locationManager;
    double lat=0;
    ImageView logo;
    double longitude=0;
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        checklocperm();
        //   progressBar = findViewById(R.id.progressBar);
        //    progressBar.setMax(10);
        //     progressBar.setProgress(0);
        logo = findViewById(R.id.logo);
        RotateAnimation rotate = new RotateAnimation(
                0, 360,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f
        );
        rotate.setInterpolator(new LinearInterpolator());

        rotate.setDuration(900);
        rotate.setRepeatCount(Animation.INFINITE);

        logo.setAnimation(rotate);

    }

    private class yourDataTask extends AsyncTask<Void, Void, JSONObject>
    {
        String aqi_frag,pm10_frag,co_frag,o3_frag,so2_frag,last_checked,no2_frag,city_json,city_loc;
        protected void onPreExecute() {
            super.onPreExecute();
            //    progressBar = new ProgressBar(SplashScreen.this);
        }
        @Override
        protected JSONObject doInBackground(Void... params)
        {

            String str="https://api.waqi.info/feed/geo:" + lat + ";" + longitude + "/?token=be941d5d53af0932dad89815a54cd2e8f28ff0a3";
            URLConnection urlConn = null;
            BufferedReader bufferedReader = null;
            try
            {
                URL url = new URL(str);
                urlConn = url.openConnection();
                bufferedReader = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));

                StringBuffer stringBuffer = new StringBuffer();
                String line;
                while ((line = bufferedReader.readLine()) != null)
                {
                    stringBuffer.append(line);
                }
                return new JSONObject(stringBuffer.toString());
            }
            catch(Exception ex)
            {
                Log.e("App", "yourDataTask", ex);
                return null;
            }
            finally
            {
                if(bufferedReader != null)
                {
                    try {
                        bufferedReader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        @Override
        protected void onPostExecute(JSONObject response)
        {
//            progressBar.setVisibility(View.INVISIBLE);
            if(response != null)
            {
                aqi_frag=null;
                pm10_frag=null;
                co_frag=null;
                o3_frag= null;
                so2_frag=null;
                no2_frag=null;
                city_json = null;
                last_checked =null;
                city_loc = null;

                try{
                    JSONObject data = response.getJSONObject("data");
                    String aqi = data.getString("aqi");
                    aqi_frag = aqi;
                    JSONObject iaqi = data.getJSONObject("iaqi");
                    JSONObject city= data.getJSONObject("city");
                    try{
                        JSONObject pm10 = iaqi.getJSONObject("pm10");
                        pm10_frag = pm10.getString("v");
                    }catch(org.json.JSONException exception){
                        exception.printStackTrace();
                    }

                    try{
                        JSONObject co = iaqi.getJSONObject("co");
                        co_frag = co.getString("v");

                    }catch(org.json.JSONException exception){
                        exception.printStackTrace();

                    }
                    try{
                        JSONObject o3 = iaqi.getJSONObject("o3");
                        o3_frag = o3.getString("v");

                    }catch(org.json.JSONException exception){
                        exception.printStackTrace();

                    }
                    try{
                        JSONObject so2 = iaqi.getJSONObject("so2");
                        so2_frag = so2.getString("v");

                    }catch(org.json.JSONException exception){
                        exception.printStackTrace();

                    }
                    try{
                        JSONObject no2 = iaqi.getJSONObject("no2");
                        no2_frag = no2.getString("v");
                    }catch(org.json.JSONException exception){
                        exception.printStackTrace();
                    }
                    try {
                        city_json = city.getString("name");
                    }catch (org.json.JSONException exception){
                        exception.printStackTrace();
                    }
                    try {
                        JSONObject time = data.getJSONObject("time");
                        last_checked = time.getString("s");
                    }catch (org.json.JSONException exception){
                        exception.printStackTrace();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    Geocoder geocoder = new Geocoder(SplashScreen.this, Locale.getDefault());
                    List<Address> addresses = geocoder.getFromLocation(lat, longitude, 1);
                    if (addresses.get(0).getLocality() == null) {
                        if (addresses.get(0).getSubLocality() == null)
                            city_loc = city_json;
                        else if ((addresses.get(0).getSubLocality() != null))
                            city_loc = (addresses.get(0).getSubLocality());
                    } else if ((addresses.get(0).getLocality()) != null)
                        city_loc = addresses.get(0).getLocality();
                }catch (Exception e){
                    e.printStackTrace();
                }

                Intent i = new Intent(SplashScreen.this, MainActivity.class);
                i.putExtra("aqi",aqi_frag);
                i.putExtra("pm10",pm10_frag);
                i.putExtra("co",co_frag);
                i.putExtra("no2",no2_frag);
                i.putExtra("so2",so2_frag);
                i.putExtra("city",city_loc);
                i.putExtra("o3",o3_frag);
                i.putExtra("time",last_checked);
                startActivity(i);
                finish();
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);

        }
    }


    private void checklocperm() {
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            }
        }
        getLocation();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                getLocation();


            } else {

                Toast.makeText(getApplicationContext(), "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    void getLocation() {
        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 5, this);
        }
        catch(SecurityException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        lat = location.getLatitude();
        longitude = location.getLongitude();
        if(lat!=0&&longitude!=0)
            new yourDataTask().execute();

    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(SplashScreen.this, "Please Enable GPS and Internet", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }


}
