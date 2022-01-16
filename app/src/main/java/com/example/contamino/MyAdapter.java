package com.example.contamino;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class MyAdapter extends ArrayAdapter<String> {
    ArrayList<String> data;

    public MyAdapter(Context context , ArrayList<String> list){
        super(context,0,list);
        this.data = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String item = getItem(position);
        if(convertView==null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.model,parent,false);
        }
        TextView list_text=convertView.findViewById(R.id.textsearch);
        list_text.setText(item);

        return convertView;
    }

    public void clearData() {
        data.clear();
    }
}
