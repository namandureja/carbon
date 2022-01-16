package com.example.contamino;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.arch.core.util.Function;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.core.widget.ImageViewCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.transition.ChangeBounds;
import androidx.transition.Transition;
import androidx.transition.TransitionManager;
import androidx.vectordrawable.graphics.drawable.ArgbEvaluator;

import android.Manifest;
import android.animation.ValueAnimator;
import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.etebarian.meowbottomnavigation.MeowBottomNavigationKt;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.TimeZone;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class MainActivity extends AppCompatActivity  {

    private TextView mTextViewResult,locationText,raw;
    private RequestQueue mQueue;
    public static int res=1;
    public static MeowBottomNavigation navView;
    public static MainActivity instance;
    public static int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    LocationManager locationManager;
    static long cigtextupdate;
    public static String cityname;
    double lat,longitude;
    ImageView circle;
    public static double x_cord = 0,y_cord = 0;
    public static ImageView home,more;
    public static String aqi_frag,city_frag,pm10_frag,o3_frag,so2_frag,no2_frag,co_frag,city_json,last_checked;
    public static long life_frag,cig_frag,time_frag;
    double aqi_high,aqi_low,conc_high,conc_low,aqi_value,conc;
    private ConstraintLayout constraint;
    private ConstraintSet constraintSet1 = new ConstraintSet();
    private ConstraintSet constraintSet2 = new ConstraintSet();



    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment =null;
            int n=-1,j=-1;
            switch (item.getItemId()) {
                case R.id.navigation_home:

                    break;
                case R.id.navigation_more:
                    changeFragment(new MoreFragment(), "more");
                    break;
            }

            return true;
        }
    };

    private int nav_boolean=2;

    public void changeFragment(Fragment fragment, String tagFragmentName) {
        FragmentManager mFragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        if(tagFragmentName.equalsIgnoreCase("more"))
            fragmentTransaction.setCustomAnimations(R.anim.enter_left_anim, R.anim.exit_right_anim, R.anim.enter_right_anim, R.anim.exit_left_anim);
        else if(tagFragmentName.equalsIgnoreCase("home"))
            fragmentTransaction.setCustomAnimations(R.anim.enter_right_anim, R.anim.exit_left_anim, R.anim.enter_left_anim, R.anim.exit_right_anim);

        Fragment currentFragment = mFragmentManager.getPrimaryNavigationFragment();
        if (currentFragment != null) {
            fragmentTransaction.hide(currentFragment);
        }

        Fragment fragmentTemp = mFragmentManager.findFragmentByTag(tagFragmentName);
        if (fragmentTemp == null) {
            fragmentTemp = fragment;
            fragmentTransaction.add(R.id.frame, fragmentTemp, tagFragmentName);

        } else {
            fragmentTransaction.show(fragmentTemp);
        }
        fragmentTransaction.setPrimaryNavigationFragment(fragmentTemp);
        fragmentTransaction.setReorderingAllowed(true);
        fragmentTransaction.commitNowAllowingStateLoss();
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
         instance = new MainActivity();
         setContentView(R.layout.activity_main);
         navView = findViewById(R.id.nav_view);
        Fragment fragment = null;
        fragment = new HomeFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.frame,fragment).commit();





        /**
        navView.add(new MeowBottomNavigation.Model(1, R.drawable.smogill_blck));
        navView.add(new MeowBottomNavigation.Model(2, R.drawable.homeill_black));
        navView.show(2,true);
        navView.setOnClickMenuListener(new Function1<MeowBottomNavigation.Model, Unit>() {
            @Override
            public Unit invoke(MeowBottomNavigation.Model model) {
                if(model.getId()==2)
                    changeFragment(new HomeFragment(),"home");
                if(model.getId()==1)
                    changeFragment(new MoreFragment(),"more");

                return null;
            }
        });
         **/

         home = findViewById(R.id.home_button);
         more = findViewById(R.id.more_button);
         constraint = findViewById(R.id.main_layout);
         constraintSet1.clone(constraint);
         constraintSet2.clone(this,R.layout.activity_main_more);
         final int colorFrom = getResources().getColor(R.color.black);
         final int colorTo = getResources().getColor(R.color.white);

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeFragment(new HomeFragment(),"home");
                swapview(1);
                colorchange(colorTo,colorFrom,more);
                colorchange(colorFrom,colorTo,home);
            }
        });

        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeFragment(new MoreFragment(),"more");
                colorchange(colorFrom,colorTo,more);
                colorchange(colorTo,colorFrom,home);
                swapview(2);

            }
        });



   //     getLocation();
        mQueue = Volley.newRequestQueue(this);
        Intent intent = getIntent();
        String aqi = intent.getStringExtra("aqi");
        String city = intent.getStringExtra("city");
        String time = intent.getStringExtra("time");
        String pm10 = intent.getStringExtra("pm10");
        String co = intent.getStringExtra("co");
        String o3 = intent.getStringExtra("o3");
        String so2 = intent.getStringExtra("so2");
        String no2 = intent.getStringExtra("no2");
    //    Toast.makeText(this, aqi + city + pm10 + co+o3,Toast.LENGTH_SHORT).show();
        setdata(aqi,city,time,pm10,co,o3,so2,no2);

    }

    private void setdata(String aqi, String city, String time, String pm10, String co, String o3, String so2, String no2) {
        pm10_frag=pm10;
        co_frag=co;
        o3_frag= o3;
        so2_frag=so2;
        no2_frag=no2;

        try {
          //  change_city_main(city);
            aqi_frag=aqi;
            city_frag = city;
            if (aqi != null) {
                calculateconc(Integer.parseInt(aqi));
            }
            if (time != null) {
                calculatetime(time);
            }

        }catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    public void colorchange(int colorFrom, int colorTo, final ImageView more) {
        final ValueAnimator colorAnimation1 = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
        colorAnimation1.setDuration(250); // milliseconds
        colorAnimation1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                ImageViewCompat.setImageTintList(more, ColorStateList.valueOf((int) animator.getAnimatedValue()));

            }

        });
        colorAnimation1.start();

    }

    public void swapview(int i) {
        TransitionManager.beginDelayedTransition(constraint);

        if(i==1) {
            constraintSet1.applyTo(constraint);
         //   home.setImageResource(R.drawable.homeill_white);
         //   more.setImageResource(R.drawable.smogill_blck);
        }else if(i==2) {
            constraintSet2.applyTo(constraint);
           // more.setImageResource(R.drawable.smogill_white);
           // home.setImageResource(R.drawable.homeill_black);
        }

    }



    private void change_city_main(String city) {
        try {
    //        HomeFragment.cityname.setText(city_frag);

            if(city_frag.equalsIgnoreCase("gurgaon") || city_frag.equalsIgnoreCase("gurugram"))
            {
                HomeFragment.delhi.setVisibility(View.INVISIBLE);
                HomeFragment.ggn.setVisibility(View.VISIBLE);
                HomeFragment.general.setVisibility(View.INVISIBLE);
                HomeFragment.london.setVisibility(View.INVISIBLE);
                HomeFragment.mumbai.setVisibility(View.INVISIBLE);

            }else
                if(city_frag.equalsIgnoreCase("mumbai") || city_frag.equalsIgnoreCase("bombay") || city_frag.equalsIgnoreCase("Navi Mumbai"))
            {
              HomeFragment.delhi.setVisibility(View.INVISIBLE);
                HomeFragment.mumbai.setVisibility(View.VISIBLE);
                HomeFragment.general.setVisibility(View.INVISIBLE);
                HomeFragment.ggn.setVisibility(View.INVISIBLE);
                HomeFragment.london.setVisibility(View.INVISIBLE);
            }else            if(city_frag.equalsIgnoreCase("london") || city_frag.equalsIgnoreCase("paris") || city_frag.contains("London"))
            {
                HomeFragment.delhi.setVisibility(View.INVISIBLE);
                HomeFragment.mumbai.setVisibility(View.INVISIBLE);
                HomeFragment.general.setVisibility(View.INVISIBLE);
                HomeFragment.ggn.setVisibility(View.INVISIBLE);
                HomeFragment.london.setVisibility(View.VISIBLE);
            }else            if(city_frag.equalsIgnoreCase("delhi") || city_frag.equalsIgnoreCase("new delhi"))
            {
                HomeFragment.delhi.setVisibility(View.VISIBLE);
                HomeFragment.mumbai.setVisibility(View.INVISIBLE);
                HomeFragment.general.setVisibility(View.INVISIBLE);
                HomeFragment.ggn.setVisibility(View.INVISIBLE);
                HomeFragment.london.setVisibility(View.INVISIBLE);
            } else {
                HomeFragment.general.setVisibility(View.VISIBLE);
                HomeFragment.delhi.setVisibility(View.INVISIBLE);
                HomeFragment.mumbai.setVisibility(View.INVISIBLE);
                HomeFragment.ggn.setVisibility(View.INVISIBLE);
                HomeFragment.london.setVisibility(View.INVISIBLE);
            }

        }catch(Exception e)
        {
            e.printStackTrace();

        }
    }



    public void jsonParse(final double latitude_json, final double longi_json) {
        aqi_frag=null;
        pm10_frag=null;
        co_frag=null;
        o3_frag= null;
        so2_frag=null;
        no2_frag=null;
        last_checked = null;

        String url = "https://api.waqi.info/feed/geo:" + latitude_json + ";" + longi_json + "/?token=be941d5d53af0932dad89815a54cd2e8f28ff0a3";


        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject data = response.getJSONObject("data");
                            String aqi = data.getString("aqi");
                            aqi_frag = aqi;
                            HomeFragment.aqi.setText(aqi);
                            calculateconc(Integer.parseInt(aqi));
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

                       //    Toast.makeText(MainActivity.this,aqi_frag+pm10_frag+longi_json+latitude_json,Toast.LENGTH_SHORT).show();
                            if (last_checked != null) {
                                calculatetime(last_checked);
                            }else calculatetime("false");

                            if(res==0) {
                                update_city(latitude_json, longi_json,city_json);
                                Fragment fragment = new MoreFragment();
                                res=1;
                              //    Toast.makeText(MainActivity.this,aqi_frag+pm10_frag+longi_json+latitude_json,Toast.LENGTH_SHORT).show();

                                getSupportFragmentManager().beginTransaction().replace(R.id.frame, fragment).commitAllowingStateLoss();
                            } else {
                                change_city_main(city_json);
                            }



                            /**

                            JSONArray jsonArray = response.getJSONArray("employees");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject employee = jsonArray.getJSONObject(i);

                                String firstName = employee.getString("firstname");
                                int age = employee.getInt("age");
                                String mail = employee.getString("mail");

                                mTextViewResult.append(firstName + ", " + String.valueOf(age) + ", " + mail + "\n\n");
                            }
                             **/
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        mQueue.add(request);

    }

    private void calculatetime(String last_checked) {
        if(!last_checked.equals("false")) {
            char[] calc_time = new char[10];
            int k = 0;
            char[] time = last_checked.toCharArray();
            for (int i = 0; i < time.length; i++) {
                if (time[i] == ' ') {
                    k = i;
                    break;
                }
            }
            for (int i = 0; k < time.length; k++, i++) {
                calc_time[i] = time[k];
            }
            String string = String.valueOf(calc_time[1]).concat(String.valueOf(calc_time[2]));
            int sub = Integer.parseInt(string);
            if (sub >= 12)
                sub = sub - 12;
            Calendar cal = Calendar.getInstance(TimeZone.getDefault());
            int currentHour = cal.get(Calendar.HOUR);
            double x = (Math.random() * ((3 - 1) + 1)) + 1;
            time_frag = currentHour - sub + (Double.valueOf(x)).longValue();
        }else
            time_frag = 0;

    }
    public void update_city(double x,double y,String city){
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        try {
            List<Address> addresses = null;
            addresses = geocoder.getFromLocation(x, y, 1);

            if(addresses.get(0).getLocality()==null){
                if(addresses.get(0).getSubLocality()==null)
                    if(city!=null)
                        cityname = city;
                    else
                    cityname = (addresses.get(0).getCountryName());
                else if((addresses.get(0).getSubLocality()!=null))
                    cityname = (addresses.get(0).getSubLocality());
            } else if ((addresses.get(0).getLocality())!=null)
                cityname = addresses.get(0).getLocality();
            city_frag= cityname;
            HomeFragment.cityname.setText(cityname);
            MoreFragment.cityname.setText(cityname);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }




    private void calculateconc(int aqi) {
        if(aqi>=0&&aqi<=50)
        {
            aqi_high = 50;
            aqi_low = 0;
            conc_high = 12.0;
            conc_low = 0.0;
        }else        if(aqi>=51&&aqi<=100)
        {
            aqi_high = 100;
            aqi_low = 51;
            conc_high = 35.4;
            conc_low = 12.1;
        }else        if(aqi>=101&&aqi<=150)
        {
            aqi_high = 150;
            aqi_low = 101;
            conc_high = 55.4;
            conc_low = 35.5;
        }else        if(aqi>=151&&aqi<=200)
        {
            aqi_high = 200;
            aqi_low = 151;
            conc_high = 150.4;
            conc_low = 55.5;
        }else        if(aqi>=201&&aqi<=300)
        {
            aqi_high = 300;
            aqi_low = 201;
            conc_high =250.4;
            conc_low = 150.5;
        }else        if(aqi>=301&&aqi<=400)
        {
            aqi_high = 400;
            aqi_low = 301;
            conc_high = 350.4;
            conc_low = 250.5;
        }else if(aqi>=401&&aqi<=500)
        {
            aqi_high = 500;
            aqi_low = 401;
            conc_high = 500.4;
            conc_low = 350.5;
        }else if (aqi>500)
            conc = aqi;
        if(aqi<=500){
            conc = ((conc_high-conc_low)/(aqi_high-aqi_low))*(aqi-aqi_low) + conc_low;
        }
        String concentration = Double.toString(conc);
        double no_of_cig = conc/(22.5);
        long numberofcig = Math.round(no_of_cig);
        cigtextupdate = numberofcig;
        cig_frag = numberofcig;
        double life_reduced = (conc-35)/10;
        double month = life_reduced*12;
        if(month<0)
            life_frag=0;
        else if(month>210)
            life_frag=210;
        else
        life_frag = Math.round(month);
        if(month<12 && month>0)
            if(month==1)
                HomeFragment.life.setText(1+" month.");
            else
                HomeFragment.life.setText(Math.round(month)+" months.");
        else if(month >12 || month ==12)
                if(life_reduced==1)
                    HomeFragment.life.setText(1+" year.");
                else
                   HomeFragment.life.setText(Math.round(life_reduced)+" years.");
        else if(month<0)
            HomeFragment.life.setText(0+" months.");
        HomeFragment.cig.setText("Oh! You smoke "+ numberofcig + " cigarettes");
    }
    public static void cigupdate(int i) {
        if(i==0)
            HomeFragment.cig.setText("Oh! You smoke "+ cigtextupdate + " cigarettes");
        else if (i==1)
            HomeFragment.cig.setText("Oh! You smoke "+ cigtextupdate*7 + " cigarettes");
        else if(i==2)
            HomeFragment.cig.setText("Oh! You smoke "+ cigtextupdate*30 + " cigarettes");




    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if(data!=null)
        if(requestCode==2)
        {
            x_cord =  data.getDoubleExtra("latitude",0);
            y_cord = data.getDoubleExtra("longitude",0);
            if(x_cord!=0 && y_cord!=0)
             jsonParse(x_cord,y_cord);
           //Toast.makeText(MainActivity.this,x_cord +", "+y_cord+" helloooo",Toast.LENGTH_SHORT).show();
            res=0;
        }
    }


}
