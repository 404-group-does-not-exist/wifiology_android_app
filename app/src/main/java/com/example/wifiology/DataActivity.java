package com.example.wifiology;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class DataActivity extends AppCompatActivity implements View.OnClickListener{

    Button refreshButton;
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    List<NetworkData> networksList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);

        recyclerView = findViewById(R.id.recyclerViewNetworks);
        //recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        networksList = new ArrayList<>();
        networksList.add(new NetworkData("Hi"));
        networksList.add(new NetworkData("Hello"));
        networksList.add(new NetworkData("How Are You"));
        networksList.add(new NetworkData("Hello"));
        networksList.add(new NetworkData("Hello"));
        networksList.add(new NetworkData("Hello"));
        networksList.add(new NetworkData("Hello"));
        networksList.add(new NetworkData("Hello"));
        networksList.add(new NetworkData("Hello"));
        networksList.add(new NetworkData("Hello"));
        networksList.add(new NetworkData("Hello"));
        adapter = new NetworkAdapter(networksList,this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {

        /*switch(view.getId()){
            case R.id.userButton:
                showUserDialog();
                break;
            default:
                break;
        }*/
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
}
