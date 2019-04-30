package com.example.wifiology;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class DataActivity extends AppCompatActivity implements View.OnClickListener{

    FloatingActionButton refreshButton;
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    HashMap<String,NetworkData> networksList;
    HashMap<Long,MeasurementData> measureList;
    Date earliestTime;
    int nodeId;
    String nodeName,nodeLocation,nodeDescription;

    MaterialCardView nodeHeader;
    LinearLayout nodeGraphLayout;
    LineChart chart,chart1,chart2;
    TextView nodeNameT,nodeLocationT,nodeDescriptionT,nodeTimeT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);

        Intent intent = getIntent();
        nodeId = intent.getIntExtra(NodeActivity.EXTRA_NODE_ID,-1);
        nodeName = intent.getStringExtra(NodeActivity.EXTRA_NODE_NAME);
        nodeLocation = intent.getStringExtra(NodeActivity.EXTRA_NODE_LOC);
        nodeDescription = intent.getStringExtra(NodeActivity.EXTRA_NODE_DESC);

        recyclerView = findViewById(R.id.recyclerViewNetworks);
        //recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        NetworkData[] init = new NetworkData[1];
        init[0] = new NetworkData("Loading");
        adapter = new NetworkAdapter(init,this);
        recyclerView.setAdapter(adapter);

        nodeNameT = findViewById(R.id.nodeHeaderTitle);
        nodeNameT.setText(nodeName);
        nodeLocationT = findViewById(R.id.nodeHeaderLocation);
        nodeLocationT.setText(nodeLocation);
        nodeDescriptionT = findViewById(R.id.nodeHeaderDescription);
        nodeDescriptionT.setText(nodeDescription);
        nodeTimeT = findViewById(R.id.nodeHeaderTime);
        refreshButton = findViewById(R.id.refreshFAB);
        refreshButton.setOnClickListener(this);
        nodeHeader = findViewById(R.id.nodeInfoCard);
        nodeHeader.setOnClickListener(this);
        nodeGraphLayout = findViewById(R.id.nodeChartList);
        nodeGraphLayout.setVisibility(View.GONE);
        chart = findViewById(R.id.chart);
        chart1 = findViewById(R.id.chart1);
        chart2 = findViewById(R.id.chart2);
        LineChart[] charts = new LineChart[] {chart,chart1,chart2};

        TypedValue val = new TypedValue();
        getTheme().resolveAttribute(R.attr.colorOnSurface,val,true);
        for(int i = 0; i < charts.length; i++) {
            Description desc = new Description();
            desc.setText("");
            charts[i].setDescription(desc);
            charts[i].setNoDataText("");
            charts[i].setBorderWidth(4);
            XAxis xAxis = charts[i].getXAxis();
            xAxis.setDrawLabels(false);
            xAxis.setTextColor(val.data);
            YAxis yAxis = charts[i].getAxisRight();
            yAxis.setDrawLabels(false);
            YAxis yAxis2 = charts[i].getAxisLeft();
            yAxis2.setTextColor(val.data);
            Legend legend = charts[i].getLegend();
            legend.setTextColor(val.data);
        }

        getMeasurements();
    }

    @Override
    public void onClick(View view) {

        switch(view.getId()){
            case R.id.refreshFAB:
                getMeasurements();
                break;
            case R.id.nodeInfoCard:
                if (nodeGraphLayout.getVisibility() == View.VISIBLE){
                    nodeGraphLayout.setVisibility(View.GONE);
                }else{
                    nodeGraphLayout.setVisibility(View.VISIBLE);
                }
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
        measureList = new HashMap<>();
        earliestTime = null;
    }

    void displayData(){
        NetworkData[] data = new NetworkData[networksList.size()];
        data = networksList.values().toArray(data);
        Arrays.sort(data);
        adapter = new NetworkAdapter(data,this);
        recyclerView.setAdapter(adapter);

        MeasurementData[] data2 = new MeasurementData[measureList.size()];
        String[] times = new String[measureList.size()];
        data2 = measureList.values().toArray(data2);
        Arrays.sort(data2);
        TypedValue val = new TypedValue();
        getTheme().resolveAttribute(R.attr.colorSecondary,val,true);
        ArrayList<Entry> entries = new ArrayList<>();
        ArrayList<Entry> entries1 = new ArrayList<>();
        ArrayList<Entry> entries2 = new ArrayList<>();
        ArrayList<Entry> entries3 = new ArrayList<>();
        ArrayList<Entry> entries4 = new ArrayList<>();
        for (int i = 0; i < data2.length; i++){
            float time = (data2[i].getTime().getTime() - earliestTime.getTime());
            times[i] = time+"";
            entries.add(new Entry(time,data2[i].getMgntFrames()));
            entries1.add(new Entry(time,data2[i].getDataFrames()));
            entries2.add(new Entry(time,data2[i].getCntlFrames()));
            entries3.add(new Entry(time,data2[i].getThroughput()));
            entries4.add(new Entry(time,data2[i].getStations()));

            if (i == data2.length-1){
                nodeTimeT.setText(data2[i].getTime().toString());
            }
        }
        LineDataSet dataSet = new LineDataSet(entries,"Management Frames");
        LineDataSet dataSet1 = new LineDataSet(entries1,"Data Frames");
        LineDataSet dataSet2 = new LineDataSet(entries2,"Control Frames");
        LineDataSet dataSet3 = new LineDataSet(entries3,"Data Throughput");
        LineDataSet dataSet4 = new LineDataSet(entries4,"Station Count");
        dataSet.setDrawValues(false);
        dataSet1.setDrawValues(false);
        dataSet2.setDrawValues(false);
        dataSet3.setDrawValues(false);
        dataSet4.setDrawValues(false);
        dataSet.setHighlightEnabled(false);
        dataSet1.setHighlightEnabled(false);
        dataSet2.setHighlightEnabled(false);
        dataSet3.setHighlightEnabled(false);
        dataSet4.setHighlightEnabled(false);
        dataSet.setColor(getResources().getColor(android.R.color.holo_red_dark));
        dataSet1.setColor(getResources().getColor(android.R.color.holo_green_dark));
        dataSet2.setColor(getResources().getColor(android.R.color.holo_blue_dark));
        dataSet3.setColor(val.data);
        dataSet4.setColor(val.data);
        dataSet.setCircleColor(getResources().getColor(android.R.color.holo_red_dark));
        dataSet1.setCircleColor(getResources().getColor(android.R.color.holo_green_dark));
        dataSet2.setCircleColor(getResources().getColor(android.R.color.holo_blue_dark));
        dataSet3.setCircleColor(val.data);
        dataSet4.setCircleColor(val.data);
        ArrayList<ILineDataSet> sets = new ArrayList<>();
        sets.add(dataSet);
        sets.add(dataSet1);
        sets.add(dataSet2);
        chart.setData(new LineData(sets));
        chart1.setData(new LineData(dataSet3));
        chart2.setData(new LineData(dataSet4));
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

    //for now: get service sets, avg number of stations on the service set, range of times of measurement
    void getMeasurements(){
        HashMap<String, String> params = new HashMap<String, String>();
        //params.put("channel","10");
        params.put("limit","20");
        //params.put("lastPriorMeasurementID","potato");
        AsyncHTTPClient.getString("nodes/" + nodeId + "/measurements", params, true, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //run this on its own thread later?
                try {
                    JSONArray array = new JSONArray(response);
                    SimpleDateFormat dateForm = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
                    setupDataList();

                    for (int i = array.length()-1; i >= 0; i--){
                        JSONObject ob = array.getJSONObject(i);
                        JSONObject obM = ob.getJSONObject("measurement");
                        JSONObject obD = obM.getJSONObject("dataCounters");
                        Date timeStamp = parseDate(obM.getString("measurementStartTime"),dateForm);
                        int management = obD.getInt("managementFrameCount");
                        int dataFrames = obD.getInt("dataFrameCount");
                        int cntlFrames = obD.getInt("controlFrameCount");
                        int throughput = obD.getInt("dataThroughputIn");
                        int stations = ob.getJSONArray("stations").length();

                        if (!measureList.containsKey(timeStamp.getTime())) {
                            measureList.put(timeStamp.getTime(), new MeasurementData(timeStamp, management, dataFrames, cntlFrames, throughput, stations));
                        }else{
                            measureList.get(timeStamp.getTime()).addData(management,dataFrames,cntlFrames,throughput,stations);
                        }
                        if (earliestTime == null){
                            earliestTime = timeStamp;
                        }else{
                            if (earliestTime.compareTo(timeStamp) > 0){
                                earliestTime = timeStamp;
                            }
                        }

                        JSONArray serviceSetArray = ob.getJSONArray("serviceSets");
                        for (int j = 0; j < serviceSetArray.length(); j++){
                            JSONObject ob2 = serviceSetArray.getJSONObject(j);
                            String serviceName = (ob2.getString("networkName"));
                            String bssid = ob2.getString("bssid");
                            if (!networksList.containsKey(serviceName)){
                                NetworkData newData = new NetworkData(serviceName);
                                newData.addBssid(bssid);
                                networksList.put(serviceName,newData);
                            }else{
                                NetworkData data = networksList.get(serviceName);
                                if (!data.getBssids().contains(bssid)){
                                    data.addBssid(bssid);
                                }
                            }
                            try{
                                JSONObject ob3 = ob2.getJSONObject("jitterMeasurement");
                                float avgJitter = (ob3.getInt("avgJitter") / 1000f);
                                float interval = ob3.getInt("beaconInterval");
                                float res = avgJitter / interval;
                                NetworkData data = networksList.get(serviceName);
                                data.setMngtFrameRatio(res);
                            }catch (JSONException error){
                                Log.e("Wifiology","Error" + "" + ": " + error.toString());
                            }
                        }
                    }

                    displayData();
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
