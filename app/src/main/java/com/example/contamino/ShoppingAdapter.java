package com.example.contamino;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.util.List;

public class ShoppingAdapter extends PagerAdapter {

    List<Integer> views;
    Context context;
    LayoutInflater layoutInflater;

    public ShoppingAdapter(List<Integer> views, Context context) {
        this.views = views;
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return views.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = layoutInflater.inflate(R.layout.card_item,container,false);

        ImageView eventimg = view.findViewById(R.id.filter);



        eventimg.setImageResource(views.get(position));

        /**buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), book.class);
                view.getContext().startActivity(intent);

            }
        });
         **/
        container.addView(view);
        return view;
    }

}
