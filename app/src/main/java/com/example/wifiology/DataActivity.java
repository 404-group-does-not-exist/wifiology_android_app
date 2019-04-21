package com.example.wifiology;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class DataActivity extends AppCompatActivity implements View.OnClickListener{

    FloatingActionButton refreshButton;
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    HashMap<Integer,NetworkData> networksList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);

        recyclerView = findViewById(R.id.recyclerViewNetworks);
        //recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        NetworkData[] init = new NetworkData[1];
        init[0] = new NetworkData("Loading");
        adapter = new NetworkAdapter(init,this);
        recyclerView.setAdapter(adapter);

        refreshButton = findViewById(R.id.refreshFAB);
        refreshButton.setOnClickListener(this);

        getNodes();
    }

    @Override
    public void onClick(View view) {

        switch(view.getId()){
            case R.id.refreshFAB:
                getNodes();
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // handle button activities
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.barOverflowButton) {
            showUserDialog();
        }
        return super.onOptionsItemSelected(item);
    }

    void logOut(){
        SharedPrefs.putString(SharedPrefs.username,"",getApplicationContext());
        SharedPrefs.putString(SharedPrefs.password,"",getApplicationContext());
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    void showUserDialog(){
        PopupMenu popup = new PopupMenu(DataActivity.this, findViewById(R.id.barOverflowButton));
        //Inflating the Popup using xml file
        popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());

        popup.getMenu().getItem(0).setTitle(SharedPrefs.getString(SharedPrefs.username,this));

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch(item.getItemId()){
                    case R.id.popupLogOut:
                        logOut();
                        break;
                    default:
                        break;
                }
                return true;
            }
        });

        popup.show();
    }

    void setupDataList(){
        networksList = new HashMap<>();
    }

    void displayData(){
        NetworkData[] data = new NetworkData[networksList.size()];
        adapter = new NetworkAdapter(networksList.values().toArray(data),this);
        recyclerView.setAdapter(adapter);
    }

    Date parseDate(String date, SimpleDateFormat form){
        Date res = new Date(0L);
        try {
            res = form.parse(date);
        }catch (Exception e){
            Log.e("Wifiology",e.toString());
        }
        return res;
    }

    void getNodes(){
        AsyncHTTPClient.getString("users/me/nodes", null, true, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray array = new JSONArray(response);
                    setupDataList();

                    for (int i = 0; i < array.length(); i++){
                        JSONObject ob = array.getJSONObject(i);
                        int nodeId = ob.getInt("nodeID");
                        boolean isLast = false;
                        if (i == array.length() - 1){
                            isLast = true;
                        }
                        getMeasurements(nodeId,isLast);
                    }
                }catch (JSONException error){
                    Log.e("Wifiology","Error " + "" + ": " + error.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Wifiology","Error " + "" + ": " + error.toString());
            }
        });
    }

    //for now: get service sets, avg number of stations on the service set, range of times of measurement
    void getMeasurements(int nodeId, final boolean isLast){
        //put stuff for last measurement id and shit here later maybe i dunno
        AsyncHTTPClient.getString("nodes/" + nodeId + "/measurements", null, true, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //run this on its own thread later?
                try {
                    JSONArray array = new JSONArray(response);
                    SimpleDateFormat dateForm = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'.000Z'", Locale.US);

                    for (int i = 0; i < array.length(); i++){
                        JSONObject ob = array.getJSONObject(i);
                        JSONObject obM = ob.getJSONObject("measurement");

                        JSONArray serviceSetArray = ob.getJSONArray("serviceSets");
                        for (int j = 0; j < serviceSetArray.length(); j++){
                            JSONObject ob2 = serviceSetArray.getJSONObject(j);
                            int setId = Integer.parseInt(ob2.getString("serviceSetID"));   //ARE THESE UNIQUE I HAVE NO IDEA
                            if (!networksList.containsKey(setId)){
                                String serviceName = ob2.getString("networkName");
                                NetworkData newData = new NetworkData(serviceName);

                                String begin = obM.getString("measurementStartTime");
                                Date beginDate = parseDate(begin,dateForm);
                                newData.setEarliestTime(beginDate);
                                networksList.put(setId,newData);
                            }
                        }
                        if (i == array.length() - 1){
                            String end = obM.getString("measurementEndTime");
                            Date endDate = parseDate(end,dateForm);

                            Integer[] keys = new Integer[networksList.keySet().size()];
                            keys = networksList.keySet().toArray(keys);
                            for (int j = 0; j < keys.length; j++){
                                networksList.get(keys[j]).setLatestTime(endDate);
                            }
                        }
                    }

                    if (isLast){
                        displayData();
                    }
                }catch (JSONException error){
                    Log.e("Wifiology","Error " + "" + ": " + error.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Wifiology","Error " + "" + ": " + error.toString());
            }
        });
    }
}
