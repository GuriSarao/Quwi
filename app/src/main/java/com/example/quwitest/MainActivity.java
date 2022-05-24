package com.example.quwitest;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getName();
    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;
    private String url = "https://api.quwi.com/v2/auth/login";
    private String msgUrl = "https://api.quwi.com/v2/chat-channels";
    CustomAdapter customAdapter;

    ProgressBar progressBar;
    String[] personNames = new String[0];
    //    Arrays.asList("Person 1", "Person 2", "Person 3", "Person 4")
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Messages");
            actionBar.setHomeAsUpIndicator(R.drawable.back_button);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        progressBar = (ProgressBar) findViewById(R.id.progressBar);


        // get the reference of RecyclerView
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        // set a LinearLayoutManager with default vertical orientation
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        //  call the constructor of CustomAdapter to send the reference and data to Adapter
        customAdapter = new CustomAdapter(MainActivity.this, personNames);
        recyclerView.setAdapter(customAdapter); // set the Adapter to RecyclerView
        loginApiCall();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the main_menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; go home
                Intent intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;

            case R.id.item1:
                //your action
                break;
            case R.id.item2:
                //your action
                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
    }

    private void getMessageListDetailApi(String token) {
        //RequestQueue initialized
        mRequestQueue = Volley.newRequestQueue(this);

        //String Request initialized
        mStringRequest = new StringRequest(Request.Method.GET, msgUrl, new Response.Listener<String>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(String response) {

                progressBar.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                try {
                    JSONObject obj = new JSONObject(response);
                    JSONArray channels = obj.getJSONArray("channels");
                    String[] personNames = new String[channels.length()];
                    for (int i = 0; i < channels.length(); i++) {
                        personNames[i] = channels.getString(i);
                    }
                    customAdapter = new CustomAdapter(MainActivity.this, personNames);
                    recyclerView.setAdapter(customAdapter); // set the Adapter to RecyclerView
                } catch (JSONException e) {
                    progressBar.setVisibility(View.GONE);
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getApplicationContext(),"Check your internet and try again.", Toast.LENGTH_LONG).show();
                Log.i(TAG, "Error :" + error.toString());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }
        };

        mRequestQueue.add(mStringRequest);
    }


    private void loginApiCall() {

        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        //RequestQueue initialized
        mRequestQueue = Volley.newRequestQueue(this);

        //String Request initialized
        mStringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    String token = obj.getString("token");

                    Log.d("TOKENsss", String.valueOf(token));
                    getMessageListDetailApi(token);
//                Toast.makeText(getApplicationContext(),"Response :" + response.toString(), Toast.LENGTH_LONG).show();//display the response on screen
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                progressBar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(),"Check your internet and try again.", Toast.LENGTH_LONG).show();
                Log.i(TAG, "Error :" + error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", "useruser1@gmail.com");
                params.put("password", "123123");
                return params;
            }
        };

        mRequestQueue.add(mStringRequest);
    }
}