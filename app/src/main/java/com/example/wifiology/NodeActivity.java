package com.example.wifiology;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupMenu;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;

public class NodeActivity extends AppCompatActivity implements View.OnClickListener{

    public static final String EXTRA_NODE_ID = "com.example.wifiology.nodeID";
    public static final String EXTRA_NODE_LOC = "com.example.wifiology.nodeLocation";
    public static final String EXTRA_NODE_NAME = "com.example.wifiology.nodeName";
    public static final String EXTRA_NODE_DESC = "com.example.wifiology.nodeDescription";
    FloatingActionButton refreshButton;
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    HashMap<String,NodeData> nodesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_node);

        recyclerView = findViewById(R.id.recyclerViewNodes);
        //recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        NodeData[] init = new NodeData[1];
        init[0] = new NodeData("Loading",-1, "", "");
        adapter = new NodeAdapter(init,this);
        recyclerView.setAdapter(adapter);

        refreshButton = findViewById(R.id.refreshFABNode);
        refreshButton.setOnClickListener(this);

        getNodes();
    }

    @Override
    public void onClick(View view) {

        switch(view.getId()){
            case R.id.refreshFABNode:
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

    void showUserDialog(){
        PopupMenu popup = new PopupMenu(NodeActivity.this, findViewById(R.id.barOverflowButton));
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

    void logOut(){
        SharedPrefs.putString(SharedPrefs.username,"",getApplicationContext());
        SharedPrefs.putString(SharedPrefs.password,"",getApplicationContext());
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    void setupDataList(){
        nodesList = new HashMap<>();
    }

    void displayData(){
        NodeData[] data = new NodeData[nodesList.size()];
        data = nodesList.values().toArray(data);
        Arrays.sort(data);
        adapter = new NodeAdapter(data,this);
        recyclerView.setAdapter(adapter);
    }

    void getNodes(){
        AsyncHTTPClient.getString("nodes", null, true, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray array = new JSONArray(response);
                    setupDataList();

                    for (int i = 0; i < array.length(); i++){
                        JSONObject ob = array.getJSONObject(i);
                        int nodeId = ob.getInt("nodeID");
                        String nodeName = ob.getString("nodeName");
                        String nodeLocation = ob.getString("nodeLocation");
                        String nodeDescription = ob.getString("nodeDescription");
                        nodesList.put(nodeName,new NodeData(nodeName,nodeId,nodeLocation,nodeDescription));
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
