package com.example.contamino;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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

public class searchFragment extends Fragment {
    ListView listView;
    ArrayList<String> list;
    EditText searchView;
    ArrayList<String> latitude,longitude;
    private RequestQueue mQueue;


    ArrayAdapter<String> adapter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);




    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.search_frag, container, false);


        searchView = rootView.findViewById(R.id.mySearchView);
        mQueue = Volley.newRequestQueue(getContext());
        listView = rootView.findViewById(R.id.myListView);
        list = new ArrayList<>();
        latitude = new ArrayList<>();
        longitude = new ArrayList<>();
        adapter = new ArrayAdapter<>(getContext(),android.R.layout.simple_list_item_1,list);
        listView.setAdapter(adapter);
        searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                adapter.clear();
                latitude.clear();
                longitude.clear();
                jsonParse(charSequence.toString());
                if(charSequence.toString().length()==0)
                    adapter.clear();

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getContext(),latitude.get(i)+", "+ longitude.get(i),Toast.LENGTH_SHORT).show();
                ((MainActivity)getActivity()).jsonParse(Double.valueOf(latitude.get(i)),Double.valueOf(longitude.get(i)));

                closefragment();



            }
        });

        return rootView;

    }

    private void closefragment() {
       getActivity().onBackPressed();



    }

    private void jsonParse(final String srch) {

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
