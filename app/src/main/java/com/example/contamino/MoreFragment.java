package com.example.contamino;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatViewInflater;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.transition.TransitionInflater;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;

public class MoreFragment extends Fragment {
    ImageView leftcloud,rightcloud,searchbt,loc,shop;
    Animation cloudanim1,cloudanim2;
    public static ImageView delhi,mumbai,ggn,london,general;
    View rootView;
    RelativeLayout airbox,citysearch;
    String city;
    private ViewPager viewPager;
    public static TextView aqi,pm25,pm10,so2,no2,o3,co,time,airtype,cityname;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       rootView = inflater.inflate(R.layout.moreinfo_frag, container, false);
        leftcloud = rootView.findViewById(R.id.cloud_left);
        rightcloud = rootView.findViewById(R.id.cloud_right);
        aqi = rootView.findViewById(R.id.aqi_text);
        shop=rootView.findViewById(R.id.shopbt);
        pm25 = rootView.findViewById(R.id.pm25);
        delhi = rootView.findViewById(R.id.del_more);
        mumbai = rootView.findViewById(R.id.mum_more);
        ggn = rootView.findViewById(R.id.ggn_more);
        london = rootView.findViewById(R.id.lon_more);
        citysearch=rootView.findViewById(R.id.city_search);
        general = rootView.findViewById(R.id.gen_more);
        cityname = rootView.findViewById(R.id.city_name);
        airbox = rootView.findViewById(R.id.air_type_box);
        airtype = rootView.findViewById(R.id.air_type);
        pm10 = rootView.findViewById(R.id.pm10);
        searchbt = rootView.findViewById(R.id.search_button);
        WormDotsIndicator wormDotsIndicator = rootView.findViewById(R.id.worm_dots_indicator);
        so2 = rootView.findViewById(R.id.so2);
        MyAdapter adapter = new MyAdapter();
        HeightWrappingViewPager pager = rootView.findViewById(R.id.viewpager);
        pager.setAdapter(adapter);
        wormDotsIndicator.setViewPager(pager);
        no2 = rootView.findViewById(R.id.no2);
        o3 = rootView.findViewById(R.id.o3);
        time = rootView.findViewById(R.id.time_change);
        co = rootView.findViewById(R.id.co);
        initialisedata(true);
        cloudanim1 = AnimationUtils.loadAnimation(getActivity(),R.anim.cloud1);
        cloudanim1.setRepeatMode(Animation.REVERSE);
        cloudanim1.setRepeatCount(Animation.INFINITE);
        leftcloud.startAnimation(cloudanim1);
        cloudanim2 = AnimationUtils.loadAnimation(getActivity(),R.anim.cloud2);
        cloudanim2.setRepeatMode(Animation.REVERSE);
        cloudanim2.setRepeatCount(Animation.INFINITE);
        rightcloud.startAnimation(cloudanim2);
     //   Toast.makeText(getContext(),String.valueOf(MainActivity.time_frag),Toast.LENGTH_SHORT).show();
        if(MainActivity.time_frag==1)
        time.setText(MainActivity.time_frag+" HOUR AGO");
        else if (MainActivity.time_frag == 0 || MainActivity.time_frag>1)
            time.setText(MainActivity.time_frag+" HOURS AGO");
        citysearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               getActivity().startActivityForResult(new Intent(getActivity(),moreinfo.class),2);
            }
        });
        shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShopFragment nextFrag= new ShopFragment();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.enter_right_anim, R.anim.exit_left_anim, R.anim.enter_left_anim, R.anim.exit_right_anim);
                transaction.replace(R.id.frame, nextFrag);
                transaction.commit();
            }
        });

        return rootView;

    }
    private void initialisedata(Boolean check){
        if(check)
        city = MainActivity.city_frag;
        else {
            city = MainActivity.cityname;
        }

        cityname.setText(city);
        if(city!=null) {

            if (city.equalsIgnoreCase("gurgaon") || city.equalsIgnoreCase("gurugram")) {
                delhi.setVisibility(View.GONE);
                ggn.setVisibility(View.VISIBLE);
                london.setVisibility(View.GONE);
                mumbai.setVisibility(View.GONE);
                general.setVisibility(View.GONE);

            }else
            if (city.equalsIgnoreCase("mumbai") || city.equalsIgnoreCase("bombay") || city.equalsIgnoreCase("Navi Mumbai")) {
                delhi.setVisibility(View.GONE);
                mumbai.setVisibility(View.VISIBLE);
                london.setVisibility(View.GONE);
                ggn.setVisibility(View.GONE);
                general.setVisibility(View.GONE);

            }else

            if (city.equalsIgnoreCase("london") || city.equalsIgnoreCase("paris") ||city.equalsIgnoreCase("United Kingdom") || city.contains("London")) {
                delhi.setVisibility(View.GONE);
                london.setVisibility(View.VISIBLE);
                ggn.setVisibility(View.GONE);
                general.setVisibility(View.GONE);
                mumbai.setVisibility(View.GONE);
            }else
            if (city.equalsIgnoreCase("delhi") || city.equalsIgnoreCase("new delhi")) {
                delhi.setVisibility(View.VISIBLE);
                ggn.setVisibility(View.GONE);
                general.setVisibility(View.GONE);
                london.setVisibility(View.GONE);
                mumbai.setVisibility(View.GONE);
            }else {
                general.setVisibility(View.VISIBLE);
                delhi.setVisibility(View.INVISIBLE);
                ggn.setVisibility(View.GONE);
                london.setVisibility(View.GONE);
                mumbai.setVisibility(View.GONE);
            }

        }

        int aqi_data=0;
        //      updatecity(MainActivity.x_cord,MainActivity.y_cord);


        if(MainActivity.aqi_frag==null) {
            aqi.setText("N/A");
            pm25.setText("N/A");

        }
        else {
            aqi.setText(MainActivity.aqi_frag);
            pm25.setText(MainActivity.aqi_frag);
        }
        if(MainActivity.aqi_frag!=null)
            aqi_data = Integer.parseInt(MainActivity.aqi_frag);
        if(MainActivity.aqi_frag==null) {
            airtype.setText("N/A");
        }
        else if (aqi_data>0 && aqi_data <= 70){
            airtype.setText("GOOD");
            airbox.setBackgroundResource(R.drawable.rect_good);
        } else if (aqi_data>70 && aqi_data <= 150){
            airtype.setText("MODERATE");
            airbox.setBackgroundResource(R.drawable.rect_moderate);
        } else if (aqi_data>150 && aqi_data <= 300){
            airtype.setText("UNHEALTHY");
            airbox.setBackgroundResource(R.drawable.rect_unhealthy);
        }
        else if (aqi_data>300){
            airtype.setText("HAZARDOUS");
            airbox.setBackgroundResource(R.drawable.rect_hazard);
        }

        if(MainActivity.pm10_frag==null)
            pm10.setText("N/A");
        else
            pm10.setText(MainActivity.pm10_frag);

        if(MainActivity.so2_frag==null)
            so2.setText("N/A");
        else
            so2.setText(MainActivity.so2_frag);

        if(MainActivity.no2_frag==null)
            no2.setText("N/A");
        else
            no2.setText(MainActivity.no2_frag);

        if(MainActivity.o3_frag==null)
            o3.setText("N/A");
        else
            o3.setText(MainActivity.o3_frag);
        if(MainActivity.co_frag==null)
            co.setText("N/A");
        else
            co.setText(MainActivity.co_frag);
    }


    @Override
    public void onResume() {
        super.onResume();
        if(MainActivity.res==0){
            initialisedata(false);
            Toast.makeText(getContext(),"data refreshed",Toast.LENGTH_SHORT).show();
        }
    }

    public MoreFragment getInstance() {
        return getInstance();
    }

    public class MyAdapter extends PagerAdapter{

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            int resId = 0;
            switch (position) {
                case 0:
                    resId = R.id.moreinfo;
                    break;
                case 1:
                    resId = R.id.maskinfo;
                    break;
            }
            return rootView.findViewById(resId);
        }
        @Override
        public int getCount() {
            return 2;
        }


        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override public void destroyItem(ViewGroup container, int position, Object object) {
            // No super
        }



    }


}
