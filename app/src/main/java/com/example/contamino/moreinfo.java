package com.example.contamino;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class moreinfo extends AppCompatActivity {

    ListView listView;
    ArrayList<String> list;
    EditText searchView;
    ImageView cross;
    private RequestQueue mQueue;
    ArrayList<String> latitude,longitude;


    MyAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moreinfo);
        searchView = findViewById(R.id.mySearchView);
        mQueue = Volley.newRequestQueue(this);
        listView = findViewById(R.id.myListView);
        list = new ArrayList<>();
        latitude = new ArrayList<>();
        cross = findViewById(R.id.imageView4);
        longitude = new ArrayList<>();
        adapter = new MyAdapter(this,list);
        listView.setAdapter(adapter);
        cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        TextView empty = findViewById(R.id.textView4);
        searchView.isFocused();
        searchView.requestFocus();
        searchView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //You can identify which key pressed buy checking keyCode value with KeyEvent.KEYCODE_
                if(keyCode == KeyEvent.KEYCODE_DEL) {
                    if(searchView.getText().toString().isEmpty()) {
                        adapter.clear();
                        latitude.clear();
                        longitude.clear();
                        adapter.clearData();
                        adapter.notifyDataSetChanged();
                    }

                }
                return false;
            }
        });
        searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                adapter.clear();
                latitude.clear();
                adapter.notifyDataSetChanged();
                longitude.clear();
                jsonParse(charSequence.toString());
                if(charSequence.toString().isEmpty()) {
                    adapter.clear();
                    latitude.clear();
                    longitude.clear();
                    adapter.clearData();
                    adapter.notifyDataSetChanged();
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Toast.makeText(moreinfo.this,latitude.get(i)+", "+ longitude.get(i),Toast.LENGTH_SHORT).show();
                Intent intent=new Intent();
                try {
                    if (latitude.size() != 0 && longitude.size() != 0) {
                        intent.putExtra("latitude", Double.valueOf(latitude.get(i)));
                        intent.putExtra("longitude", Double.valueOf(longitude.get(i)));
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
                setResult(2,intent);
                MainActivity.res= 0;
                finish();
            }
        });

    }


    private void jsonParse(String srch) {

        String url =  "https://api.waqi.info/search/?token=be941d5d53af0932dad89815a54cd2e8f28ff0a3&keyword="+srch;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray data = response.getJSONArray("data");
                            latitude.clear();
                            longitude.clear();

                            for (int i = 0; i < data.length(); i++) {
                                JSONObject city = data.getJSONObject(i);
                                JSONObject station = city.getJSONObject("station");
                                String name = station.getString("name");
                                JSONArray geo = station.getJSONArray("geo");
                                latitude.add(geo.getString(0));
                                longitude.add(geo.getString(1));
                                list.add(name);
                                adapter.notifyDataSetChanged();
                            // mTextViewResult.append(firstName + ", " + String.valueOf(age) + ", " + mail + "\n\n");
                             }
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

}
