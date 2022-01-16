package com.example.contamino;

import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.transition.Explode;
import androidx.transition.Fade;
import androidx.transition.TransitionInflater;

import static com.example.contamino.MainActivity.home;
import static com.example.contamino.MainActivity.more;

public class HomeFragment extends Fragment {
    ImageView leftcloud,rightcloud,cigclick,loc;
    Animation cloudanim1,cloudanim2;
    public static ImageView delhi,mumbai,ggn,london,general;


    public static TextView cityname,aqi,cig,life,day,viewmore,airtype,faq,faq2;
    int n =1;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.home_frag, container, false);
        leftcloud = rootView.findViewById(R.id.cloud_left);
        rightcloud = rootView.findViewById(R.id.cloud_right);
        cityname = rootView.findViewById(R.id.city_name);
        aqi = rootView.findViewById(R.id.aqi_text);
        cig = rootView.findViewById(R.id.cig_calc);
        life = rootView.findViewById(R.id.life_span);
        day = rootView.findViewById(R.id.cig_day);
        cigclick = rootView.findViewById(R.id.cig_click);
        viewmore = rootView.findViewById(R.id.view_more);
        delhi = rootView.findViewById(R.id.del_more);
        mumbai = rootView.findViewById(R.id.mum_more);
        ggn = rootView.findViewById(R.id.ggn_more);
        general = rootView.findViewById(R.id.gen_more);
        london = rootView.findViewById(R.id.lon_more);
        faq2 = rootView.findViewById(R.id.faq_bt2);
        initialisedata();
        faq = rootView.findViewById(R.id.faq_bt);
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        try {
            display.getRealSize(size);
        } catch (NoSuchMethodError err) {
            display.getSize(size);
        }
        int width = size.x;
        cityname.setMaxWidth((int)(width*0.6));
        cityname.setSelected(true);

    //    Toast.makeText(getContext(),"the aqi is = "+aqi_data,Toast.LENGTH_SHORT).show();
        faq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FAQfragment nextFrag= new FAQfragment();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.enter_right_anim, R.anim.exit_left_anim, R.anim.enter_left_anim, R.anim.exit_right_anim);
                transaction.replace(R.id.frame, nextFrag);
                transaction.commit();
            }
        });
        faq2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FAQfragment nextFrag= new FAQfragment();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.enter_right_anim, R.anim.exit_left_anim, R.anim.enter_left_anim, R.anim.exit_right_anim);
                transaction.replace(R.id.frame, nextFrag);
                transaction.commit();
            }
        });


        cigclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(n==0)
                {
                    day.setText("everyday");
                    n=1;
                    MainActivity.cigupdate(0);
                }else                 if(n==1)
                {
                    day.setText("weekly");
                    n=2;
                    MainActivity.cigupdate(1);
                }else if (n==2)
                {
                    day.setText("monthly");
                    n=0;
                    MainActivity.cigupdate(2);
                }

            }
        });

         cloudanim1 = AnimationUtils.loadAnimation(getActivity(),R.anim.cloud1);
         cloudanim1.setRepeatMode(Animation.REVERSE);
         cloudanim1.setRepeatCount(Animation.INFINITE);
         leftcloud.startAnimation(cloudanim1);
        cloudanim2 = AnimationUtils.loadAnimation(getActivity(),R.anim.cloud2);
        cloudanim2.setRepeatMode(Animation.REVERSE);
        cloudanim2.setRepeatCount(Animation.INFINITE);
        rightcloud.startAnimation(cloudanim2);
        final int colorFrom = getResources().getColor(R.color.black);
        final int colorTo = getResources().getColor(R.color.white);
        viewmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).changeFragment(new MoreFragment(),"more");
                ((MainActivity)getActivity()).colorchange(colorFrom,colorTo,MainActivity.more);
                ((MainActivity)getActivity()).colorchange(colorTo,colorFrom, MainActivity.home);
                ((MainActivity)getActivity()).swapview(2);

            }

            });

        return rootView;

    }

    private void initialisedata() {
        String city = MainActivity.city_frag;
        String aqi_data = MainActivity.aqi_frag;
        long life_data = MainActivity.life_frag;
        long cig_data = MainActivity.cig_frag;

        if(life_data>12 || life_data ==12) {
            if (Math.round(life_data / 12) == 1)
                life.setText(1 + " year.");
            else
                life.setText(Math.round(life_data / 12) + " years.");
        }
        if(Math.round(life_data)==1)
            life.setText(1 + " month." );
        else if(life_data<12)
            life.setText(Math.round(life_data) + " months." );

        cig.setText("Oh! You smoke "+ cig_data + " cigarettes");

        cityname.setText(city);
        aqi.setText(aqi_data);
        if(city!= null) {

            if (city.equalsIgnoreCase("gurgaon") || city.equalsIgnoreCase("gurugram")) {
                delhi.setVisibility(View.INVISIBLE);
                ggn.setVisibility(View.VISIBLE);
                london.setVisibility(View.GONE);
                mumbai.setVisibility(View.GONE);
                general.setVisibility(View.GONE);
            }else if (city.equalsIgnoreCase("mumbai") || city.equalsIgnoreCase("bombay") || city.equalsIgnoreCase("Navi Mumbai")) {
                delhi.setVisibility(View.INVISIBLE);
                mumbai.setVisibility(View.VISIBLE);
                london.setVisibility(View.GONE);
                general.setVisibility(View.GONE);
                ggn.setVisibility(View.GONE);
            }else if (city.equalsIgnoreCase("london") || city.equalsIgnoreCase("paris") || city.equalsIgnoreCase("United Kingdom") || city.contains("London")) {
                delhi.setVisibility(View.INVISIBLE);
                london.setVisibility(View.VISIBLE);
                ggn.setVisibility(View.GONE);
                general.setVisibility(View.GONE);
                mumbai.setVisibility(View.GONE);
            }else if (city.equalsIgnoreCase("delhi") || city.equalsIgnoreCase("new delhi")) {
                delhi.setVisibility(View.VISIBLE);
                ggn.setVisibility(View.GONE);
                general.setVisibility(View.GONE);
                london.setVisibility(View.GONE);
                mumbai.setVisibility(View.GONE);
            } else {
                general.setVisibility(View.VISIBLE);
                delhi.setVisibility(View.INVISIBLE);
                ggn.setVisibility(View.GONE);
                london.setVisibility(View.GONE);
                mumbai.setVisibility(View.GONE);
            }
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        initialisedata();
    }
}
