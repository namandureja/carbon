package com.example.contamino;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.gigamole.infinitecycleviewpager.HorizontalInfiniteCycleViewPager;
import com.gigamole.infinitecycleviewpager.VerticalInfiniteCycleViewPager;
import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator;


import java.util.ArrayList;
import java.util.List;

public class ShopFragment extends Fragment {
    Button filter,mask;
    HorizontalScrollView scroll1,scroll2;
    TextView value,best;
    ImageView backbt;
    HeightWrappingViewPager viewPager;

    List<Integer> eventlist;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.shopping_frag, container, false);



        backbt= rootView.findViewById(R.id.backbt);
        best =rootView.findViewById(R.id.best);
        value = rootView.findViewById(R.id.value);
        filter = rootView.findViewById(R.id.purifierbt);
        mask = rootView.findViewById(R.id.airmasksbt);
        scroll1 =rootView.findViewById(R.id.horizontalScrollView1);
        scroll2 =rootView.findViewById(R.id.horizontalScrollView2);
        eventlist = new ArrayList<>();
        initdata();
        ShoppingAdapter myAdapter = new ShoppingAdapter(eventlist,getActivity());
        viewPager = rootView.findViewById(R.id.viewpager);
        viewPager.setAdapter(myAdapter);
        viewPager.setPadding(130,0,130,0);
        WormDotsIndicator wormDotsIndicator = rootView.findViewById(R.id.worm_dots_indicator);
        wormDotsIndicator.setViewPager(viewPager);


        backbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new MoreFragment();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.enter_left_anim, R.anim.exit_right_anim, R.anim.enter_right_anim, R.anim.exit_left_anim);
                transaction.replace(R.id.frame, fragment);
                transaction.commit();
                MainActivity.navView.show(1,true);
            }
        });
        mask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filter.setBackgroundResource(R.drawable.shop_bt2);
                mask.setBackgroundResource(R.drawable.shop_bt1);
                filter.setTextColor(getResources().getColor(R.color.unselected));
                mask.setTextColor(getResources().getColor(R.color.selected));
                scroll1.setVisibility(View.VISIBLE);
                scroll2.setVisibility(View.VISIBLE);
                value.setVisibility(View.VISIBLE);
                best.setVisibility(View.VISIBLE);
                viewPager.setVisibility(View.INVISIBLE);
            }
        });
        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filter.setBackgroundResource(R.drawable.shop_bt1);
                mask.setBackgroundResource(R.drawable.shop_bt2);
                filter.setTextColor(getResources().getColor(R.color.selected));
                mask.setTextColor(getResources().getColor(R.color.unselected));
                scroll1.setVisibility(View.INVISIBLE);
                scroll2.setVisibility(View.INVISIBLE);
                value.setVisibility(View.INVISIBLE);
                best.setVisibility(View.INVISIBLE);
                viewPager.setVisibility(View.VISIBLE);
            }
        });

        return rootView;

    }
    private void initdata()
    {
        eventlist.add(R.drawable.dyson);
       eventlist.add(R.drawable.philips);
        eventlist.add(R.drawable.mi);
    }

}
